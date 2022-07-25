package de.petropia.chickenLeagueHost.specialItem.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.specialItem.SpecialItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ChangeBall extends SpecialItem implements Listener {

	private static final ItemStack ITEM = createItemStack();
	private static final EntityType[] TYPES = {
			EntityType.BAT,
			EntityType.MUSHROOM_COW,
			EntityType.GOAT,
			EntityType.SALMON,
			EntityType.FOX,
			EntityType.VILLAGER,
			EntityType.OCELOT,
			EntityType.RABBIT,
			EntityType.FROG,
			EntityType.TURTLE,
			EntityType.BEE};
	
	@Override
	public void activate(Player player) {
		player.getInventory().setItem(8, ITEM);
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if(!event.getPlayer().getInventory().getItemInMainHand().equals(ITEM)) {
			return;
		}
		event.getPlayer().getInventory().clear(8);
		Arena arena = null;
		for(Arena i : Arena.getArenas()) {
			if(!i.isPlayerPresent(event.getPlayer())) {
				continue;
			}
			arena = i;
			break;
		}
		if(arena == null) {
			return;
		}
		Random random = new Random();
		int randInt = random.nextInt(TYPES.length);
		arena.getBall().changeEntity(TYPES[randInt]);
	}
	
	private static ItemStack createItemStack() {
		ItemStack item = new ItemStack(Material.TOTEM_OF_UNDYING);
		item.addUnsafeEnchantment(Enchantment.LUCK, 1);
		item.editMeta(meta -> {
			meta.displayName(Component.text("Ballverwandeler").color(TextColor.fromCSSHexString("#a185d2")).decorate(TextDecoration.BOLD));
			final List<Component> lore = new ArrayList<>();
			lore.add(Component.text(" "));
			lore.add(Component.text("Verwandele den Ball in ein zufälliges Tier").color(NamedTextColor.GRAY));
			lore.add(Component.text(" "));
			meta.lore(lore);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS);
		});
		return item;
	}

}
