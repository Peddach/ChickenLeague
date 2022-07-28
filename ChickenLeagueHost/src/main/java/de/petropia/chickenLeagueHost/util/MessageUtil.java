package de.petropia.chickenLeagueHost.util;

import de.petropia.chickenLeagueHost.arena.Arena;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;

public class MessageUtil {
	
	public static final MessageUtil INSTANCE = new MessageUtil();
	private static final Component PREFIX = Component.text("[").color(NamedTextColor.GRAY).append(
			Component.text("Chicken League").color(NamedTextColor.GOLD).append(
			Component.text("] ").color(NamedTextColor.GRAY)));

	private MessageUtil() {}

	/**
	 * Broadcast title to arena
	 * @param arena
	 * @param title
	 */
	public void broadcastTitle(Arena arena, Title title) {
		Audience audience = Audience.audience(arena.getPlayers());
		audience.showTitle(title);
	}
	
	/**
	 * Broadcast title to arena with sound
	 * @param arena
	 * @param title
	 * @param sound
	 */
	public void broadcastTitle(Arena arena, Title title, Sound sound) {
		Audience audience = Audience.audience(arena.getPlayers());
		audience.showTitle(title);
		audience.playSound(sound);
	}

	/**
	 * @return Instance of this Message util
	 */
	public static MessageUtil getInstace() {
		return INSTANCE;
	}
	
}
