package de.petropia.chickenLeagueHost.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.arena.GameState;
import de.petropia.chickenLeagueHost.events.PlayerJoinArenaEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class TeamSelectItem implements Listener {
	private final static ItemStack BED = createBedItem();
	
	/**
	 * @return Instance of the item to open the gui
	 */
	private static ItemStack createBedItem() {
		ItemStack item = new ItemStack(Material.GREEN_BED);
		item.editMeta(meta -> {
			meta.displayName(Component.text("Wähle dein Team").color(NamedTextColor.GOLD));
			List<Component> lore = new ArrayList<>();
			lore.add(Component.text(" "));
			lore.add(Component.text("Wähle dein Team").color(NamedTextColor.GRAY));
			lore.add(Component.text(" "));
			meta.lore(lore);
		});
		return item;
	}

	//Listen if player clicks item
	@EventHandler
	public void onPlayerClickEvent(InventoryClickEvent event) {
		if(event.getCurrentItem() == null) {
			return;
		}
		if(!event.getCurrentItem().equals(BED)) {
			return;
		}
		Player player = (Player) event.getWhoClicked();
		for(Arena arena : Arena.getArenas()) {
			if(!arena.isPlayerPresent(player)) {
				continue;
			}
			arena.getTeamSelectGui().openForPlayer(player);
			arena.getTeamSelectGui().updateInv(arena);
			return;
		}
	}
	
	//Listen if player clicks item
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if(event.getItem() == null) {
			return;
		}
		if(!event.getItem().equals(BED)) {
			return;
		}
		Player player = event.getPlayer();
		for(Arena arena : Arena.getArenas()) {
			if(!arena.isPlayerPresent(player)) {
				continue;
			}
			arena.getTeamSelectGui().openForPlayer(player);
			arena.getTeamSelectGui().updateInv(arena);
			return;
		}
	}
	
	//Give player item on join
	@EventHandler
	public void onPlayerJoinArena(PlayerJoinArenaEvent event){
		if(!(event.getArena().getGameState() == GameState.WAITING || event.getArena().getGameState() == GameState.STARTING)) {
			return;
		}
		event.getPlayer().getInventory().setItem(0, BED);
	}
	
	public static ItemStack getBedItem() {
		return BED;
	}
}
