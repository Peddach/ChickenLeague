package de.petropia.chickenLeagueLobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import de.petropia.chickenLeagueHost.util.MessageSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ChickenLeagueLobbyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if(sender instanceof Player == false) {
			return false;
		}
		
		Player player = (Player) sender;
		
		if(args.length == 0) {
			if(!player.hasPermission("ChickenLeague.admin")) {
				return false;
			}
			MessageSender.INSTANCE.sendMessage(player, Component.text("Subcommands: list, join").color(NamedTextColor.GRAY));
			return false;
		}
		
		if(args[0].equalsIgnoreCase("list") && player.hasPermission("chickenLeague.admin")) {
			//TODO: implement arena list
		}
		
		if(args[0].equalsIgnoreCase("join") && player.hasPermission("chickenLeague.admin")) {
			if(args.length != 2) {
				MessageSender.INSTANCE.sendMessage(player, Component.text("Bitte gib einen Arenanamen an").color(NamedTextColor.RED));
			}
			//TODO: Implement join command for admins
		}
		return false;
	}
}
