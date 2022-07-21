package de.petropia.chickenLeagueHost.specialItem.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.entity.EntityDismountEvent;

import de.petropia.chickenLeagueHost.specialItem.SpecialItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class Enderpearl extends SpecialItem {

	private final ItemStack item = createItemStack();
	private static final List<Player> boundPlayer = new ArrayList<>();
	
	@Override
	public void activate(Player player) {
		player.getInventory().setItem(8, item);
	}

	private ItemStack createItemStack() {
		ItemStack item = new ItemStack(Material.ENDER_PEARL);
		item.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
		item.editMeta(meta -> {
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
			meta.displayName(Component.text("Enderperle").color(TextColor.fromCSSHexString("#36bf5a")).decorate(TextDecoration.BOLD));
			final List<Component> lore = new ArrayList<>();
			lore.add(Component.text(" "));
			lore.add(Component.text("Teleportiere dich hin und her").color(NamedTextColor.GRAY));
			lore.add(Component.text(" "));
			meta.lore(lore);
		});
		return item;
	}
	
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		if(event.getEntityType() != EntityType.ENDER_PEARL) {
			return;
		}
		if(event.getEntity().getShooter() instanceof Player == false) {
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		boundPlayer.add(player);
		event.getEntity().addPassenger(player);
	}
	
	@EventHandler
	public void onPlayerDismound(EntityDismountEvent event) {
		if(event.getEntity() instanceof Player == false) {
			return;
		}
		Player player = (Player) event.getEntity();
		if(!boundPlayer.contains(player)) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if(event.getEntity().getShooter() instanceof Player == false) {
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		if(boundPlayer.contains(player)) {
			boundPlayer.remove(player);
		}
	}
}
