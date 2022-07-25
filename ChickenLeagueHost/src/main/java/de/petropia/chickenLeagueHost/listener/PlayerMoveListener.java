package de.petropia.chickenLeagueHost.listener;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerMoveListener implements Listener {
	
	private static final ArrayList<Player> PLAYERS = new ArrayList<>();
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		if(!PLAYERS.contains(event.getPlayer())) {
			return;
		}
		if(event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) {
			event.setTo(event.getFrom());
		}
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		if(!PLAYERS.contains(event.getPlayer())) {
			return;
		}
		remove(event.getPlayer());
	}
	
	public static void add(Player player) {
		PLAYERS.add(player);
		player.setWalkSpeed(0);
	}
	
	public static void remove(Player player) {
		PLAYERS.remove(player);
		player.setWalkSpeed(0.2F);
	}

}
