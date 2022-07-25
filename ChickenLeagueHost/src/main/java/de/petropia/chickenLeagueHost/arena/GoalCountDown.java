package de.petropia.chickenLeagueHost.arena;

import java.time.Duration;

import org.bukkit.Bukkit;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.listener.PlayerMoveListener;
import de.petropia.chickenLeagueHost.util.MessageSender;
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
	
	public GoalCountDown(Arena arena) {
		this.arena = arena;
		countdown = 5;	//Const
		arena.getPlayers().forEach(PlayerMoveListener :: add);
		arena.teleportToSpawnPoints();
		arena.getBall().kill();
		arena.teleportToSpawnPoints();
		taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(Constants.plugin, () -> {
			if(arena.getGameState() != GameState.INGAME) {
				cancel();
			}
			MessageSender.INSTANCE.broadcastTitle(arena, currentTitle(), currentSound());
			if(countdown == 0) {
				cancel();
				arena.getBall().spawn();
				arena.getSpecialItemManager().start();
			}
			countdown --;
		}, 0, 20);
	}
	
	private void cancel() {
		Bukkit.getScheduler().cancelTask(taskid);
		arena.getPlayers().forEach(PlayerMoveListener :: remove);
	}
	
	private Title currentTitle() {
		return Title.title(Component.text(countdown).color(NamedTextColor.GRAY), Component.text("Sekunden").color(NamedTextColor.GRAY), TIMES);
	}
	
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
