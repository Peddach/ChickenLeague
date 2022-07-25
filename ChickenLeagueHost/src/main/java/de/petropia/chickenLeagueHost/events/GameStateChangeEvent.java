package de.petropia.chickenLeagueHost.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.arena.GameState;

public class GameStateChangeEvent extends Event{
	private static final HandlerList HANDLERS = new HandlerList();
	private final Arena arena;
	private final GameState before;
	private final GameState after;

	/**
	 * Event which fires when the gamestate of a arena changes
	 * 
	 * @param arena Changed Arena
	 * @param before gamestate before change
	 * @param after gamestate after change
	 */
	public GameStateChangeEvent(Arena arena, GameState before, GameState after){
		this.arena = arena;
		this.before = before;
		this.after = after;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	public Arena getArena() {
		return arena;
	}

	public GameState getBefore() {
		return before;
	}

	public GameState getAfter() {
		return after;
	}
}
