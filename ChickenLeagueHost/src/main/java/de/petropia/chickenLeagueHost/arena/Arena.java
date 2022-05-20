package de.petropia.chickenLeagueHost.arena;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
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
	
	public Arena(ArenaMode mode) {
		setGamestate(GameState.WAITING);
		this.name = getRandomString();
		this.arenaMode = mode;

		MultiverseCore mvCore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		MVWorldManager worldManager = mvCore.getMVWorldManager();
		if (mode == ArenaMode.FIVE_VS_FIVE) {
			worldManager.cloneWorld("Big_Arena", name);
			maxPlayer = 5+5;
		}
		if (mode == ArenaMode.THREE_VS_THREE) {
			worldManager.cloneWorld("Big_Arena", name);
			maxPlayer = 3+3;
		}
		if (mode == ArenaMode.ONE_VS_ONE) {
			worldManager.cloneWorld("Small_Arena", name);
			maxPlayer = 1+1;
		}
		world = Bukkit.getWorld(name);
		applyGameRules();
		
		team1 = new ChickenLeagueTeam(maxPlayer);
		team2 = new ChickenLeagueTeam(maxPlayer);
		
		registerArena();
	}
	
	private void registerArena() {
		ARENAS.add(this);
		MySQLManager.addArena(this);
		Constants.plugin.getLogger().info("Arena " + name + " registered and added to DB");
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
	}
	
	private void deleteWorld() {
		MultiverseCore mvCore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		MVWorldManager worldManager = mvCore.getMVWorldManager();
		worldManager.deleteWorld(name);
	}
	
	public void teleportToSpawnPoints() {
		
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

}
