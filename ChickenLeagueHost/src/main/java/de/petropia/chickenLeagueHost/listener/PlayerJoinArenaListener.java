package de.petropia.chickenLeagueHost.listener;

import net.kyori.adventure.audience.Audience;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.GameCountDown;
import de.petropia.chickenLeagueHost.events.PlayerJoinArenaEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class PlayerJoinArenaListener implements Listener {
	
	private static final Component DESCRIPTION = Component.text(Constants.config.getString("Description")).color(NamedTextColor.GRAY);
	private static final Component DESCRIPTION_TITLE = Component.text("ChickenLeague").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD);
	private static final Component JOIN_MESSAGE = Component.text(" ist dem Spiel beigetreten!").color(NamedTextColor.GRAY);
	
	@EventHandler
	public void onPlayerJoinArena(PlayerJoinArenaEvent event) {
		Constants.plugin.getMessageUtil().broadcastMessage(Audience.audience(event.getArena().getPlayers()), event.getPlayer().name().append(JOIN_MESSAGE));
		if(event.getArena().getPlayers().size() == event.getArena().getMaxPlayers()) {
			new GameCountDown(10, event.getArena(), false);
		}
		event.getPlayer().sendMessage(Component.text(" "));
		event.getPlayer().sendMessage(DESCRIPTION_TITLE);
		event.getPlayer().sendMessage(DESCRIPTION);
		event.getPlayer().sendMessage(Component.text(" "));
	}
}
