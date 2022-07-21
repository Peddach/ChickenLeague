package de.petropia.chickenLeagueHost.specialItem;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.inventory.ItemStack;

import de.petropia.chickenLeagueHost.arena.Arena;

public class MysteryChestListener implements Listener {
	
	private final ItemStack chest = MysteryChest.getChest();
	
	@EventHandler
	public void onItemPickUp(EntityPickupItemEvent event) {
		if(!event.getItem().getItemStack().equals(chest)){
			return;
		}
		event.setCancelled(true);
		if(event.getEntityType() != EntityType.PLAYER) {
			return;
		}
		Player player = (Player) event.getEntity();
		if(player.getInventory().getItem(8) != null) {
			return;
		}
		Arena arena = null;
		for(Arena i : Arena.getArenas()) {
			if(!i.isPlayerPresent(player)) {
				continue;
			}
			arena = i;
			break;
		}
		if(arena == null) {
			return;
		}
		MysteryChest chest = arena.getSpecialItemManager().getMysteryChestByLocation(player.getLocation());
		if(chest == null) {
			return;
		}
		chest.getItem().activate(player);
		arena.getSpecialItemManager().removeMysteryChest(chest);
	}
	
	@EventHandler
	public void onItemDespawn(ItemDespawnEvent event) {
		if(event.getEntity().getItemStack().equals(chest)) {
			event.setCancelled(true);
		}
	}
}
