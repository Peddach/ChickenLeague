package de.petropia.chickenLeagueHost.arena;

import java.time.Duration;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.util.MessageUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.Sound.Source;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;

public class GameCountDown {

	private int time;
	private final BukkitTask task;
	private final Arena arena;
	
	/**
	 * First Coundown before game starts. Afterward create a GoalCountdown which teleports players to spawnpoints
	 * 
	 * @param count How long the countdown should be
	 * @param arena Arena to start
	 * @param force if false, arena must be full to start
	 */
	public GameCountDown(int count, Arena arena, boolean force) {
		this.time = count;
		this.arena = arena;
		task = Bukkit.getScheduler().runTaskTimer(Constants.plugin, () -> {
			if(arena.getPlayers().size() != arena.getMaxPlayers() && !force) {
				cancel(true);
				return;
			}
			arena.getPlayers().forEach(p -> p.setLevel(time));
			showTitle();
			if(time <= 0) {
				cancel(false);
				arena.setGamestate(GameState.INGAME);
				arena.assignPlayersToTeams();
				arena.teleportToSpawnPoints();
				return;
			}
			time--;
		}, 20, 20);
	}
	
	/**
	 * Cancel the countdown
	 * 
	 * @param showTitle true, players will see that countdown was canceled.
	 */
	public void cancel(boolean showTitle) {
		task.cancel();
		if(!showTitle) {
			return;
		}
		final Component title = Component.text("Start").color(NamedTextColor.GRAY);
		final Component subtitle = Component.text("abgebrochen").color(NamedTextColor.RED).decorate(TextDecoration.BOLD);
		final Times times = Times.times(Duration.ofMillis(300), Duration.ofMillis(1000), Duration.ofMillis(500));
		final Sound sound = Sound.sound(Key.key("entity.villager.no"), Source.NEUTRAL, 1, 0.9F);
		MessageUtil.INSTANCE.broadcastTitle(arena, Title.title(title, subtitle, times), sound);
	}

	/**
	 * Show a title and play a sound based on current time
	 */
	private void showTitle() {
		final Component subtitle = Component.text("Sekunden").color(NamedTextColor.GRAY);
		final Component title = Component.text(time).color(NamedTextColor.GOLD);
		final Times times = Times.times(Duration.ofMillis(50), Duration.ofMillis(850), Duration.ofMillis(100));
		if (time == 10 || time == 5) {
			MessageUtil.INSTANCE.broadcastTitle(arena, Title.title(title, subtitle, times), sound(1));
		}
		if (time == 4) {
			MessageUtil.INSTANCE.broadcastTitle(arena, Title.title(title, subtitle, times), sound(0.9F));
		}
		if (time == 3) {
			MessageUtil.INSTANCE.broadcastTitle(arena, Title.title(title, subtitle, times), sound(0.8F));
		}
		if (time == 2) {
			MessageUtil.INSTANCE.broadcastTitle(arena, Title.title(title, subtitle, times), sound(0.7F));
		}
		if (time == 1) {
			final Component subtitleSingular = Component.text("Sekunde").color(NamedTextColor.GRAY);
			MessageUtil.INSTANCE.broadcastTitle(arena, Title.title(title, subtitleSingular, times), sound(0.6F));
		}
	}
	
	/**
	 * Create cowbell sound
	 * 
	 * @param pitch Pitch of cowbell
	 * @return Sound
	 */
	private Sound sound(float pitch) {
		return Sound.sound(Key.key("block.note_block.cow_bell"), Source.NEUTRAL, 1, pitch);
	}
}
