package de.petropia.chickenLeagueHost.specialItem.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.petropia.chickenLeagueHost.specialItem.SpecialItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class BlindnessCrossbow extends SpecialItem implements Listener {
	private static final ItemStack ITEM = createItemStack();
	private static final List<Player> LAUNCHING_PLAYERS = new ArrayList<>();
	
	@Override
	public void activate(Player player) {
		player.getInventory().setItem(8, ITEM);
	}

	@EventHandler
	public void onPlayerShoot(ProjectileLaunchEvent event) {
		if(event.getEntity().getShooter() instanceof Player == false) {
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		if(!player.getInventory().getItemInMainHand().equals(ITEM)) {
			return;
		}
		LAUNCHING_PLAYERS.add(player);
		player.getInventory().clear(8);
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if(event.getEntityType() != EntityType.ARROW) {
			return;
		}
		if(event.getEntity().getShooter() instanceof Player == false) {
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		if(!LAUNCHING_PLAYERS.contains(player)) {
			return;
		}
		LAUNCHING_PLAYERS.remove(player);
		event.setCancelled(true);
		event.getEntity().remove();
		if(event.getHitBlock() != null) {
			return;
		}
		if(event.getHitEntity() == null) {
			return;
		}
		if(event.getHitEntity() instanceof Player == false) {
			return;
		}
		Player hitPlayer = (Player) event.getHitEntity();
		hitPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 5));
	}
	

	private static ItemStack createItemStack() {
		ItemStack item = new ItemStack(Material.CROSSBOW);
		item.addUnsafeEnchantment(Enchantment.LUCK, 1);
		item.editMeta(meta -> {
			meta.displayName(Component.text("Blindheitsarmbrust").color(TextColor.fromCSSHexString("#a4c22f")).decorate(TextDecoration.BOLD));
			final List<Component> lore = new ArrayList<>();
			lore.add(Component.text(" "));
			lore.add(Component.text("Mache einen Spieler blind").color(NamedTextColor.GRAY));
			lore.add(Component.text(" "));
			meta.lore(lore);
			meta.setUnbreakable(true);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS);
		});
		CrossbowMeta meta = (CrossbowMeta) item.getItemMeta();
		meta.addChargedProjectile(new ItemStack(Material.ARROW, 1));
		item.setItemMeta(meta);
		return item;
	}
	
}
