package de.petropia.chickenLeagueLobby.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import de.petropia.chickenLeagueHost.mysql.ArenaRecord;
import de.petropia.chickenLeagueHost.util.MessageSender;
import de.petropia.chickenLeagueLobby.join.ArenaData;
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
			if(ArenaData.getInstance().getArenas().size() == 0) {
				MessageSender.getInstace().sendMessage(player, Component.text("Keine Arenen verfügbar!").color(NamedTextColor.RED));
				return true;
			}
			for(ArenaRecord arena : ArenaData.getInstance().getArenas()) {
				MessageSender.getInstace().sendMessage(player, Component.text(arena.name() + " - " + arena.mode().name() + " - " + arena.players()));
			}
		}
		
		if(args[0].equalsIgnoreCase("quick") && player.hasPermission("chickenLeague.admin")){
			if(ArenaData.getInstance().getCurrent1v1() == null) {
				MessageSender.INSTANCE.sendMessage(player, Component.text("1v1: null"));
			}
			else {
				MessageSender.INSTANCE.sendMessage(player, Component.text("1v1: " + ArenaData.getInstance().getCurrent1v1().name()));
			}
			if(ArenaData.getInstance().getCurrent1v1() == null) {
				MessageSender.INSTANCE.sendMessage(player, Component.text("3v3: null"));
			}
			else {
				MessageSender.INSTANCE.sendMessage(player, Component.text("3v3: " + ArenaData.getInstance().getCurrent3v3().name()));
			}
		}
		
		if(args[0].equalsIgnoreCase("join") && player.hasPermission("chickenLeague.admin")) {
			if(args.length != 2) {
				MessageSender.INSTANCE.sendMessage(player, Component.text("Bitte gib einen Arenanamen an").color(NamedTextColor.RED));
			}
			//TODO: Implement join command for admins
		}
		
		if(args[0].equalsIgnoreCase("ping") && player.hasPermission("chickenLeague.admin")) {
			ArrayList<Player> pingList = ArenaData.getInstance().getPingList();
			if(pingList.contains(player)) {
				pingList.remove(player);
				MessageSender.INSTANCE.sendMessage(player, Component.text("Du wurdest von der Ping Liste erfolgreich entfernt"));
				return true;
			}
			pingList.add(player);
			MessageSender.getInstace().sendMessage(player, Component.text("Du wurdest zur Ping Liste hinzugefügt"));
			return true;
		}
		return false;
	}
}
