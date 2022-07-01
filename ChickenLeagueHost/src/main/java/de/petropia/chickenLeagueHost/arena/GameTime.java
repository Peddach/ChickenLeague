package de.petropia.chickenLeagueHost.arena;

import org.bukkit.Bukkit;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.util.MessageSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class GameTime {
	
	private int time;
	private int taskID = -1;
	private final Arena arena;
	
	public GameTime(Arena arena) {
		time = 0;
		this.arena = arena;
		start();
	}
	
	public void start() {
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Constants.plugin, () -> {
			if(time >= 10*60) {
				stop();
				int team1 = arena.getTeam1().getScore();
				int team2 = arena.getTeam2().getScore();
				if(team1 == team2) {
					arena.setWinner(null);
				}
				if(team1 > team2) {
					arena.setWinner(arena.getTeam1());
				}
				if(team2 > team1) {
					arena.setWinner(arena.getTeam2());
				}
				MessageSender.INSTANCE.broadcastMessage(arena, Component.text("Die Zeit ist vorbei!").color(NamedTextColor.RED));
				return;
			}
			time ++;
		}, 20, 20);
	}
	
	public void stop() {
		if(taskID == -1) {
			return;
		}
		Bukkit.getScheduler().cancelTask(taskID);
		taskID = -1;
	}
	
	public int getTime() {
		return time;
	}
	
	public String getTimeAsString() {
		int seconds = time % 60;
	    int minutes = (time / 60) % 60;
	    String stringSeconds = (seconds<10 && seconds > 0) ? "0" + Integer.toString(seconds) : Integer.toString(seconds);
	    String stringMinutes = (minutes<10 && minutes > 0) ? "0" + Integer.toString(minutes) : Integer.toString(minutes);
	    return stringMinutes + " Minuten " + stringSeconds + " Sekunden";
	}
}