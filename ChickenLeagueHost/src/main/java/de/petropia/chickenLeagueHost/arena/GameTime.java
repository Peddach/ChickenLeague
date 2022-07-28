package de.petropia.chickenLeagueHost.arena;

import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;

import de.petropia.chickenLeagueHost.Constants;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class GameTime {
	
	private int time;
	private int maxTime;
	private int taskID = -1;
	private final Arena arena;
	
	/**
	 * Count how long the arena is ingame 
	 * 
	 * @param arena Arena
	 */
	public GameTime(Arena arena) {
		if(arena.getArenaMode() == ArenaMode.ONE_VS_ONE) {
			maxTime = 10*60; // 10 Minutes * 60 Seconds
		}
		else {
			maxTime = 15*60; // 15 Minutes * 60 Seconds
		}
		time = 0;
		this.arena = arena;
		start();
	}
	
	/**
	 * Start the time to count. When max time is exceeded, winner will be set
	 */
	public void start() {
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Constants.plugin, () -> {
			if(time >= maxTime) {
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
				Constants.plugin.getMessageSender().broadcastMessage(Audience.audience(arena.getPlayers()), Component.text("Die Zeit ist vorbei!").color(NamedTextColor.RED));
				return;
			}
			time ++;
		}, 20, 20);
	}
	
	/**
	 * Stop the time to count up
	 */
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
	
	/**
	 * Return time as human readable String
	 * 
	 * @return human redable representation
	 */
	public String getTimeAsString() {
		int seconds = time % 60;
	    int minutes = (time / 60) % 60;
	    String stringSeconds = (seconds<10 && seconds > 0) ? "0" + Integer.toString(seconds) : Integer.toString(seconds);
	    String stringMinutes = (minutes<10 && minutes > 0) ? "0" + Integer.toString(minutes) : Integer.toString(minutes);
	    return stringMinutes + " Minuten " + stringSeconds + " Sekunden";
	}
}
