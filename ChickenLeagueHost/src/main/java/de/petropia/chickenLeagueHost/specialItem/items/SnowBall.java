package de.petropia.chickenLeagueHost.specialItem.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import de.petropia.chickenLeagueHost.specialItem.SnowSurface;
import de.petropia.chickenLeagueHost.specialItem.SpecialItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.util.Vector;

public class SnowBall extends SpecialItem implements Listener {
	
	private static final ItemStack ITEM = createItemStack();
	private static final ArrayList<Player> LAUNCHING_PLAYERS = new ArrayList<>();
	
	@Override
	public void activate(Player player) {
		player.getInventory().setItem(8, ITEM);
	}
	
	@EventHandler
	public void onThrow(ProjectileLaunchEvent event) {
		if(!(event.getEntity().getShooter() instanceof Player player)) {
			return;
		}
		if(!player.getInventory().getItemInMainHand().equals(ITEM)) {
			return;
		}
		LAUNCHING_PLAYERS.add(player);
	}
	
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if(event.getEntityType() != EntityType.SNOWBALL) {
			return;
		}
		if(!(event.getEntity().getShooter() instanceof Player player)) {
			return;
		}
		if(!LAUNCHING_PLAYERS.contains(player)) {
			return;
		}
		LAUNCHING_PLAYERS.remove(player);
		if(event.getHitEntity() != null){
			event.setCancelled(true);
			Vector vector = event.getEntity().getVelocity().clone();
			vector = vector.multiply(new Vector(-0.25, 1,-0.25));
			vector = vector.add(new Vector(0, 0.3, 0));
			event.getEntity().setVelocity(vector);
			LAUNCHING_PLAYERS.add(player);
			return;
		}
		if(event.getHitBlock() == null) {
			return;
		}
		if(event.getHitBlockFace() != BlockFace.UP) {
			return;
		}
		List<Location> blocks = new ArrayList<>();
		Location hitLoc = event.getHitBlock().getLocation();
		World world = hitLoc.getWorld();
		blocks.add(new Location(world, hitLoc.getX() -1, hitLoc.getY() + 1, hitLoc.getZ() + 1));
		blocks.add(new Location(world, hitLoc.getX(), hitLoc.getY() + 1, hitLoc.getZ() +1));
		blocks.add(new Location(world, hitLoc.getX() + 1, hitLoc.getY() + 1, hitLoc.getZ() + 1));
		blocks.add(new Location(world, hitLoc.getX() - 1, hitLoc.getY() + 1, hitLoc.getZ()));
		blocks.add(new Location(world, hitLoc.getX(), hitLoc.getY() + 1, hitLoc.getZ()));
		blocks.add(new Location(world, hitLoc.getX() + 1, hitLoc.getY() + 1, hitLoc.getZ()));
		blocks.add(new Location(world, hitLoc.getX() - 1, hitLoc.getY() + 1, hitLoc.getZ() -1));
		blocks.add(new Location(world, hitLoc.getX(), hitLoc.getY() + 1, hitLoc.getZ() - 1));
		blocks.add(new Location(world, hitLoc.getX() + 1, hitLoc.getY() + 1, hitLoc.getZ() - 1));
		new SnowSurface(blocks);
	}
	
	private static ItemStack createItemStack() {
		ItemStack item = new ItemStack(Material.SNOWBALL);
		item.addUnsafeEnchantment(Enchantment.LUCK, 1);
		item.editMeta(meta -> {
			meta.displayName(Component.text("Pulverschnee Bombe").color(TextColor.fromCSSHexString("#5ef7f0")).decorate(TextDecoration.BOLD));
			final List<Component> lore = new ArrayList<>();
			lore.add(Component.text(" "));
			lore.add(Component.text("Spawned eine 3x3 Pulverschneefläche").color(NamedTextColor.GRAY));
			lore.add(Component.text(" "));
			meta.lore(lore);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS);
		});
		return item;
	}
}
