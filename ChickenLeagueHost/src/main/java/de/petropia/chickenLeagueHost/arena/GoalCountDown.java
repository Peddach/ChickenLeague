package de.petropia.chickenLeagueHost.arena;

import java.time.Duration;

import org.bukkit.Bukkit;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.listener.PlayerMoveListener;
import de.petropia.chickenLeagueHost.util.MessageUtil;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.Sound.Source;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;

public class GoalCountDown {
	
	private int countdown;
	private final Arena arena;
	private final int taskid;
	private static final Times TIMES = Times.times(Duration.ofMillis(100), Duration.ofMillis(850), Duration.ofMillis(50));
	
	/**
	 * Countdown after every goal. Teleports players back to spawn points
	 * 
	 * @param arena Arena
	 */
	public GoalCountDown(Arena arena) {
		this.arena = arena;
		countdown = 5;	//Const
		arena.getPlayers().forEach(PlayerMoveListener :: add); //Dont lets players move away
		arena.teleportToSpawnPoints();
		arena.getBall().kill();
		arena.teleportToSpawnPoints();
		taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(Constants.plugin, () -> {
			if(arena.getGameState() != GameState.INGAME) {
				cancel(); //Cancel if time is exceeded
			}
			MessageUtil.INSTANCE.broadcastTitle(arena, currentTitle(), currentSound());
			if(countdown == 0) {
				cancel(); //Cancel if countdown is at 0
				arena.getBall().spawn();
				arena.getSpecialItemManager().start(); //start spawning special items
			}
			countdown --;
		}, 0, 20);
	}
	
	/**
	 * cancel the countdown and let players move again
	 */
	private void cancel() {
		Bukkit.getScheduler().cancelTask(taskid);
		arena.getPlayers().forEach(PlayerMoveListener :: remove);
	}
	
	/**
	 * return a title to display to players based on time
	 * @return
	 */
	private Title currentTitle() {
		return Title.title(Component.text(countdown).color(NamedTextColor.GRAY), Component.text("Sekunden").color(NamedTextColor.GRAY), TIMES);
	}
	
	/**
	 * @return Pitched Sound based on current time
	 */
	private Sound currentSound() {
		Sound sound = null;
		if(countdown == 5) {
			sound = Sound.sound(org.bukkit.Sound.BLOCK_NOTE_BLOCK_COW_BELL.getKey(), Source.NEUTRAL, 1, 1F);
		}
		if(countdown == 4) {
			sound = Sound.sound(org.bukkit.Sound.BLOCK_NOTE_BLOCK_COW_BELL.getKey(), Source.NEUTRAL, 1, 1F);
		}
		if(countdown == 3) {
			sound = Sound.sound(org.bukkit.Sound.BLOCK_NOTE_BLOCK_COW_BELL.getKey(), Source.NEUTRAL, 1, 1F);
		}
		if(countdown == 2) {
			sound = Sound.sound(org.bukkit.Sound.BLOCK_NOTE_BLOCK_COW_BELL.getKey(), Source.NEUTRAL, 1, 1F);
		}
		if(countdown == 1) {
			sound = Sound.sound(org.bukkit.Sound.BLOCK_NOTE_BLOCK_COW_BELL.getKey(), Source.NEUTRAL, 1, 1F);
		}
		if(countdown == 0) {
			sound = Sound.sound(org.bukkit.Sound.ENTITY_PLAYER_LEVELUP.getKey(), Source.NEUTRAL, 1, 1F);
		}
		return sound;
	}
}
