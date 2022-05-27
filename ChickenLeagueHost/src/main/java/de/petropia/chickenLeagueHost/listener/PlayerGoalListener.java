package de.petropia.chickenLeagueHost.listener;

import java.time.Duration;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.GoalCountDown;
import de.petropia.chickenLeagueHost.events.PlayerGoalEvent;
import de.petropia.chickenLeagueHost.util.MessageSender;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.Sound.Source;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;

public class PlayerGoalListener implements Listener {
	
	private static final Component SUBTITLE = Component.text(" hat ein Tor geschossen").color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC);
	private static final Times TIMES = Times.times(Duration.ofMillis(500), Duration.ofMillis(1500), Duration.ofMillis(300));
	private static final Sound SOUND = Sound.sound(org.bukkit.Sound.EVENT_RAID_HORN.getKey(), Source.NEUTRAL, 0.9F, 200F);
	
	@EventHandler
	public void onPlayerGoal(PlayerGoalEvent event) {
		final Title title = Title.title(event.getTeam().getName(), event.getPlayer().name().append(SUBTITLE), TIMES);
		MessageSender.INSTANCE.broadcastTitle(event.getArena(), title, SOUND);
		Bukkit.getScheduler().runTaskLater(Constants.plugin, () -> {
			 new GoalCountDown(event.getArena());
		}, 3*20);
	}
	
}