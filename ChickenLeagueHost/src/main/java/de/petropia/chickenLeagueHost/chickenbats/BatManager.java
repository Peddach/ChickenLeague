package de.petropia.chickenLeagueHost.chickenbats;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class BatManager {
	
	private final HashMap<Player, SpeedBuff> playerSpeedBuffs = new HashMap<>();

	/**
	 * Give a player a speedbuff for 3 seconds
	 * 
	 * @param player Player to speedbuff
	 * @param speed Speed as value from -1 to 1
	 */
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
	
	/**
	 * reset all speedbuffs from every player
	 */
	public void resetAllBuffs() {
		for(Player player : playerSpeedBuffs.keySet()) {
			playerSpeedBuffs.get(player).cancel();
		}
	}
}
