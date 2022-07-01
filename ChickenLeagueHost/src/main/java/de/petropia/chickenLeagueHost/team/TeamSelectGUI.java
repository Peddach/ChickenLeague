package de.petropia.chickenLeagueHost.team;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.util.MessageSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class TeamSelectGUI implements Listener {
	
	private final Inventory inv = Bukkit.createInventory(null, 9, Component.text("Wähle dein Team", NamedTextColor.GOLD));
	public TeamSelectGUI(Arena arena) {
		if(arena == null) {
			return;
		}
		updateInv(arena);
	}
	
	public void updateInv(Arena arena) {
		inv.setItem(0, getBedForTeam(arena.getTeam1(), Material.BLUE_BED));
		inv.setItem(1, getBedForTeam(arena.getTeam2(), Material.RED_BED));
	}
	
	private ItemStack getBedForTeam(ChickenLeagueTeam team, Material material) {
		if(!team.isFull()) {
			ItemStack item = new ItemStack(material);
			item.editMeta(meta -> {
				meta.displayName(team.getName());
				List<Component> lore = new ArrayList<>();
				lore.add(Component.text(" "));
				lore.add(Component.text("Leer").color(NamedTextColor.GRAY));
				lore.add(Component.text(" "));
				meta.lore(lore);
			});
			return item;
		}
		else {
			ItemStack item = new ItemStack(material);
			item.editMeta(meta -> {
				meta.displayName(team.getName());
				List<Component> lore = new ArrayList<>();
				lore.add(Component.text(" "));
				for(Player player : team.getPlayers()) {
					if(player == null) {
						continue;
					}
					lore.add(player.name().color(NamedTextColor.GRAY));
				}
				lore.add(Component.text(" "));
				meta.lore(lore);
			});
			return item;
		}
	}
	
	@EventHandler
	public void onPlayerChooseTeam(InventoryClickEvent event) {
		for(Arena arena : Arena.getArenas()) {
			if(!arena.getTeamSelectGui().getInv().equals(event.getInventory())) {
				continue;
			}
			event.setCancelled(true);
			if(event.getSlot() >= 10 || event.getSlot() < 0) {
				return;
			}
			if(event.getSlotType() == SlotType.QUICKBAR) {
				return;
			}
			int slot = event.getSlot();
			Player player = (Player) event.getWhoClicked();
			if(slot == 0) {
				toggleTeam(arena.getTeam1(), player, arena);
			}
			if(slot == 1) {
				toggleTeam(arena.getTeam2(), player, arena);
			}
		}
	}
		
	private void toggleTeam(ChickenLeagueTeam team, Player player, Arena arena) {
		if(team.isPlayerPresent(player)) {
			team.removePlayer(player);
			MessageSender.getInstace().sendMessage(player, Component.text("Du hast ").color(NamedTextColor.GRAY).append(team.getName()).append(Component.text(" verlassen").color(NamedTextColor.GRAY)));
			arena.getTeamSelectGui().updateInv(arena);
			return;
		}
		if(team.isFull()) {
			MessageSender.getInstace().sendMessage(player, Component.text("Das Team ist bereits voll").color(NamedTextColor.GRAY));
			arena.getTeamSelectGui().updateInv(arena);
			return;
		}
		team.removePlayer(player);
		arena.getTeam1().removePlayer(player);
		arena.getTeam2().removePlayer(player);
		team.addPlayer(player);
		MessageSender.getInstace().sendMessage(player, Component.text("Du hast ").color(NamedTextColor.GRAY).append(team.getName()).append(Component.text(" betreten").color(NamedTextColor.GRAY)));
		arena.getTeamSelectGui().updateInv(arena);
		return;
	}
	
	public void openForPlayer(Player player) {
		player.openInventory(inv);
	}
	
	public Inventory getInv() {
		return inv;
	}
}
