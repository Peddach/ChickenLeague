package de.petropia.chickenLeagueHost.specialItem.items;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import de.petropia.chickenLeagueHost.specialItem.SpecialItem;
import de.petropia.chickenLeagueHost.specialItem.Wall;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class EggWall extends SpecialItem implements Listener {

	private static final ItemStack ITEM = createItem();
	private static final HashMap<Player, Float> LAUNCHING_PLAYERS = new HashMap<>();
	
	@Override
	public void activate(Player player) {
		player.getInventory().setItem(8, ITEM);
	}
	
	@EventHandler
	public void onProjectileLaunchEvent(ProjectileLaunchEvent event) {
		if(event.getEntity().getType() != EntityType.EGG) {
			return;
		}
		if(event.getEntity().getShooter() instanceof Player == false) {
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		if(!player.getInventory().getItemInMainHand().equals(ITEM)) {
			return;
		}
		LAUNCHING_PLAYERS.put(player, player.getLocation().getYaw());
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if(event.getEntityType() != EntityType.EGG) {
			return;
		}
		if(event.getEntity().getShooter() instanceof Player == false) {
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		if(!LAUNCHING_PLAYERS.containsKey(player)) {
			return;
		}
		float yaw = LAUNCHING_PLAYERS.get(player);
		LAUNCHING_PLAYERS.remove(player);
		if(event.getHitBlock() == null) {
			return;
		}
		if(event.getHitBlockFace() != BlockFace.UP) {
			return;
		}
		if((yaw > 135 || yaw < -135) || (yaw > -45 && yaw < 45)) {
			Location hitLocBlock = event.getHitBlock().getLocation();
			int hitX = (int) hitLocBlock.getX();
			int hitY = (int) hitLocBlock.getY();
			int hitZ = (int) hitLocBlock.getZ();
			World world = hitLocBlock.getWorld();
			List<Location> blocks = new ArrayList<>();
			blocks.add(new Location(world, hitX, hitY + 1, hitZ));
			blocks.add(new Location(world, hitX, hitY + 2, hitZ));
			blocks.add(new Location(world, hitX - 1, hitY + 1, hitZ));
			blocks.add(new Location(world, hitX - 1, hitY + 2, hitZ));
			blocks.add(new Location(world, hitX + 1, hitY + 1, hitZ));
			blocks.add(new Location(world, hitX + 1, hitY + 2, hitZ));
			new Wall(blocks);
		}
		if((yaw > 45 && yaw < 135) || (yaw < -45 && yaw > -135)) {
			Location hitLocBlock = event.getHitBlock().getLocation();
			int hitX = (int) hitLocBlock.getX();
			int hitY = (int) hitLocBlock.getY();
			int hitZ = (int) hitLocBlock.getZ();
			World world = hitLocBlock.getWorld();
			List<Location> blocks = new ArrayList<>();
			blocks.add(new Location(world, hitX, hitY + 1, hitZ));
			blocks.add(new Location(world, hitX, hitY + 2, hitZ));
			blocks.add(new Location(world, hitX, hitY + 1, hitZ - 1));
			blocks.add(new Location(world, hitX, hitY + 2, hitZ - 1));
			blocks.add(new Location(world, hitX, hitY + 1, hitZ + 1));
			blocks.add(new Location(world, hitX, hitY + 2, hitZ + 1));
			new Wall(blocks);
		}
		
	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if(event.getSpawnReason() == SpawnReason.EGG) {
			event.setCancelled(true);
		}
	}
	
	private static ItemStack createItem() {
		ItemStack item = new ItemStack(Material.EGG);
		item.addUnsafeEnchantment(Enchantment.LUCK, 1);
		item.editMeta(meta -> {
			meta.displayName(Component.text("Spiegelei Mauer").color(TextColor.fromCSSHexString("#ffd500")).decorate(TextDecoration.BOLD));
			final List<Component> lore = new ArrayList<>();
			lore.add(Component.text(" "));
			lore.add(Component.text("Wirf das Ei und eine 2x2 Mauer erscheint").color(NamedTextColor.GRAY));
			lore.add(Component.text(" "));
			meta.lore(lore);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS);
		});
		return item;
	}
}
