package de.petropia.chickenLeagueHost.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.team.ChickenLeagueTeam;

public class PlayerGoalEvent extends Event {
	
	private static final HandlerList HANDLERS = new HandlerList();
	
	private final Arena arena;
	private final Player player;
	private final ChickenLeagueTeam team;
	
	public PlayerGoalEvent(Arena arena, Player player, ChickenLeagueTeam team) {
		this.arena = arena;
		this.team = team;
		this.player = player;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	public Player getPlayer() {
		return player;
	}

	public Arena getArena() {
		return arena;
	}

	public ChickenLeagueTeam getTeam() {
		return team;
	}
	
}
