package de.petropia.chickenLeagueHost.listener;

import de.petropia.chickenLeagueHost.Constants;
import net.kyori.adventure.audience.Audience;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.petropia.chickenLeagueHost.arena.GameState;
import de.petropia.chickenLeagueHost.events.PlayerQuitArenaEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class PlayerLeaveArenaListener implements Listener {
	private static final Component LEAVE_MESSAGE = Component.text(" hat das Spiel verlassen!").color(NamedTextColor.GRAY);
	
	public PlayerLeaveArenaListener() {};
	
	@EventHandler
	public void onPlayerLeaveArena(PlayerQuitArenaEvent event) {
		Constants.plugin.getMessageUtil().broadcastMessage(Audience.audience(event.getArena().getPlayers()), event.getPlayer().name().append(LEAVE_MESSAGE));
		if(event.getArena().getGameState() != GameState.INGAME) {
			return;
		}
		if(!event.getArena().getTeam1().isPlayerPresent(event.getPlayer())) {
			if(event.getArena().getTeam1().teamSize() - 1 == 0) {
				event.getArena().setWinner(event.getArena().getTeam2());
			}
		}
		if(!event.getArena().getTeam2().isPlayerPresent(event.getPlayer())) {
			if(event.getArena().getTeam2().teamSize() - 1 == 0) {
				event.getArena().setWinner(event.getArena().getTeam1());
			}
		}
	}
}
