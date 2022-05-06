package de.petropia.chickenLeagueHost.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.petropia.chickenLeagueHost.events.PlayerQuitArenaEvent;
import de.petropia.chickenLeagueHost.util.MessageSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class PlayerLeaveArenaListener implements Listener {
	private static final Component LEAVE_MESSAGE = Component.text(" hat das Spiel verlassen!").color(NamedTextColor.GRAY);
	
	public PlayerLeaveArenaListener() {};
	
	@EventHandler
	public void onPlayerLeaveArena(PlayerQuitArenaEvent event) {
		MessageSender.INSTANCE.broadcastMessage(event.getArena(), event.getPlayer().name().append(LEAVE_MESSAGE));
	}
}
