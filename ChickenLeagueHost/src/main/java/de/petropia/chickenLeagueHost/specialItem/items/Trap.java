package de.petropia.chickenLeagueHost.specialItem.items;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.events.PlayerGoalEvent;
import de.petropia.chickenLeagueHost.specialItem.SpecialItem;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.Sound.Source;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;

public class Trap extends SpecialItem {

	private static final ItemStack ITEM = createItemStack();
	private static final List<Player> TRAPED_PLAYERS = new ArrayList<>();
	private static final List<Player> BLOCKED_PLAYERS = new ArrayList<>();
	
	@Override
	public void activate(Player player) {
		player.getInventory().setItem(8, ITEM);
		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 50, 10));
		Title title = Title.title(Component.text("Falle").color(NamedTextColor.DARK_RED), Component.text("Du hast Pech gehabt!").color(NamedTextColor.GRAY), Times.times(Duration.ofMillis(1500), Duration.ofMillis(200), Duration.ofMillis(200)));
		player.showTitle(title);
		TRAPED_PLAYERS.add(player);
		Bukkit.getScheduler().runTaskLater(Constants.plugin, () -> {
			if(!TRAPED_PLAYERS.contains(player)) {
				return;
			}
			player.getInventory().clear(8);
			TRAPED_PLAYERS.remove(player);
			if(BLOCKED_PLAYERS.contains(player)) {
				return;
			}
			Arena arena = null;
			for(Arena i : Arena.getArenas()) {
				if(!i.isPlayerPresent(player)) {
					continue;
				}
				arena = i;
				break;
			}
			if(arena == null) {
				return;
			}
			Random random = new Random();
			int randInt = random.nextInt(arena.getSpecialItemManager().getLocations().size());
			Location loc = arena.getSpecialItemManager().getLocations().get(randInt);
			player.teleport(loc);
			player.playSound(Sound.sound(org.bukkit.Sound.ENTITY_ENDERMAN_TELEPORT, Source.NEUTRAL, 1F, 1F));
		}, 30);
	}
	
	@EventHandler
	public void onGoal(PlayerGoalEvent event) {
		BLOCKED_PLAYERS.addAll(event.getArena().getPlayers());
		Bukkit.getScheduler().runTaskLater(Constants.plugin, () -> {
			BLOCKED_PLAYERS.removeAll(event.getArena().getPlayers());
		}, 5*20);
		if(event.getPlayer() == null) {
			return;
		}
		if(TRAPED_PLAYERS.contains(event.getPlayer())) {
			TRAPED_PLAYERS.remove(event.getPlayer());
		}
	}
	
	private static ItemStack createItemStack() {
		ItemStack item = new ItemStack(Material.BARRIER);
		item.addUnsafeEnchantment(Enchantment.LUCK, 1);
		item.editMeta(meta -> {
			meta.displayName(Component.text("Falle").color(TextColor.fromCSSHexString("#0c1282")).decorate(TextDecoration.BOLD));
			final List<Component> lore = new ArrayList<>();
			lore.add(Component.text(" "));
			lore.add(Component.text("Du hast eine Falle gezogen").color(NamedTextColor.GRAY));
			lore.add(Component.text(" "));
			meta.lore(lore);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS);
		});
		return item;
	}
}
