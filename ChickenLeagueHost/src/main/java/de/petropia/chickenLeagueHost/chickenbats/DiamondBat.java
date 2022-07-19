package de.petropia.chickenLeagueHost.chickenbats;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import de.petropia.chickenLeagueHost.Constants;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class DiamondBat extends ChickenBat {

	private static final double SPEED_BUFF = Constants.config.getDouble("Bats.Diamond.Speed");
	private static final DiamondBat DIAMOND_BAT = new DiamondBat();
	
	@Override
	public double getSpeedBuff() {
		return SPEED_BUFF;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = createItem(Material.DIAMOND_SHOVEL, Component.text("Diamantschläger").color(TextColor.fromCSSHexString("#0DECBD")), Component.text("Schlage das Huhn mit Rückstoß 4"));
		item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 4);
		return item;
	}
	
	public static DiamondBat getInstance() {
		return DIAMOND_BAT;
	}

}
