package de.petropia.chickenLeagueHost.firework;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.team.ChickenLeagueTeam;
import de.petropia.chickenLeagueHost.util.ExplodingChicken;
import de.petropia.chickenLeagueHost.util.MessageSender;

public class GoalFireWork {

	private final Arena arena;
	private final ChickenLeagueTeam goalTeam;
	private int fireworkTaskID;
	private int endFireWorkID;

	public GoalFireWork(Arena arena, ChickenLeagueTeam goalTeam, boolean endFireWork) {
		this.arena = arena;
		this.goalTeam = goalTeam;
		showFirework();
		if(endFireWork) {
			showEndFireWork();
		}
	}

	private void showEndFireWork() {
		endFireWorkID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Constants.plugin, new Runnable() {
			int i = 0;

			@Override
			public void run() {
				i++;
				if (i == 7) {
					Bukkit.getScheduler().cancelTask(endFireWorkID);
				}
				createFireWork();
				for (int a = 0; a < 5; a++) {
					new ExplodingChicken(arena.getMiddle());
				}
			}
		}, 110, 30);
		return;
	}

	private void showFirework() {
		fireworkTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Constants.plugin, new Runnable() {
			int i = 0;

			@Override
			public void run() {
				i++;
				createFireWork();
				if (i >= 3) {
					Bukkit.getScheduler().cancelTask(fireworkTaskID);
				}
			}

		}, 10, 30);
	}

	private void createFireWork() {
		Color teamColor = Color.WHITE;
		if(goalTeam != null) {
			if (goalTeam == arena.getTeam1()) {
				teamColor = Color.BLUE;
			}
			if (goalTeam == arena.getTeam2()) {
				teamColor = Color.RED;
			}
		}
		FireworkEffect effect = FireworkEffect.builder().flicker(true).trail(true).with(Type.BALL_LARGE).withColor(teamColor, Color.GREEN).withFade(Color.WHITE).build();
		Firework firework = arena.getWorld().spawn(arena.getMiddle(), Firework.class);
		FireworkMeta meta = firework.getFireworkMeta();
		meta.addEffect(effect);
		meta.setPower(1);
		firework.setFireworkMeta(meta);
	}
}
