package de.petropia.chickenLeagueHost.specialItem;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.inventory.ItemStack;

import com.destroystokyo.paper.ParticleBuilder;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.Arena;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class MysteryChest implements Listener{
	
	private final ItemStack chest = createChest();
	private final SpecialItem item;
	private final Item itemEntity;
	private final Arena arena;
	private final Location location;
	private int taskID = -1;
	
	public MysteryChest(Location location, SpecialItem item, Arena arena) {
		this.item = item;
		this.arena = arena;
		this.location = location;
		itemEntity = location.getWorld().dropItem(location, chest);
		itemEntity.customName(Component.text("Special Item").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
		itemEntity.setCustomNameVisible(true);
		Bukkit.getServer().getPluginManager().registerEvents(this, Constants.plugin);
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Constants.plugin, () -> {
			showParticle();
		}, 1, 5);
	}
	
	public void showParticle() {
		ParticleBuilder builder = new ParticleBuilder(Particle.REDSTONE)
				.count(1)
				.color(Color.ORANGE)
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
		itemEntity.remove();
		HandlerList.unregisterAll(this);
		Bukkit.getScheduler().cancelTask(taskID);
	}
	
	private ItemStack createChest() {
		ItemStack item = new ItemStack(Material.ENDER_CHEST);
		item.editMeta(meta -> {
			meta.displayName(Component.text("Special Item").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
		});
		item.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 1);
		return item;
	}
	
	@EventHandler
	public void onItemPickUp(EntityPickupItemEvent event) {
		if(!event.getItem().getItemStack().equals(chest)){
			return;
		}
		event.setCancelled(true);
		if(event.getEntityType() != EntityType.PLAYER) {
			return;
		}
		Player player = (Player) event.getEntity();
		if(player.getInventory().getItem(8) != null) {
			return;
		}
		arena.getSpecialItemManager().getFreeLocations().add(location);
		event.getItem().remove();
		item.activate(player);
		Bukkit.getScheduler().cancelTask(taskID);
	}
	
	@EventHandler
	public void onItemDespawn(ItemDespawnEvent event) {
		if(event.getEntity().getItemStack().equals(chest)) {
			event.setCancelled(true);
		}
	}
	
}
