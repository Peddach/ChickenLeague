package de.petropia.chickenLeagueHost.listener;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

/**
 * Listener to protect the arena from damage and players from damaging theirself
 */
public class ArenaProtectionListener implements Listener {

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player player) {
			event.setDamage(0);
			player.setHealth(20);
		}
		
	}
	
	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent event) {
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
		if(event.getPlayer().getInventory().getHeldItemSlot() == 8) {
			event.getPlayer().getInventory().clear(8);
		}
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
		if(event.getPlayer().getInventory().getHeldItemSlot() == 8 && event.getPlayer().getInventory().getItemInMainHand().getType() == Material.NETHERITE_SHOVEL) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onChickenLayEgg(EntitySpawnEvent event) {
		if(event.getEntity() instanceof Item == false) {
			return;
		}
		if(((Item) event.getEntity()).getItemStack().getType() != Material.EGG) {
			return;
		}
		event.setCancelled(true);
	}
}
