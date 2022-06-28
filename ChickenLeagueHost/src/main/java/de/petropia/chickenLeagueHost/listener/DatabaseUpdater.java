package de.petropia.chickenLeagueHost.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import de.petropia.chickenLeagueHost.events.GameStateChangeEvent;
import de.petropia.chickenLeagueHost.events.PlayerJoinArenaEvent;
import de.petropia.chickenLeagueHost.events.PlayerQuitArenaEvent;
import de.petropia.chickenLeagueHost.mysql.MySQLManager;

public class DatabaseUpdater implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoinArena(PlayerJoinArenaEvent event) {
		MySQLManager.updateArena(event.getArena());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuitArena(PlayerQuitArenaEvent event) {
		MySQLManager.updateArena(event.getArena());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onGameStateChange(GameStateChangeEvent event) {
		MySQLManager.updateArena(event.getArena());
	}
	
}
