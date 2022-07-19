package de.petropia.chickenLeagueHost.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ArenaProtectionListener implements Listener {
	
	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerPunshPlayer(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}
}
