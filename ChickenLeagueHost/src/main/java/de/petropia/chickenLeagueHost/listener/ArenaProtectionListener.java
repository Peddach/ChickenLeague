package de.petropia.chickenLeagueHost.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

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
	
	@EventHandler
	public void onPlayerSwapItem(PlayerSwapHandItemsEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerItemDrag(InventoryDragEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerItemClick(InventoryClickEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerUseEvent(PlayerInteractEvent event) {
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		if(event.getPlayer().getInventory().getHeldItemSlot() <= 3){
			event.setCancelled(true);
		}
		if(event.getPlayer().getInventory().getHeldItemSlot() == 8 && event.getPlayer().getInventory().getItemInMainHand().getType() == Material.NETHERITE_SWORD) {
			event.setCancelled(true);
		}
	}
}
