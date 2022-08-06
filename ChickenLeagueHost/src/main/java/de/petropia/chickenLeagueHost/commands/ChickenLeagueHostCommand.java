package de.petropia.chickenLeagueHost.commands;

import java.util.ArrayList;
import java.util.List;

import de.petropia.chickenLeagueHost.Constants;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.specialItem.MysteryChest;
import de.petropia.chickenLeagueHost.specialItem.SpecialItem;
import de.petropia.chickenLeagueHost.specialItem.SpecialItemManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ChickenLeagueHostCommand implements CommandExecutor {

	/**
	 * Command for the chickenleague host
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		//Permission check
		Player player = (Player) sender;
		if (!player.hasPermission("chickenLeague.admin")) {
			Constants.plugin.getMessageUtil().sendMessage(player, Component.text("Leider darfst du diesen Command nicht ausführen"));
			return false;
		}
		//teams subcommand to display players in different teams
		if (args.length == 1 && args[0].equalsIgnoreCase("teams")) {
			for (Arena arena : Arena.getArenas()) {
				if (!arena.isPlayerPresent(player)) {
					continue;
				}
				Constants.plugin.getMessageUtil().sendMessage(player, arena.getTeam1().getName());
				for (Player p : arena.getTeam1().getPlayers()) {
					if (p == null) {
						continue;
					}
					Constants.plugin.getMessageUtil().sendMessage(player, p.name());
				}
				Constants.plugin.getMessageUtil().sendMessage(player, arena.getTeam2().getName());
				for (Player p : arena.getTeam2().getPlayers()) {
					if (p == null) {
						continue;
					}
					Constants.plugin.getMessageUtil().sendMessage(player, p.name());
				}
				return true;
			}
			Constants.plugin.getMessageUtil().sendMessage(player, Component.text("Du bist in keiner Arena!"));
			return false;
		}
		//time subcommand to get human readable ingame time
		if (args.length == 1 && args[0].equalsIgnoreCase("time")) {
			for (Arena arena : Arena.getArenas()) {
				if (!arena.isPlayerPresent(player)) {
					continue;
				}
				if (arena.getGameTime() == null) {
					Constants.plugin.getMessageUtil().sendMessage(player, Component.text("Keine Zeit vorhanden"));
					return false;
				}
				Constants.plugin.getMessageUtil().sendMessage(player, Component.text(arena.getGameTime().getTimeAsString()).color(NamedTextColor.GOLD));
				
			}
		}
		//Show all mysteryChests in chat
		if(args.length == 1 && args[0].equalsIgnoreCase("mysterychests")) {
			for (Arena arena : Arena.getArenas()) {
				if(!arena.isPlayerPresent(player)) {
					continue;
				}
				for (Location location : arena.getSpecialItemManager().getChests().keySet()) {
					MysteryChest chest = arena.getSpecialItemManager().getChests().get(location);
					if(chest == null) {
						Constants.plugin.getMessageUtil().sendMessage(player, Component.text(location.getX() + " - " + location.getBlockZ() + " : null"));
						continue;
					}
					Constants.plugin.getMessageUtil().sendMessage(player, Component.text(location.getX() + " - " + location.getBlockZ() + " : ✅"));
				}
			}
		}
		//delete all mysterychests
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
		//give a special item to player based on indexed number
		if(args.length == 2 && args[0].equalsIgnoreCase("Specialitem")) {
			int i = Integer.parseInt(args[1]);
			if(i > SpecialItemManager.getSpecialItems().size()) {
				Constants.plugin.getMessageUtil().sendMessage(player, Component.text("Out of Range"));
				return false;
			}
			SpecialItem item = SpecialItemManager.getSpecialItems().get(i);
			item.activate(player);
			return true;
		}
		return false;
	}

}
