package de.petropia.chickenLeagueHost.specialItem.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.destroystokyo.paper.ParticleBuilder;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.specialItem.SpecialItem;
import de.petropia.chickenLeagueHost.team.ChickenLeagueTeam;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ImposterBall extends SpecialItem implements Listener{

	private static final ItemStack ITEM = createItemStack();
	
	@Override
	public void activate(Player player) {
		player.getInventory().setItem(8, ITEM);
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		if(!(event.getAction() != Action.RIGHT_CLICK_AIR || event.getAction() != Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		if(event.getPlayer().getInventory().getItemInMainHand() == null || !event.getPlayer().getInventory().getItemInMainHand().equals(ITEM)) {
			return;
		}
		Arena arena = null;
		for (Arena i :Arena.getArenas()) {
			if(!i.isPlayerPresent(event.getPlayer())) {
				continue;
			}
			arena = i;
			break;
		}
		if(arena == null) {
			return;
		}
		if(arena.getBall().getChicken() == null || arena.getBall().getChicken().isDead()){
			Constants.plugin.getMessageUtil().sendMessage(event.getPlayer(), Component.text("Es ist aktuell kein Ball vorhanden!", NamedTextColor.RED));
			return;
		}
		event.getPlayer().getInventory().clear(8);
		new Imposter(arena, event.getPlayer());
	}

	private static ItemStack createItemStack() {
		ItemStack item = new ItemStack(Material.WITHER_ROSE);
		item.addUnsafeEnchantment(Enchantment.LUCK, 1);
		item.editMeta(meta -> {
			meta.displayName(Component.text("Imposter Ball").color(TextColor.fromCSSHexString("#24739e")).decorate(TextDecoration.BOLD));
			final List<Component> lore = new ArrayList<>();
			lore.add(Component.text(" "));
			lore.add(Component.text("Erzeuge einen zeiten Ball").color(NamedTextColor.GRAY));
			lore.add(Component.text(" "));
			meta.lore(lore);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS);
		});
		return item;
	}

}

class Imposter {
	
	private int taskID; 
	
	public Imposter(Arena arena, Player player) {
		Entity ball = arena.getBall().getChicken();
		if(ball == null){
			return;
		}
		Entity imposter = ball.getLocation().getWorld().spawnEntity(ball.getLocation(), ball.getType());
		imposter.setSilent(true);
		imposter.setGlowing(true);
		if(imposter instanceof LivingEntity livingImposter) {
			livingImposter.setRemoveWhenFarAway(false);
			livingImposter.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1D);
			livingImposter.setHealth(1);
		}
		if(imposter instanceof Ageable ageableImposter) {
			ageableImposter.setAdult();						
		}
		if(imposter instanceof Mob mobImposter) {
			mobImposter.setAware(false);
		}
		Bukkit.getScheduler().runTaskLater(Constants.plugin, () -> {
			imposter.remove();
		}, 5*20);
		ChickenLeagueTeam team;
		if(arena.getTeam1().isPlayerPresent(player)) {
			team = arena.getTeam1();
		}
		else {
			team = arena.getTeam2();
		}
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Constants.plugin, new Runnable() {

			@Override
			public void run() {
				if(imposter.isDead()) {
					Bukkit.getScheduler().cancelTask(taskID);
					return;
				}
				List<Player> players = new ArrayList<>();
				for(Player player : team.getPlayers()) {
					if(player == null) {
						continue;
					}
					players.add(player);
				}
				showCircle(players, imposter.getLocation(), Color.RED, imposter.getHeight());
				showCircle(players, ball.getLocation(), Color.GREEN, ball.getHeight());	
			}	
		}, 5, 5);
		ball.setVelocity(getRandomVector());
		imposter.setVelocity(getRandomVector());
	}
	
	private void showCircle(List<Player> players, Location location, Color color, double height) {
		ParticleBuilder builder = new ParticleBuilder(Particle.REDSTONE)
				.count(1)
				.color(color)
				.receivers(players)
				.force(true);
		double size = 0.7;
	    for (int d = 0; d <= 90; d += 1) {
	        Location particleLoc = new Location(location.getWorld(), location.getX(), location.getY() + height + 0.5, location.getZ());
	        particleLoc.setX(location.getX() + Math.cos(d) * size);
	        particleLoc.setZ(location.getZ() + Math.sin(d) * size);
	        builder.location(particleLoc).spawn();
	    }
	}
	
	private Vector getRandomVector() {
		return new Vector(getRandomDouble(), 0.1D, getRandomDouble());
	}
	
	private double getRandomDouble() {
		double min = -0.5;
		double max = 0.5;
		Random r = new Random();
		return min + r.nextDouble() * (max - min);
	}
}
