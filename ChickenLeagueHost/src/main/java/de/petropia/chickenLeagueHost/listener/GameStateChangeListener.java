package de.petropia.chickenLeagueHost.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.GameState;
import de.petropia.chickenLeagueHost.arena.GoalCountDown;
import de.petropia.chickenLeagueHost.events.GameStateChangeEvent;
import de.petropia.chickenLeagueHost.mysql.MySQLManager;
import de.petropia.chickenLeagueHost.util.MessageSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class GameStateChangeListener implements Listener{
	
	@EventHandler
	public void onGameStateChangeEvent(GameStateChangeEvent event) {
		MySQLManager.updateArena(event.getArena());
		
		if(event.getAfter() == GameState.ENDING) {
			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Constants.plugin, new Runnable() {
				
				int i = 20;
				
				@Override
				public void run() {
					if(i % 5 == 0) {
						MessageSender.INSTANCE.broadcastMessage(event.getArena(), Component.text("Der Server stoppt in " + i + " Sekunden!").color(NamedTextColor.RED));
					}
					if(i == 0) {
						event.getArena().delete();
					}
					i--;
				}
			}, 0, 20);
		}
		if(event.getAfter() == GameState.INGAME) {
			new GoalCountDown(event.getArena());
		}
	}

}
