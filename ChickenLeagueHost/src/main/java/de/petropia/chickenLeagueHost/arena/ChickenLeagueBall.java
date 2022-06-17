package de.petropia.chickenLeagueHost.arena;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Chicken;

public class ChickenLeagueBall {
	
	private static final ArrayList<Chicken> CHICKENS = new ArrayList<>();
	private Chicken chicken;
	private final Arena arena;
	
	public ChickenLeagueBall(Arena arena) {
		this.arena = arena;
	}
	
	public void spawn() {
		if(chicken != null) {
			chicken.teleport(arena.getMiddle());
			return;
		}
		chicken = arena.getWorld().spawn(arena.getMiddle(), Chicken.class);
		chicken.setSilent(true);
		chicken.setAdult();
		chicken.setGlowing(true);
		chicken.setRemoveWhenFarAway(false);
		chicken.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(999D);
		chicken.setAI(false);
		CHICKENS.add(chicken);
	}
	
	public void kill() {
		if(chicken == null) {
			return;
		}
		if(chicken.isDead()) {
			return;
		}
		CHICKENS.remove(chicken);
		chicken.setHealth(0);
		chicken = null;
	}
	
	public Location chickenLocation() {
		if(chicken.isDead()) {
			return null;
		}
		return chicken.getLocation();
	}

	public Chicken getChicken() {
		return chicken;
	}

	public static ArrayList<Chicken> getChickens() {
		return CHICKENS;
	}
}
