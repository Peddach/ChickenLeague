package de.petropia.chickenLeagueHost.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.specialItem.MysteryChest;
import de.petropia.chickenLeagueHost.util.MessageSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ChickenLeagueHostCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		if (!player.hasPermission("chickenLeague.admin")) {
			MessageSender.getInstace().sendMessage(player, Component.text("Leider darfst du diesen Command nicht ausführen"));
			return false;
		}
		if (args.length == 1 && args[0].equalsIgnoreCase("teams")) {
			for (Arena arena : Arena.getArenas()) {
				if (!arena.isPlayerPresent(player)) {
					continue;
				}
				MessageSender.getInstace().sendMessage(player, arena.getTeam1().getName());
				for (Player p : arena.getTeam1().getPlayers()) {
					if (p == null) {
						continue;
					}
					MessageSender.INSTANCE.sendMessage(player, p.name());
				}
				MessageSender.getInstace().sendMessage(player, arena.getTeam2().getName());
				for (Player p : arena.getTeam2().getPlayers()) {
					if (p == null) {
						continue;
					}
					MessageSender.INSTANCE.sendMessage(player, p.name());
				}
				return true;
			}
			MessageSender.getInstace().sendMessage(player, Component.text("Du bist in keiner Arena!"));
			return false;
		}
		if (args.length == 1 && args[0].equalsIgnoreCase("time")) {
			for (Arena arena : Arena.getArenas()) {
				if (!arena.isPlayerPresent(player)) {
					continue;
				}
				if (arena.getGameTime() == null) {
					MessageSender.INSTANCE.sendMessage(player, Component.text("Keine Zeit vorhanden"));
					return false;
				}
				MessageSender.INSTANCE.sendMessage(player, Component.text(arena.getGameTime().getTimeAsString()).color(NamedTextColor.GOLD));
				
			}
		}
		if(args.length == 1 && args[0].equalsIgnoreCase("mysterychests")) {
			for (Arena arena : Arena.getArenas()) {
				if(!arena.isPlayerPresent(player)) {
					continue;
				}
				for (Location location : arena.getSpecialItemManager().getChests().keySet()) {
					MysteryChest chest = arena.getSpecialItemManager().getChests().get(location);
					if(chest == null) {
						MessageSender.INSTANCE.sendMessage(player, Component.text(location.getX() + " - " + location.getBlockZ() + " : null"));
						continue;
					}
					MessageSender.INSTANCE.sendMessage(player, Component.text(location.getX() + " - " + location.getBlockZ() + " : ✅"));
				}
			}
		}
		if(args.length == 1 && args[0].equalsIgnoreCase("clearmysterychests")) {
			for (Arena arena : Arena.getArenas()) {
				if(!arena.isPlayerPresent(player)) {
					continue;
				}
				List<Location> locs = new ArrayList<>(arena.getSpecialItemManager().getChests().keySet());
				for (Location location : locs) {
					MysteryChest chest = arena.getSpecialItemManager().getChests().get(location);
					if(chest == null) {
						continue;
					}
					arena.getSpecialItemManager().removeMysteryChest(chest);
				}
				break;
			}
		}
		return false;
	}

}
