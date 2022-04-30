package de.petropia.chickenLeagueHost.mysql;

import de.petropia.chickenLeagueHost.arena.ArenaMode;
import de.petropia.chickenLeagueHost.arena.GameState;

public record ArenaRecord(String name, ArenaMode mode, int players, GameState gameState, String server) {

}
