package de.petropia.chickenLeagueHost.util;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.arena.ArenaMode;
import de.petropia.turtleServer.api.minigame.GameMode;
import de.petropia.turtleServer.api.minigame.GameState;
import de.petropia.turtleServer.api.minigame.MinigameNames;

public class CloudNetAdapter {

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
