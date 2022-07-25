package de.petropia.chickenLeagueHost.specialItem.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.specialItem.SpecialItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class Rod extends SpecialItem implements Listener {

	private static final ItemStack ITEM = createItemStack();
	
	@Override
	public void activate(Player player) {
		player.getInventory().setItem(8, ITEM);
	}
	
	@EventHandler
	public void onUse(PlayerFishEvent event) {
		if(!event.getPlayer().getInventory().getItemInMainHand().equals(ITEM)) {
			return;
		}
		if(event.getCaught() == null) {
			return;
		}
		event.getPlayer().getInventory().setItem(8, new ItemStack(Material.AIR));
		Bukkit.getScheduler().runTaskLater(Constants.plugin, () -> {
			Vector oldVector = event.getCaught().getVelocity().clone();
			Vector newVector = oldVector.multiply(2.5).add(new Vector(0, 1, 0));
			event.getCaught().setVelocity(newVector);	
		}, 1);
	}
	
	private static ItemStack createItemStack() {
		ItemStack item = new ItemStack(Material.FISHING_ROD);
		item.addUnsafeEnchantment(Enchantment.LUCK, 1);
		item.editMeta(meta -> {
			meta.displayName(Component.text("Angel'o'Mertel").color(TextColor.fromCSSHexString("#34ebc0")).decorate(TextDecoration.BOLD));
			final List<Component> lore = new ArrayList<>();
			lore.add(Component.text(" "));
			lore.add(Component.text("Angel dir das Huhn oder den Spieler").color(NamedTextColor.GRAY));
			lore.add(Component.text(" "));
			meta.lore(lore);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_UNBREAKABLE);
			meta.setUnbreakable(true);
		});
		return item;
	}	
}
