package de.petropia.chickenLeagueHost.specialItem;

import java.util.ArrayList;
import java.util.List;

import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.Arena;

/**
 * Listener to active the special item on pickup and speedbuff player on organge carpet
 *
 */
public class MysteryChestListener implements Listener {
	
	private static final List<Player> BOOST_BLACK_LIST = new ArrayList<>();
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
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if(BOOST_BLACK_LIST.contains(player)) {
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
		int x = (int) player.getLocation().getX();
		int z = (int) player.getLocation().getZ();
		for(Location loc : arena.getSpecialItemManager().getLocations()) {
			int locX = (int) loc.getX();
			int locZ = (int) loc.getZ();
			if (locX == x && locZ == z && loc.getWorld() == player.getLocation().getWorld()) {
				arena.getBatManager().speedBuffPlayer(player, 0.25);
				player.playSound(Sound.sound(org.bukkit.Sound.BLOCK_CONDUIT_DEACTIVATE, Sound.Source.NEUTRAL, 0.4F, 1.99F));
				BOOST_BLACK_LIST.add(player);
				Bukkit.getScheduler().runTaskLater(Constants.plugin, () -> BOOST_BLACK_LIST.remove(player), 20);
			}
		}
	}
}
