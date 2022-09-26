package de.petropia.chickenLeagueHost.listener;

import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.arena.GameState;
import de.petropia.chickenLeagueHost.arena.GameTime;
import de.petropia.chickenLeagueHost.arena.GoalCountDown;
import de.petropia.chickenLeagueHost.chickenbats.DiamondBat;
import de.petropia.chickenLeagueHost.chickenbats.GoldenBat;
import de.petropia.chickenLeagueHost.chickenbats.WoodenBat;
import de.petropia.chickenLeagueHost.events.GameStateChangeEvent;
import de.petropia.chickenLeagueHost.items.LeaveItem;
import de.petropia.chickenLeagueHost.util.InventoryUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * Listener which does misc stuff on gamestate change ex. deleteing arena<
 */
public class GameStateChangeListener implements Listener{
	
	private int taskID;
	
	@EventHandler
	public void onGameStateChangeEvent(GameStateChangeEvent event) {
		if(event.getAfter() == GameState.ENDING) {
			event.getArena().getBatManager().resetAllBuffs();
			event.getArena().getSpecialItemManager().stop();
			taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Constants.plugin, new Runnable() {
				
				int i = 20;
				
				@Override
				public void run() {
					if(i % 5 == 0) {
						Constants.plugin.getMessageUtil().sendMessage(Audience.audience(event.getArena().getPlayers()), Component.text("Der Server stoppt in " + i + " Sekunden!").color(NamedTextColor.RED));
					}
					if(i == 0) {
						event.getArena().delete();
						new Arena(event.getArena().getArenaMode());
						Bukkit.getScheduler().cancelTask(taskID);
					}
					i--;
				}
			}, 0, 20);
			event.getArena().getGameTime().stop();
			event.getArena().getPlayers().forEach(player -> {
				InventoryUtil.clearPlayer(player);
				LeaveItem.giveItemPlayer(player);
			});
		}
		if(event.getAfter() == GameState.INGAME) {
			event.getArena().getPlayers().forEach(InventoryUtil::clearPlayer);
			new GoalCountDown(event.getArena());
			event.getArena().setGameTime(new GameTime(event.getArena()));
			event.getArena().getSpecialItemManager().start();
			event.getArena().getPlayers().forEach(p -> {
				p.getInventory().setItem(0, WoodenBat.getInstance().getItem());
				p.getInventory().setItem(1, GoldenBat.getInstance().getItem());
				p.getInventory().setItem(2, DiamondBat.getInstance().getItem());
			});
		}
	}

}
