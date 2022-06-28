package de.petropia.chickenLeagueHost.mysql;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.mysql.cj.jdbc.MysqlDataSource;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.arena.ArenaMode;
import de.petropia.chickenLeagueHost.arena.GameState;

public class MySQLManager {
	private static MysqlDataSource datasource;
	private static InputStream setupFile;
	private static String version = "1_1_1";

	public static Boolean setup() {
		Constants.plugin.getLogger().info("§2Starting Database Setup");
		setupFile = Constants.setupFile;
		try {
			connect();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static void connect() throws SQLException {

		if (Constants.plugin.getConfig().getBoolean("debug") == true) {
			Constants.plugin.getLogger().info("MySqlCredentails [Host: " + Constants.config.getString("Database.Host") + " Port: " + Constants.config.getInt("Database.Port") + " Database: " + Constants.config.getString("Database.Database") + " User: " + Constants.config.getString("Database.User")
					+ " Password: " + Constants.config.getString("Database.Password") + "]");
		}
		datasource = new MysqlDataSource();
		datasource.setServerName(Constants.config.getString("Database.Host"));
		datasource.setPortNumber(Constants.config.getInt("Database.Port"));
		datasource.setDatabaseName(Constants.config.getString("Database.Database"));
		datasource.setUser(Constants.config.getString("Database.User"));
		datasource.setPassword(Constants.config.getString("Database.Password"));
		Constants.plugin.getLogger().info("§2Connecting to Database");
		try (Connection connection = datasource.getConnection()) {
			if (!connection.isValid(1000)) {
				disableplugin();
				throw new SQLException("Could not establish database connection.");
			}
		}

		try {
			Constants.plugin.getLogger().info("§2Initialize Tables");
			setupDB();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			disableplugin();
		}

	}

	private static void setupDB() throws IOException, SQLException {
		String setup;
		try (InputStream in = setupFile) {
			setup = new String(in.readAllBytes());
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		String[] queries = setup.split(";");
		for (String query : queries) {
			if (query.isBlank())
				continue;
			try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.execute();
			}
		}
		setupFile.close();
		Constants.plugin.getLogger().info("§2Database setup complete.");
	}

	private static void disableplugin() {
		Constants.plugin.getLogger().warning("Disableing plugins because of a SQL-Exception!");
		Bukkit.getPluginManager().disablePlugin(Constants.plugin);
	}

	public static void updateArena(Arena arena) {
		if(!arena.isRegistered()) {
			return;
		}
		Bukkit.getScheduler().runTaskAsynchronously(Constants.plugin, () -> {
			try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("REPLACE " + version + "_Arenas(ArenaName, ArenaState, Type, Players, Server) VALUES (?, ?, ?, ?, ?)")) {
				stmt.setString(1, arena.getName());
				stmt.setString(2, arena.getGameState().toString());
				stmt.setString(3, arena.getArenaMode().toString());
				stmt.setInt(4, arena.getPlayers().size());
				stmt.setString(5, Constants.serverName);
				stmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	public static void addArena(Arena arena) {
		Bukkit.getScheduler().runTaskAsynchronously(Constants.plugin, () -> {
			try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + version + "_Arenas(ArenaName, ArenaState, Type, Players, Server) VALUES (?, ?, ?, ?, ?)")) {
				stmt.setString(1, arena.getName());
				stmt.setString(2, arena.getGameState().toString());
				stmt.setString(3, arena.getArenaMode().toString());
				stmt.setInt(4, arena.getPlayers().size());
				stmt.setString(5, Constants.serverName);
				stmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	public static void deleteArena(String arenaUUID) {
		Bukkit.getScheduler().runTaskAsynchronously(Constants.plugin, () -> {
			try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + version + "_Arenas WHERE ArenaName = ?;")) {
				stmt.setString(1, arenaUUID);
				stmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	public static void purgeDatabase() {
		try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + version + "_Arenas WHERE Server = ?;")) {
			stmt.setString(1, Constants.serverName);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + version + "_Teleport WHERE Server = ?;")) {
			stmt.setString(1, Constants.serverName);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<ArenaRecord> readArenas() {
		try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT ArenaName, ArenaState, Players, Type, Server FROM " + version + "_Arenas;")) {
			ResultSet resultSet = stmt.executeQuery();
			ArrayList<ArenaRecord> arenaRecords = new ArrayList<>();
			while (resultSet.next()) {
				String arenaName = resultSet.getString("ArenaName");
				GameState gameState = GameState.valueOf(resultSet.getString("ArenaState"));
				int players = resultSet.getInt("Players");
				ArenaMode mode = ArenaMode.valueOf(resultSet.getString("Type"));
				String server = resultSet.getString("Server");
				arenaRecords.add(new ArenaRecord(arenaName, mode, players, gameState, server));
			}
			return arenaRecords;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static @Nullable String readPlayerTeleport(Player player) {
		try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT ArenaName FROM " + version + "_Teleport WHERE PlayerName = ?;")) {
			stmt.setString(1, player.getName());
			ResultSet resultSet = stmt.executeQuery();
			String arenaname = null;
			if (resultSet.next()) {
				arenaname = resultSet.getString("ArenaName");
			}
			if (arenaname == null) {
				return null;
			} else {
				return arenaname;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void deletePlayerFromTeleport(String player) {
		Bukkit.getScheduler().runTaskAsynchronously(Constants.plugin, () -> {
			try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + version + "_Teleport WHERE PlayerName = ?;")) {
				stmt.setString(1, player);
				stmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	public static void addPlayerToTeleport(String player, String arena, String server) {
		Bukkit.getScheduler().runTaskAsynchronously(Constants.plugin, () -> {
			try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + version + "_Teleport(PlayerName, ArenaName, Server) VALUES (?, ?, ?)")) {
				stmt.setString(1, player);
				stmt.setString(2, arena);
				stmt.setString(3, server);
				stmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}
}
