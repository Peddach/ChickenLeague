package de.petropia.chickenLeagueHost.listener;

import java.time.Duration;

import de.petropia.turtleServer.server.TurtleServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.destroystokyo.paper.ParticleBuilder;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.arena.GoalCountDown;
import de.petropia.chickenLeagueHost.events.PlayerGoalEvent;
import de.petropia.chickenLeagueHost.firework.GoalFireWork;
import de.petropia.chickenLeagueHost.util.MessageUtil;
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
	
	@EventHandler
	public void onPlayerGoal(PlayerGoalEvent event) {
		//stats
		TurtleServer.getMongoDBHandler().getPetropiaPlayerByUUID(event.getPlayer().getUniqueId().toString()).thenAccept(player -> {
			player.increateStats("ChickenLeague_Goals",1);
			player.increateStats("ChickenLeague_Points", 1);
		});
		Location chickenLocataion = event.getArena().getBall().getChicken().getLocation();
		event.getArena().getBall().kill();
		if(event.getPlayer() != null) {	//Prepare Title
			name = event.getPlayer().name();
		}
		final Title title = Title.title(event.getTeam().getName(), name.append(SUBTITLE), TIMES);	//send title
		MessageUtil.INSTANCE.broadcastTitle(event.getArena(), title, SOUND);
		event.getArena().getBatManager().resetAllBuffs();
		if(event.getTeam().getScore() >= 5) {	//Win managment
			event.getArena().setWinner(event.getTeam());
			showExplosion(event.getArena(), chickenLocataion);
			for(Player p : event.getTeam().getPlayers()){
				if(p == null){
					continue;
				}
				TurtleServer.getMongoDBHandler().getPetropiaPlayerByOnlinePlayer(p).thenAccept(petropiaPlayer -> {
					petropiaPlayer.increateStats("ChickenLeague_Points", 3);
					petropiaPlayer.increateStats("ChickenLeague_Wins", 1);
				});
			}
			return;
		}
		Bukkit.getScheduler().runTaskLater(Constants.plugin, () -> {
			new GoalCountDown(event.getArena());
			event.getArena().getSpecialItemManager().stop();
		}, 4*20);
		showExplosion(event.getArena(), chickenLocataion);
		new GoalFireWork(event.getArena(), event.getTeam(), false);
	}
	
	private void showExplosion(Arena arena, Location location) {
		Bukkit.getScheduler().runTaskLater(Constants.plugin, () -> {
			new ParticleBuilder(Particle.EXPLOSION_HUGE)
					.allPlayers()
					.count(20)
					.location(location)
					.offset(4, 4, 4)
					.spawn();
			arena.getPlayers().forEach(p -> p.playSound(EXPLOSION));
		}, 10);
	}	
}
