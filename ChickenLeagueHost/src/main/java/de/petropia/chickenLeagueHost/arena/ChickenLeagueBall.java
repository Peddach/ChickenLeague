package de.petropia.chickenLeagueHost.arena;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import de.petropia.chickenLeagueHost.Constants;

public class ChickenLeagueBall {
	
	private static final HashMap<Entity, Arena> CHICKENS = new HashMap<>();
	private Entity chicken;
	private final Arena arena;
	private Player lastHit = null;
	private BukkitTask changeTask;
	
	/**
	 * Object to control behavior of the ball
	 *
	 * @param arena Arena for ball
	 */
	public ChickenLeagueBall(Arena arena) {
		this.arena = arena;
	}
	
	/**
	 * Spawn the ball as chicken in middle of arena
	 */
	public void spawn() {
		if(chicken != null) {
			chicken.teleport(arena.getMiddle());
			lastHit = null;
			return;
		}
		Chicken chicken = arena.getWorld().spawn(arena.getMiddle(), Chicken.class);
		chicken.setSilent(true);
		chicken.setAdult();
		chicken.setGlowing(true);
		chicken.setRemoveWhenFarAway(false);
		chicken.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(999D);
		chicken.setAware(false);
		this.chicken = chicken;
		CHICKENS.put(chicken, arena);
		lastHit = null;
	}
	
	/**
	 * Change the chicken to a different entity for 5 seconds. 
	 * 
	 * @param entityType Type of new entity
	 */
	public void changeEntity(EntityType entityType) {
		if(chicken == null) {
			return;
		}
		if(changeTask != null) {
			changeTask.cancel();
			changeTask = null;
		}
		Location chickenLoc = chicken.getLocation();
		kill();
		chicken = chickenLoc.getWorld().spawnEntity(chickenLoc, entityType);
		if(chicken instanceof LivingEntity livingChicken) {
			livingChicken.setRemoveWhenFarAway(false);
		}
		chicken.setGlowing(true);
		chicken.setSilent(true);
		if(chicken instanceof Attributable attributableChicken) {
			attributableChicken.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(999D);;
		}
		CHICKENS.put(chicken, arena);
		changeTask = Bukkit.getScheduler().runTaskLater(Constants.plugin, () -> {
			Location reverseLocation = chicken.getLocation();
			Vector chickenVelocity = chicken.getVelocity();
			chicken.remove();
			chicken = null;
			CHICKENS.remove(chicken);
			Chicken chicken = arena.getWorld().spawn(reverseLocation, Chicken.class);
			chicken.setSilent(true);
			chicken.setAdult();
			chicken.setGlowing(true);
			chicken.setRemoveWhenFarAway(false);
			chicken.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(999D);
			chicken.setAware(false);
			chicken.setVelocity(chickenVelocity);
			this.chicken = chicken;
			CHICKENS.put(chicken, arena);
			changeTask = null;
		}, 5*20);
	}
	
	/**
	 * Remove the ball and reset it
	 */
	public void kill() {
		if(chicken == null) {
			return;
		}
		if(chicken.isDead()) {
			return;
		}
		if(changeTask != null) {
			changeTask.cancel();
			changeTask = null;
		}
		CHICKENS.remove(chicken);
		chicken.remove();
		chicken = null;
	}
	
	/**
	 * Location of ball
	 *  
	 * @return Location of ball
	 */
	public Location chickenLocation() {
		if(chicken.isDead()) {
			return null;
		}
		return chicken.getLocation();
	}

	public Entity getChicken() {
		return chicken;
	}

	public static HashMap<Entity, Arena> getChickens() {
		return CHICKENS;
	}

	public Player getLastHit() {
		return lastHit;
	}

	public void setLastHit(Player lastHit) {
		this.lastHit = lastHit;
	}
}
