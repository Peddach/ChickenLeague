package de.petropia.chickenLeagueHost.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.arena.GameCountDown;
import de.petropia.chickenLeagueHost.util.MessageSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class StartCommand implements CommandExecutor {

	/**
	 * Command to forcestart game
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if(sender instanceof Player == false) {
			return false;
		}
		Player player = (Player) sender;
		if(!player.hasPermission("chickenleague.start")) {
			MessageSender.INSTANCE.sendMessage(player, Component.text("Dieser Command ist Teammitgliedern in Chickenleague vorbehalten"));
			return false;
		}
		Arena arena = null;
		for(Arena i : Arena.getArenas()) {
			if(!i.isPlayerPresent(player)) {
				continue;
			}
			arena = i;
			break;
		}
		MessageSender.INSTANCE.broadcastMessage(arena, player.name().append(Component.text(" hat das Spiel gestartet").color(NamedTextColor.GRAY)));
		new GameCountDown(10, arena, true);
		return true;
	}

}
