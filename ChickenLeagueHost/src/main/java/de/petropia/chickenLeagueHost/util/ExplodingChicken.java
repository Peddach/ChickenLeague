package de.petropia.chickenLeagueHost.util;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.util.Vector;

import com.destroystokyo.paper.ParticleBuilder;

import de.petropia.chickenLeagueHost.Constants;

public class ExplodingChicken {
	
	private final Chicken chicken;
	
	/**
	 * Create chicken with random velocity which explodes after 5 seconds
	 * @param location
	 */
	public ExplodingChicken(Location location) {
		chicken = location.getWorld().spawn(location, Chicken.class);
		chicken.setMemory(MemoryKey.HUNTED_RECENTLY, true);
		chicken.setVelocity(getRandomVector());
		chicken.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(999D);
		chicken.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5D);;
		chicken.setFireTicks(60);
		chicken.setSilent(true);
		Bukkit.getScheduler().runTaskLater(Constants.plugin, () -> {
			chicken.setHealth(0);
			new ParticleBuilder(Particle.EXPLOSION_LARGE)
			.offset(1.5, 1.5, 1.5)
			.count(15)
			.allPlayers()
			.location(chicken.getLocation())
			.spawn();
			chicken.getNearbyEntities(5, 5, 5).forEach(entity -> {
				if(entity instanceof Player player) {
					player.playSound(chicken.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.7F, 1F);
				}
			});
		}, 90);
	}
	
	/**
	 * create a random vector
	 */
	private Vector getRandomVector() {
		return new Vector(getRandomDouble(), 0.7D, getRandomDouble());
	}
	
	private double getRandomDouble() {
		double min = -1.5;
		double max = 1.5;
		Random r = new Random();
		double random = min + r.nextDouble() * (max - min);
		return random;
	}
}
