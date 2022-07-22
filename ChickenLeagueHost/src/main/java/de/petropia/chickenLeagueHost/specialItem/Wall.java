package de.petropia.chickenLeagueHost.specialItem;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import de.petropia.chickenLeagueHost.Constants;

public class Wall {
	
	private final HashMap<Location, Material> placedBlocks = new HashMap<>();
	
	public Wall(List<Location> blocks) {
		for(Location loc : blocks) {
			if(loc.getBlock().getType() != Material.AIR && loc.getBlock().getType() != Material.ORANGE_CARPET && loc.getBlock().getType() != Material.LIGHT) {
				continue;
			}
			placedBlocks.put(loc, loc.getBlock().getType());
			loc.getBlock().setType(Material.BRICKS);
		}
		Bukkit.getScheduler().runTaskLater(Constants.plugin, () -> {
			for(Location loc : placedBlocks.keySet()) {
				loc.getBlock().setType(placedBlocks.get(loc));
			}
		}, 3*20);
	}
}
