package de.petropia.chickenLeagueHost.specialItem.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.specialItem.SpecialItem;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.Sound.Source;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class NetheriteSword extends SpecialItem implements Listener {
	
	private static final ItemStack ITEM = createItemStack();

	@Override
	public void activate(Player player) {
		player.getInventory().setItem(8, ITEM);
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player player){
			if(player.getInventory().getItemInMainHand().equals(ITEM)) {
				event.setCancelled(true);
			}
		}
		if(event.getEntity() instanceof Player == false) {
			return;
		}
		Player attacker = (Player) event.getDamager();
		Player victim = (Player) event.getEntity();
		if(!attacker.getInventory().getItemInMainHand().equals(ITEM)) {
			return;
		}
		attacker.getInventory().setItem(8, new ItemStack(Material.AIR));
		for(Arena arena : Arena.getArenas()) {
			if(!arena.isPlayerPresent(victim)) {
				continue;
			}
			victim.teleport(arena.getMiddle());
			victim.getLocation().setPitch(0);
			victim.playSound(Sound.sound(org.bukkit.Sound.ENTITY_ENDERMAN_DEATH, Source.NEUTRAL, 1, 1));
			attacker.playSound(Sound.sound(org.bukkit.Sound.ENTITY_ENDERMAN_DEATH, Source.NEUTRAL, 1, 1));			
			break;
		}
	}
	
	private static ItemStack createItemStack() {
		ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
		item.addUnsafeEnchantment(Enchantment.LUCK, 1);
		item.editMeta(meta -> {
			meta.displayName(Component.text("Killer 9000").color(TextColor.fromCSSHexString("#f20000")).decorate(TextDecoration.BOLD));
			final List<Component> lore = new ArrayList<>();
			lore.add(Component.text(" "));
			lore.add(Component.text("Schlage einen Spieler und er stirbt sofort").color(NamedTextColor.GRAY));
			lore.add(Component.text(" "));
			meta.lore(lore);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS);
		});
		return item;
	}

}
