package de.petropia.chickenLeagueHost.util;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class InventoryUtil {
	
	public static void clearPlayer(Player player) {
		player.getInventory().clear();
		player.getActivePotionEffects().clear();
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setFireTicks(0);
		player.setExp(0);
		player.setLevel(0);
		player.setVelocity(new Vector(0, 0, 0));
	}
}
