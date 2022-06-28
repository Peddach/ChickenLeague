package de.petropia.chickenLeagueHost.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.Arena;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;

public class MessageSender {
	
	public static final MessageSender INSTANCE = new MessageSender();
	private static final Component PREFIX = Component.text("[").color(NamedTextColor.GRAY).append(
			Component.text("Chicken League").color(NamedTextColor.GOLD).append(
			Component.text("] ").color(NamedTextColor.GRAY)));
	private static final Component DEBUG_PREFIX = PREFIX.append(Component.text(" [INFO] ").color(NamedTextColor.GRAY));
	
	private MessageSender() {}
	
	public void sendMessage(Player player, Component component) {
		player.sendMessage(format(component));
	}
	
	public void broadcastMessage(Arena arena, Component component) {
		Audience audience = Audience.audience(arena.getPlayers());
		audience.sendMessage(format(component));
	}
	
	public void broadcastTitle(Arena arena, Title title) {
		Audience audience = Audience.audience(arena.getPlayers());
		audience.showTitle(title);
	}
	
	public void broadcastTitle(Arena arena, Title title, Sound sound) {
		Audience audience = Audience.audience(arena.getPlayers());
		audience.showTitle(title);
		audience.playSound(sound);
	}
	
	public void showDebugMessage(String message) {
		showDebugMessage(Component.text(message).color(NamedTextColor.GRAY));
	}
	
	public void showDebugMessage(Component message) {
		if(!Constants.debug) {
			return;
		}
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(!player.hasPermission("ChickenLeague.debug")) {
				continue;
			}
			player.sendMessage(DEBUG_PREFIX.append(message));
		}
		Constants.plugin.getComponentLogger().info(message);
	}
	
	public Component format(Component component) {
		return PREFIX.append(component);
	}
	
	public static MessageSender getInstace() {
		return INSTANCE;
	}
	
}
