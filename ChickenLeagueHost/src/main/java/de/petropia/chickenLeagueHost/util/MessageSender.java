package de.petropia.chickenLeagueHost.util;

import org.bukkit.entity.Player;

import de.petropia.chickenLeagueHost.arena.Arena;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class MessageSender {
	
	public static final MessageSender INSTANCE = new MessageSender();
	private static final Component PREFIX = Component.text("[").color(NamedTextColor.GRAY).append(
			Component.text("Chicken League").color(NamedTextColor.GOLD).append(
			Component.text("] ").color(NamedTextColor.GRAY)));
	
	private MessageSender() {}
	
	public void sendMessage(Player player, Component component) {
		player.sendMessage(format(component));
	}
	
	public void broadcastMessage(Arena arena, Component component) {
		Audience audience = Audience.audience(arena.getPlayers());
		audience.sendMessage(format(component));
	}
	
	public Component format(Component component) {
		return PREFIX.append(component);
	}
	
	public static MessageSender getInstace() {
		return INSTANCE;
	}
	
}
