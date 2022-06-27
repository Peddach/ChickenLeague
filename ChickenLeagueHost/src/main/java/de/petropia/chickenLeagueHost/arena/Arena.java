package de.petropia.chickenLeagueHost.arena;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.events.GameStateChangeEvent;
import de.petropia.chickenLeagueHost.events.PlayerJoinArenaEvent;
import de.petropia.chickenLeagueHost.events.PlayerQuitArenaEvent;
import de.petropia.chickenLeagueHost.mysql.MySQLManager;
import de.petropia.chickenLeagueHost.team.ChickenLeagueTeam;
import de.petropia.chickenLeagueHost.util.CloudNetAdapter;
import de.petropia.chickenLeagueHost.util.MessageSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class Arena {
	
	private static final List<Arena> ARENAS = new ArrayList<>();
	
	private final String name;
	private GameState gamestate;
	private final ArenaMode arenaMode;
	private final List<Player> players = new ArrayList<>();
	private final World world;
	private int maxPlayer;
	private final ChickenLeagueTeam team1;
	private final ChickenLeagueTeam team2;
	private final Location[] team1Spawns;
	private final Location[] team2Spawns;
	private final Location middle;
	private final ChickenLeagueBall ball;
	private boolean registered = false;
	
	public Arena(ArenaMode mode) {
		setGamestate(GameState.WAITING);
		this.name = getRandomString();
		this.arenaMode = mode;
		
		MultiverseCore mvCore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		MVWorldManager worldManager = mvCore.getMVWorldManager();
		if (mode == ArenaMode.FIVE_VS_FIVE) {
			worldManager.cloneWorld("FIVE_VS_FIVE", name);
			maxPlayer = 5+5;
		}
		if (mode == ArenaMode.THREE_VS_THREE) {
			worldManager.cloneWorld("THREE_VS_THREE", name);
			maxPlayer = 3+3;
		}
		if (mode == ArenaMode.ONE_VS_ONE) {
			worldManager.cloneWorld("ONE_VS_ONE", name);
			maxPlayer = 1+1;
		}
		world = Bukkit.getWorld(name);
		applyGameRules();
		
		team1 = new ChickenLeagueTeam(maxPlayer, Component.text("Team 1").color(NamedTextColor.BLUE));
		team2 = new ChickenLeagueTeam(maxPlayer, Component.text("Team 2").color(NamedTextColor.RED));
		team1.setBallChecker(loadBallChecker(1, team1));
		team2.setBallChecker(loadBallChecker(2, team2));
		
		team2Spawns = loadSpawns(2);
		team1Spawns = loadSpawns(1);
		middle = loadMiddleLocation();
		
		ball = new ChickenLeagueBall(this);
		
		registerArena();
		
	}
	
	private BallChecker loadBallChecker(int teamNumber, ChickenLeagueTeam team) {
		final int x1 = Constants.config.getInt(arenaMode.name() + ".Team" + teamNumber + ".Goal.X1");
		final int x2 = Constants.config.getInt(arenaMode.name() + ".Team" + teamNumber + ".Goal.X2");
		final int z1 = Constants.config.getInt(arenaMode.name() + ".Team" + teamNumber + ".Goal.Z1");
		final int z2 = Constants.config.getInt(arenaMode.name() + ".Team" + teamNumber + ".Goal.X2");
		return new BallChecker(x1, z1, x2, z2, this, team);
	}

	private Location loadMiddleLocation() {
		final double x = Constants.config.getDouble(arenaMode.name() + ".Middle.X");
		final double y = Constants.config.getDouble(arenaMode.name() + ".Middle.Y");
		final double z = Constants.config.getDouble(arenaMode.name() + ".Middle.Z");
		final float yaw = Constants.config.getLong(arenaMode.name() + ".Middle.Yaw");
		final float pitch = Constants.config.getLong(arenaMode.name() +".Middle.Pitch");
		final Location location = new Location(world, x, y, z, yaw, pitch);
		return location;
	}
	
	private Location[] loadSpawns(int team) {
		final Location[] spawns = new Location[maxPlayer / 2];
		for (int i = 0; i < maxPlayer / 2; i++) {
			final double x = Constants.config.getDouble(arenaMode.name() + ".Team" + team + ".Spawn" + i + ".X");
			final double y = Constants.config.getDouble(arenaMode.name() + ".Team" + team + ".Spawn" + i + ".Y");
			final double z = Constants.config.getDouble(arenaMode.name() + ".Team" + team +  ".Spawn" + i + ".Z");
			final float yaw = Constants.config.getLong(arenaMode.name() + ".Team" + team +  ".Spawn" + i + ".Yaw");
			final float pitch = Constants.config.getLong(arenaMode.name() +".Team" + team +  ".Spawn" + i + ".Pitch");
			final Location location = new Location(world, x, y, z, yaw, pitch);
			spawns[i] = location;
		}
		return spawns;
	}
	
	public World getWorld() {
		return world;
	}

	private void registerArena() {
		MessageSender.getInstace().showDebugMessage("registerArena() call");
		ARENAS.add(this);
		MySQLManager.addArena(this);
		MessageSender.getInstace().showDebugMessage("Arena " + name + " registered and added to DB");
		registered = true;
	}
	
	public void assignPlayersToTeams() {
		List<Player> playerWithoutTeams = new ArrayList<>(players);
		for(Player player : team1.getPlayers()) {
			playerWithoutTeams.remove(player);
		}
		for(Player player : team2.getPlayers()) {
			playerWithoutTeams.remove(player);
		}
		for(int i = 0; i < playerWithoutTeams.size(); i++) {
			for(int a = 0; a < maxPlayer / 2; a++) {
				if(team1.getPlayers()[i] == null) {
					team1.addPlayer(playerWithoutTeams.get(i));
					continue;
				}
				if(team1.getPlayers()[i] == null) {
					team1.addPlayer(playerWithoutTeams.get(i));
					continue;
				}
			}
		}
	}
	
	private void applyGameRules() {
		world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
		world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
		world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
		world.setGameRule(GameRule.DO_FIRE_TICK, false);
		world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
		world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
		world.setGameRule(GameRule.DO_INSOMNIA, false);
		world.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
		world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
		world.setTime(12000);
	}
	
	public boolean addPlayer(Player player) {
		if(players.size() == maxPlayer) {
			return false;
		}
		players.add(player);
		PlayerJoinArenaEvent event = new PlayerJoinArenaEvent(this, player);
		Bukkit.getServer().getPluginManager().callEvent(event);
		return true;
	}
	
	public void removePlayer(Player player) {
		if(players.contains(player)) {
			PlayerQuitArenaEvent event = new PlayerQuitArenaEvent(this, player);
			Bukkit.getServer().getPluginManager().callEvent(event);
			players.remove(player);
			team1.removePlayer(player);
			team2.removePlayer(player);
		}
	}
	
	// Copied the 5th time :/. Dont know author
	private String getRandomString() {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 5;
		Random random = new Random();
		String generatedString = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
		return generatedString;
	}
	
	public void delete() {
		for(Player player : players) {
			CloudNetAdapter.sendPlayerToLobbyTask(player);
		}
		MySQLManager.deleteArena(name);
		ARENAS.remove(this);
		Bukkit.getScheduler().runTaskLater(Constants.plugin, () -> {
			deleteWorld();
		}, 20);
		MessageSender.INSTANCE.showDebugMessage(name + " deleted!");
	}
	
	private void deleteWorld() {
		MultiverseCore mvCore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		MVWorldManager worldManager = mvCore.getMVWorldManager();
		worldManager.deleteWorld(name);
	}
	
	public void teleportToSpawnPoints() {
		for(int i = 0; i < team1.getPlayers().length; i++) {
			Player p = team1.getPlayers()[i];
			if(p == null) {
				continue;
			}
			p.teleport(team1Spawns[i]);
		}
		for(int i = 0; i < team2.getPlayers().length; i++) {
			Player p = team2.getPlayers()[i];
			if(p == null) {
				continue;
			}
			p.teleport(team2Spawns[i]);
		}
	}

	public String getName() {
		return name;
	}

	public GameState getGameState() {
		return gamestate;
	}

	public void setGamestate(GameState gamestate) {
		GameStateChangeEvent event = new GameStateChangeEvent(this, this.gamestate, gamestate);
		Bukkit.getServer().getPluginManager().callEvent(event);
		this.gamestate = gamestate;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public ArenaMode getArenaMode() {
		return arenaMode;
	}

	public static List<Arena> getArenas() {
		return ARENAS;
	}
	
	public int getMaxPlayers() {
		return maxPlayer;
	}

	public Location getMiddle() {
		return middle;
	}
	
	public ChickenLeagueBall getBall() {
		return ball;
	}
	
	public ChickenLeagueTeam getTeam1() {
		return team1;
	}
	
	public ChickenLeagueTeam getTeam2() {
		return team2;
	}

	public boolean isRegistered() {
		return registered;
	}
}
