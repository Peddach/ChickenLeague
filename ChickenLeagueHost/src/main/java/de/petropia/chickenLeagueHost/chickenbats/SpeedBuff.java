package de.petropia.chickenLeagueHost.chickenbats;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import de.petropia.chickenLeagueHost.Constants;

public class SpeedBuff {
	
	private final Player player;
	private BukkitTask task;
	
	/**
	 * Every instance gives a player for 3 second the speed specified as double
	 * @see {@link BatManager#speedBuffPlayer(Player, double)}
	 * @param player Player to speedbuff
	 * @param speed Speed as double from -1 to 1
	 */
	public SpeedBuff(Player player, double speed) {
		this.player = player;
		player.setWalkSpeed((float) speed);
		task = Bukkit.getServer().getScheduler().runTaskLater(Constants.plugin, () -> {
			resetPlayerSpeed();
		}, 3*20);
	}
	
	/**
	 * Cancel Speedbuff and reset to default speed
	 */
	public void cancel() {
		resetPlayerSpeed();
		task.cancel();
	}
	
	/**
	 * Set players default speed to mojang default (0.2)
	 */
	private void resetPlayerSpeed() {
		player.setWalkSpeed(0.2F);
	}
	
}
