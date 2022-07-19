package de.petropia.chickenLeagueHost.chickenbats;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import de.petropia.chickenLeagueHost.Constants;

public class SpeedBuff {
	
	private final Player player;
	private BukkitTask task;
	
	public SpeedBuff(Player player, double speed) {
		this.player = player;
		player.setWalkSpeed((float) speed);
		task = Bukkit.getServer().getScheduler().runTaskLater(Constants.plugin, () -> {
			resetPlayerSpeed();
		}, 3*20);
	}
	
	public void cancel() {
		resetPlayerSpeed();
		task.cancel();
	}
	
	private void resetPlayerSpeed() {
		player.setWalkSpeed(0.2F);
	}
	
}
