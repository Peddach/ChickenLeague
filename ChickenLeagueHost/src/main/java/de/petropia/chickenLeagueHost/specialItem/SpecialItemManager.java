package de.petropia.chickenLeagueHost.specialItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.Arena;

public class SpecialItemManager {
	
	private final static List<SpecialItem> SPECIAL_ITEMS = new ArrayList<>();
	private final List<MysteryChest> chests = new ArrayList<>();
	private final List<Location> allLocations = new ArrayList<>();
	private List<Location> freeLocations;
	private final Runnable spawnNewItem;
	private int taskID = -1;
	
	public SpecialItemManager(Arena arena) {
		for(Object obj  : Constants.config.getList(arena.getArenaMode().name() + ".ItemSpawns")){
			String[] string = ((String) obj).split("/");
			double x = Double.valueOf(string[0]);
			double y = Double.valueOf(string[1]);
			double z = Double.valueOf(string[2]);
			allLocations.add(new Location(arena.getWorld(), x, y + 0.3, z));
		}
		freeLocations = new ArrayList<>(allLocations);
		spawnNewItem = () -> {
			if(freeLocations.size() == 0) {
				return;
			}
			Collections.shuffle(freeLocations);
			Location location = freeLocations.get(0);
			ArrayList<SpecialItem> specialItems = new ArrayList<>(SPECIAL_ITEMS);
			Collections.shuffle(specialItems);
			SpecialItem item = specialItems.get(0);
			MysteryChest chest = new MysteryChest(location, item, arena);
			chests.add(chest);
			freeLocations.remove(location);
		};
	}
	
	public void stop(){
		for(MysteryChest chest : chests) {
			chest.remove();
		}
		freeLocations = new ArrayList<>(allLocations);
		if(taskID != -1) {
			Bukkit.getScheduler().cancelTask(taskID);
			taskID = -1;
		}
	}
	
	public void start() {
		if(taskID != -1) {
			return;
		}
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Constants.plugin, spawnNewItem, 5*20, 15*20);
	}
	
	public static void registerItem(SpecialItem item) {
		SPECIAL_ITEMS.add(item);
		Bukkit.getServer().getPluginManager().registerEvents(item, Constants.plugin);
	}
	
	public List<Location> getFreeLocations(){
		return freeLocations;
	}
}
