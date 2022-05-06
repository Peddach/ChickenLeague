package de.petropia.chickenLeagueHost.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import de.petropia.chickenLeagueHost.arena.Arena;
import io.papermc.paper.event.player.AsyncChatEvent;

public class ChatListener implements Listener {
	
	@EventHandler
	public void onPlayerChatEvent(AsyncChatEvent event) {
		Player player = event.getPlayer();
		Arena arena = null;
		for (Arena i : Arena.getArenas()) {
			if (i.getPlayers().contains(player)) {
				arena = i;
			}
		}
		if(arena == null) {
			event.setCancelled(true);
		}
		event.viewers().clear();
		event.viewers().addAll(arena.getPlayers());

	}
}
