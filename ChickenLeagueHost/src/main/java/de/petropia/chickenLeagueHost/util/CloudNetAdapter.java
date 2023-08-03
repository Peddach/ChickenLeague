package de.petropia.chickenLeagueHost.util;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.arena.ArenaMode;
import de.petropia.turtleServer.api.minigame.GameMode;
import de.petropia.turtleServer.api.minigame.GameState;
import de.petropia.turtleServer.api.minigame.MinigameNames;
import org.bukkit.entity.Player;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.wrapper.Wrapper;

public class CloudNetAdapter {
	/**
	 * @return Name of current server instance in CloudNet
	 */
	public static String getServerInstanceName() {
		return Wrapper.getInstance().getCurrentServiceInfoSnapshot().getName();
	}

	/*
	 * split players to all lobby servers
	 */
	public static void sendPlayerToLobbyTask(Player player) {
		IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
		playerManager.getPlayerExecutor(playerManager.getOnlinePlayer(player.getUniqueId())).connectToFallback();
	}

	public static void publishArenaUpdate(Arena arena) {
		Constants.plugin.getCloudNetAdapter().publishArenaUpdate(
				MinigameNames.CHICKEN_LEAGUE.name(),
				arena.getName(),
				convertGameMode(arena.getArenaMode()),
				arena.getMaxPlayers(),
				convertGameState(arena.getGameState()),
				arena.getPlayers()
		);
	}

	public static void publishArenaDelete(Arena arena){
		Constants.plugin.getCloudNetAdapter().publishArenaDelete(arena.getName());
	}

	private static GameMode convertGameMode(ArenaMode mode){
		if(mode == ArenaMode.ONE_VS_ONE){
			return GameMode.SINGLE;
		}
		return GameMode.MULTI;
	}

	private static GameState convertGameState(de.petropia.chickenLeagueHost.arena.GameState state){
		if(state == null){
			return GameState.WAITING;
		}
		return switch (state){
			case WAITING -> GameState.WAITING;
			case STARTING -> GameState.STARTING;
			case INGAME -> GameState.INGAME;
			case ENDING, UNKNOWN -> GameState.ENDING;
		};
	}
}
