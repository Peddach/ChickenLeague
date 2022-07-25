package de.petropia.chickenLeagueHost.chickenbats;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

/**
 * Parent class of all bats
 */
public abstract class ChickenBat {
	
	/**
	 * @return Speedbuff a players get when hitting chicken
	 */
	public abstract double getSpeedBuff();
	public abstract ItemStack getItem();
	
	/**
	 * Method to create standardized bat items
	 * 
	 * @param material Material of bat
	 * @param name DisplayName of the bat
	 * @param description Description of bat
	 * @return ItemStack for bat
	 */
	protected ItemStack createItem(Material material, Component name, Component description) {
		ItemStack item = new ItemStack(material);
		item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
		item.editMeta(meta -> {
			meta.setUnbreakable(true);
			final List<Component> lore = new ArrayList<>();
			Component empty = Component.text(" ");
			lore.add(empty);
			lore.add(description.color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
			lore.add(empty);
			meta.lore(lore);
			meta.displayName(name);
		});
		return item;
	}
}
