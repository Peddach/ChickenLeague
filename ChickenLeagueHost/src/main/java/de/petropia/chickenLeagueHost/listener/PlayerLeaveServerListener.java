package de.petropia.chickenLeagueHost.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.petropia.chickenLeagueHost.arena.Arena;

public class PlayerLeaveServerListener implements Listener{
	
	@EventHandler
	public void onPlayerLeaveEvent(PlayerQuitEvent event) {
		for(Arena arena : Arena.getArenas()) {
			arena.removePlayer(event.getPlayer());
		}
		event.quitMessage(null);
	}
	
}
