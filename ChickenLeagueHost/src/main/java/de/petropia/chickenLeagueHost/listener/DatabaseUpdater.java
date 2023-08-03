package de.petropia.chickenLeagueHost.listener;

import de.petropia.chickenLeagueHost.util.CloudNetAdapter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import de.petropia.chickenLeagueHost.events.GameStateChangeEvent;
import de.petropia.chickenLeagueHost.events.PlayerJoinArenaEvent;
import de.petropia.chickenLeagueHost.events.PlayerQuitArenaEvent;

/**
 * Listener to update database when arena changes
 */
public class DatabaseUpdater implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoinArena(PlayerJoinArenaEvent event) {
		CloudNetAdapter.publishArenaUpdate(event.getArena());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuitArena(PlayerQuitArenaEvent event) {
		CloudNetAdapter.publishArenaUpdate(event.getArena());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onGameStateChange(GameStateChangeEvent event) {
		if(event.getArena().isRegistered()){
			CloudNetAdapter.publishArenaUpdate(event.getArena());
		}
	}
	
}
