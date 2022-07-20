package de.petropia.chickenLeagueHost.specialItem.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.petropia.chickenLeagueHost.specialItem.SpecialItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class NetheriteBat extends SpecialItem {

	private static final ItemStack ITEM = createItemStack();
	
	@Override
	public void activate(Player player) {
		player.getInventory().setItem(8, ITEM);
	}

	private static ItemStack createItemStack() {
		ItemStack item = new ItemStack(Material.NETHERITE_SHOVEL);
		item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 12);
		item.editMeta(meta -> {
			meta.displayName(Component.text("Superschläger").color(TextColor.fromCSSHexString("#8c0e12")).decorate(TextDecoration.BOLD));
			final List<Component> lore = new ArrayList<>();
			lore.add(Component.text(" "));
			lore.add(Component.text("Schlage das Huhn mit Knockback 12"));
			lore.add(Component.text(" "));
			meta.lore(lore);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS);
		});
		return item;
	}
	
	@EventHandler
	public void onItemUse(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player == false || event.getEntity() instanceof Player) {
			return;
		}
		Player player = (Player) event.getDamager();
		if(player.getInventory().getItem(8) == null) {
			return;
		}
		if(!player.getInventory().getItem(8).equals(ITEM)) {
			return;
		}
		player.getInventory().setItem(8, new ItemStack(Material.AIR));
		Vector oldVector = player.getLocation().getDirection().clone();
		Vector multiplay = new Vector(-1, 1, -1);
		Vector add = new Vector(0, 0.4, 0);
		Vector newVector = oldVector.multiply(multiplay).add(add).multiply(7);
		player.setVelocity(newVector);
	}
}
