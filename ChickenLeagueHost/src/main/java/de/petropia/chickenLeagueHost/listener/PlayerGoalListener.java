package de.petropia.chickenLeagueHost.listener;

import java.time.Duration;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;

import com.destroystokyo.paper.ParticleBuilder;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.arena.GoalCountDown;
import de.petropia.chickenLeagueHost.events.PlayerGoalEvent;
import de.petropia.chickenLeagueHost.team.ChickenLeagueTeam;
import de.petropia.chickenLeagueHost.util.ExplodingChicken;
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
	private static final Sound SOUND = Sound.sound(org.bukkit.Sound.EVENT_RAID_HORN.getKey(), Source.NEUTRAL, 200F, 1.2F);
	private static final Sound EXPLOSION = Sound.sound(org.bukkit.Sound.ENTITY_GENERIC_EXPLODE.getKey(), Source.NEUTRAL, 2F, 1F);
	private Component name = Component.text("-").color(NamedTextColor.GRAY);
	private int fireworkTaskID;
	private int endFireWorkID;
	@EventHandler
	public void onPlayerGoal(PlayerGoalEvent event) {
		Location chickenLocataion = event.getArena().getBall().getChicken().getLocation();
		event.getArena().getBall().kill();
		if(event.getPlayer() != null) {
			name = event.getPlayer().name();
		}
		final Title title = Title.title(event.getTeam().getName(), name.append(SUBTITLE), TIMES);
		MessageSender.INSTANCE.broadcastTitle(event.getArena(), title, SOUND);
		event.getArena().getBatManager().resetAllBuffs();
		if(event.getTeam().getScore() == 5) {
			endFireWorkID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Constants.plugin, new Runnable() {
				int i = 0;
				@Override
				public void run() {
					i++;
					if(i == 8) {
						Bukkit.getScheduler().cancelTask(endFireWorkID);
					}
					spwanFireWork(event.getArena(), event.getTeam());
					for(int a = 0; a < 5; a++) {
						new ExplodingChicken(event.getArena().getMiddle());
					}
				}
			}, 110, 30);
			event.getArena().setWinner(event.getTeam());
			showEffects(event.getArena(), event.getTeam(), chickenLocataion);
			return;
		}
		Bukkit.getScheduler().runTaskLater(Constants.plugin, () -> {
			event.getArena().getSpecialItemManager().stop();
			new GoalCountDown(event.getArena());
		}, 3*20);
		showEffects(event.getArena(), event.getTeam(), chickenLocataion);
	}
	
	private void showEffects(Arena arena, ChickenLeagueTeam team, Location location) {
		Bukkit.getScheduler().runTaskLater(Constants.plugin, () -> {
			new ParticleBuilder(Particle.EXPLOSION_HUGE)
					.allPlayers()
					.count(20)
					.location(location)
					.offset(4, 4, 4)
					.spawn();
			arena.getPlayers().forEach(p -> p.playSound(EXPLOSION));
		}, 10);
		fireworkTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Constants.plugin, new Runnable(){
			int i = 0;
			@Override
			public void run() {
				i++;
				spwanFireWork(arena, team);
				if (i >= 3) {
					Bukkit.getScheduler().cancelTask(fireworkTaskID);
				}
			}

		}, 10, 30);
	}
	
	private void spwanFireWork(Arena arena, ChickenLeagueTeam team) {
		Color teamColor = Color.WHITE;
		if(team == arena.getTeam1()) {
			teamColor = Color.BLUE;
		}
		if(team == arena.getTeam2()) {
			teamColor = Color.RED;
		}
		FireworkEffect effect = FireworkEffect
				.builder()
				.flicker(true)
				.trail(true)
				.with(Type.BALL_LARGE)
				.withColor(teamColor, Color.GREEN)
				.withFade(Color.WHITE)
				.build();
		Firework firework = arena.getWorld().spawn(arena.getMiddle(), Firework.class);
		FireworkMeta meta = firework.getFireworkMeta();
		meta.addEffect(effect);
		meta.setPower(1);
		firework.setFireworkMeta(meta);
	}
	
}
