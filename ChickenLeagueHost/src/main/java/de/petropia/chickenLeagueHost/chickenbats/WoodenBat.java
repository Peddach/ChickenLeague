package de.petropia.chickenLeagueHost.chickenbats;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import de.petropia.chickenLeagueHost.Constants;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class WoodenBat extends ChickenBat {
	
	private static final double SPEED_BUFF = Constants.config.getDouble("Bats.Wood.Speed");
	private final static WoodenBat INSTANCE = new WoodenBat();

	@Override
	public double getSpeedBuff() {
		return SPEED_BUFF;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = createItem(Material.WOODEN_SHOVEL, Component.text("Holzschläger").color(TextColor.fromCSSHexString("#B0601D")), Component.text("Schlage das Huhn um Speed 2 zu bekommen"));
		item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
		return item;
	}

	public static WoodenBat getInstance() {
		return INSTANCE;
	}

}
