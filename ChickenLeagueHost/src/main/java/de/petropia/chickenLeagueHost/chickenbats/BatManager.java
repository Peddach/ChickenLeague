package de.petropia.chickenLeagueHost.chickenbats;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class BatManager {
	
	private final HashMap<Player, SpeedBuff> playerSpeedBuffs = new HashMap<>();

	public void speedBuffPlayer(Player player, double speed) {
		if(playerSpeedBuffs.get(player) == null) {
			playerSpeedBuffs.put(player, new SpeedBuff(player, speed));
			return;
		}
		SpeedBuff oldSpeedBuff = playerSpeedBuffs.get(player);
		oldSpeedBuff.cancel();
		SpeedBuff newSpeedBuff = new SpeedBuff(player, speed);
		playerSpeedBuffs.put(player, newSpeedBuff);
	}
	
	public void resetAllBuffs() {
		for(Player player : playerSpeedBuffs.keySet()) {
			playerSpeedBuffs.get(player).cancel();
		}
	}
}
