package de.petropia.chickenLeagueHost.specialItem;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import de.petropia.chickenLeagueHost.Constants;

public class Wall {

	private final HashMap<Location, Material> placedBlocks = new HashMap<>();
	private final List<Location> blocks;

	public Wall(List<Location> blocks) {
		this.blocks = blocks;
		Random rand = new Random();
		int randint = rand.nextInt(4);
		if (randint == 0) {
			placeWall(Material.SCULK, 4);
		}
		if (randint == 1) {
			placeWall(Material.BRICKS, 4);
		}
		if (randint == 2) {
			placeWall(Material.COBWEB, 3);
		}
		if (randint >= 3) {
			placeWall(Material.HONEY_BLOCK, 3);
		}
	}

	private void placeWall(Material material, int seconds) {
		for(Location loc : blocks) {
			if(loc.getBlock().getType() != Material.AIR && loc.getBlock().getType() != Material.ORANGE_CARPET && loc.getBlock().getType() != Material.LIGHT) {
				continue;
			}
			placedBlocks.put(loc, loc.getBlock().getType());
			loc.getBlock().setType(material);
		}
		Bukkit.getScheduler().runTaskLater(Constants.plugin, () -> {
			for(Location loc : placedBlocks.keySet()) {
				loc.getBlock().setType(placedBlocks.get(loc));
			}
		}, seconds*20);
	}

}
