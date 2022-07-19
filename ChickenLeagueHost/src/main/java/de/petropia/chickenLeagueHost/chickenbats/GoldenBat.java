package de.petropia.chickenLeagueHost.chickenbats;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import de.petropia.chickenLeagueHost.Constants;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class GoldenBat extends ChickenBat {

	private static final double SPEED_BUFF = Constants.config.getDouble("Bats.Gold.Speed");
	private static final GoldenBat GOLDEN_BAT = new GoldenBat();
	
	@Override
	public double getSpeedBuff() {
		return SPEED_BUFF;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = createItem(Material.GOLDEN_SHOVEL, Component.text("Goldschläger").color(TextColor.fromCSSHexString("#FACA00")), Component.text("Schlage das Huhn um Speed 1 zu bekommen"));
		item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
		return item;
	}

	public static GoldenBat getInstance() {
		return GOLDEN_BAT;
	}

}
