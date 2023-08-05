package de.petropia.chickenLeagueHost.items;

import java.util.ArrayList;
import java.util.List;

import de.petropia.chickenLeagueHost.Constants;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;

import de.petropia.chickenLeagueHost.arena.GameState;
import de.petropia.chickenLeagueHost.events.PlayerJoinArenaEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class LeaveItem implements Listener{
	
	private static final ItemStack ITEM = createLeaveItem();
	
	/**
	 * @return Item to leave the game and get back to lobby
	 */
	private static ItemStack createLeaveItem() {
		ItemStack item = new ItemStack(Material.FIREWORK_STAR, 1);
		item.editMeta(meta -> {
			meta.displayName(Component.text("Verlassen").color(NamedTextColor.RED));
			List<Component> lore = new ArrayList<>();
			lore.add(Component.text(" "));
			lore.add(Component.text("Klicke zum verlassen").color(NamedTextColor.RED));
			lore.add(Component.text(" "));
			meta.lore(lore);
		});
		FireworkEffectMeta firemeta = (FireworkEffectMeta) item.getItemMeta();
		firemeta.setEffect(FireworkEffect.builder().withColor(Color.RED).build());
		item.setItemMeta(firemeta);
		item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE);
		return item;
	}
	
	/**
	 * Gives the leave item a player
	 * 
	 * @param player Player
	 */
	public static void giveItemPlayer(Player player) {
		player.getInventory().setItem(8, ITEM);
	}

	//Listen if player clicks the item
	@EventHandler
	public void onPlayerClickLeaveItemEvent(PlayerInteractEvent event) {
		if(event.getItem() == null) {
			return;
		}
		if(!event.getItem().equals(ITEM)) {
			return;
		}
		Constants.plugin.getCloudNetAdapter().sendPlayerToLobby(event.getPlayer());
	}
	
	//listen if player clicks the item in inventory
	@EventHandler
	public void onPlayerClickLeaveItem(InventoryClickEvent event) {
		if(event.getCurrentItem() == null) {
			return;
		}
		if(!event.getCurrentItem().equals(ITEM)) {
			return;
		}
		Constants.plugin.getCloudNetAdapter().sendPlayerToLobby((Player) event.getWhoClicked());
	}
	
	//Gives the item players who joins
	@EventHandler
	public void onPlayerJoinArenaEvent(PlayerJoinArenaEvent event) {
		if(event.getArena().getGameState() == GameState.WAITING || event.getArena().getGameState() == GameState.STARTING) {
			event.getPlayer().getInventory().setItem(8, ITEM);
		}
	}
}
