package de.petropia.chickenLeagueHost.specialItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.arena.ArenaMode;

public class SpecialItemManager {
	
	private final static List<SpecialItem> SPECIAL_ITEMS = new ArrayList<>();
	private final List<Location> locations = new ArrayList<>();
	private final HashMap<Location, MysteryChest> chests = new HashMap<>(); 
	private final Runnable spawnNewItem;
	private int taskID = -1;
	private int delay;
	
	/**
	 * Spawns new item every X second on every orange carpet from config
	 * @param arena
	 */
	public SpecialItemManager(Arena arena) {
		for(Object obj  : Constants.config.getList(arena.getArenaMode().name() + ".ItemSpawns")){
			String[] string = ((String) obj).split("/");
			double x = Double.valueOf(string[0]);
			double y = Double.valueOf(string[1]);
			double z = Double.valueOf(string[2]);
			locations.add(new Location(arena.getWorld(), x, y + 0.3, z));
			if(arena.getArenaMode() == ArenaMode.ONE_VS_ONE) {
				delay = 10*20;
			}
			else {
				delay = 5*20;
			}
		}
		spawnNewItem = () -> {
			List<Location> randomLocations = new ArrayList<>(locations);
			Collections.shuffle(randomLocations);
			Location location = null;
			for(Location loc : randomLocations) {
				if(chests.get(loc) != null) {
					continue;
				}
				location = loc;
				break;
			}
			if(location == null) {
				return;
			}
			Random rand = new Random();
		    SpecialItem item = SPECIAL_ITEMS.get(rand.nextInt(SPECIAL_ITEMS.size()));
			MysteryChest chest = new MysteryChest(location, item);
			chests.put(location, chest);
		};
	}
	
	/**
	 * Remove every special item on every orange carpet
	 */
	public void stop(){
		List<MysteryChest> chestList = new ArrayList<>(chests.values()); 
		for(int i = 0; i < chests.values().size(); i++) {
			MysteryChest chest = chestList.get(i);
			if(chest != null) {
			chest.remove();
			}
		}
		chests.clear();
		if(taskID != -1) {
			Bukkit.getScheduler().cancelTask(taskID);
			taskID = -1;
		}
	}
	
	/**
	 * start creating special items on every carpet
	 */
	public void start() {
		if(taskID != -1) {
			return;
		}
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Constants.plugin, spawnNewItem, 10*20, delay);
	}
	
	/**
	 * Register an item as special item and register Bukkit listener
	 * @param item {@link SpecialItem} to register
	 */
	public static void registerItem(SpecialItem item) {
		SPECIAL_ITEMS.add(item);
		Bukkit.getServer().getPluginManager().registerEvents(item, Constants.plugin);
	}
	
	/**
	 * @param location {@link Location} to check
	 * @return MysteryChest on this location or null
	 */
	public MysteryChest getMysteryChestByLocation(Location location) {
		for(Location loc : chests.keySet()) {
			if((int) loc.getX() == (int) location.getX() && (int) loc.getZ() == (int) location.getZ()) {
				return chests.get(loc);
			}
		}
		return null;
	}
	
	/**
	 * Remove a specific MysteryChest
	 */
	public void removeMysteryChest(MysteryChest chest) {
		List<Location> keys = new ArrayList<>(chests.keySet());
		for(Location loc : keys) {
			MysteryChest value = chests.get(loc);
			if(value == chest) {
				chests.remove(loc);
				chest.remove();
			}
		}
	}
	
	public HashMap<Location, MysteryChest> getChests(){
		return chests;
	}
	
	public List<Location> getLocations() {
		return locations;
	}
	
	public static List<SpecialItem> getSpecialItems(){
		return SPECIAL_ITEMS;
	}
}
