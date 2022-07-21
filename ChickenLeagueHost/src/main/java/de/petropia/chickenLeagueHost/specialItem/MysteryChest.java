package de.petropia.chickenLeagueHost.specialItem;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.destroystokyo.paper.ParticleBuilder;

import de.petropia.chickenLeagueHost.Constants;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class MysteryChest{
	
	private static final ItemStack CHEST = createChest();
	private final SpecialItem item;
	private final Item itemEntity;
	private final Location location;
	private int taskID;
	
	public MysteryChest(Location location, SpecialItem item) {
		this.item = item;
		this.location = location;
		Location spawnLoc = new Location(location.getWorld(), location.getX(), location.getY() + 0.1, location.getZ());
		itemEntity = spawnLoc.getWorld().spawn(location, Item.class);
		itemEntity.setItemStack(CHEST);
		itemEntity.customName(Component.text("Special Item").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
		itemEntity.setCustomNameVisible(true);
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Constants.plugin, () -> {
			showParticle();
		}, 1, 5);
		Bukkit.getScheduler().runTaskLater(Constants.plugin, () -> {
			itemEntity.setVelocity(new Vector(0, 0, 0));
			itemEntity.teleport(spawnLoc);
		}, 1);
	}
	
	public void showParticle() {
		ParticleBuilder builder = new ParticleBuilder(Particle.REDSTONE)
				.count(1)
				.color(Color.LIME)
				.allPlayers()
				.force(true);
		int size = 1;
	    for (int d = 0; d <= 90; d += 1) {
	        Location particleLoc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
	        particleLoc.setX(location.getX() + Math.cos(d) * size);
	        particleLoc.setZ(location.getZ() + Math.sin(d) * size);
	        builder.location(particleLoc).spawn();
	    }
	}
	
	public void remove() {
		itemEntity.setHealth(0);
		itemEntity.remove();
		Bukkit.getScheduler().cancelTask(taskID);
	}
	
	private static ItemStack createChest() {
		ItemStack item = new ItemStack(Material.ENDER_CHEST);
		item.editMeta(meta -> {
			meta.displayName(Component.text("Special Item").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
		});
		item.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 1);
		return item;
	}

	public SpecialItem getItem() {
		return item;
	}
	
	public static ItemStack getChest() {
		return CHEST;
	}
	
}
