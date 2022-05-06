package de.petropia.chickenLeagueHost.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.petropia.chickenLeagueHost.events.PlayerJoinArenaEvent;
import de.petropia.chickenLeagueHost.util.MessageSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class PlayerJoinArenaListener implements Listener {
	
	private static final Component JOIN_MESSAGE = Component.text(" ist dem Spiel beigetreten!").color(NamedTextColor.GRAY);
	
	public PlayerJoinArenaListener() {};
	
	@EventHandler
	public void onPlayerJoinArena(PlayerJoinArenaEvent event) {
		MessageSender.INSTANCE.broadcastMessage(event.getArena(), event.getPlayer().name().append(JOIN_MESSAGE));
	}
	
}
