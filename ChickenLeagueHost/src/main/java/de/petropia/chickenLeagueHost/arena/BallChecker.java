package de.petropia.chickenLeagueHost.arena;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.events.PlayerGoalEvent;
import de.petropia.chickenLeagueHost.team.ChickenLeagueTeam;
import de.petropia.chickenLeagueHost.util.MessageSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class BallChecker {
	
	private final int x1;
	private final int z1;
	private final int x2;
	private final int z2;
	private int taskID = -1;
	private final Arena arena;
	private final ChickenLeagueTeam team;
	
	public BallChecker(int x1, int z1, int x2, int z2, Arena arena, ChickenLeagueTeam team) {
		this.x1 = x1;
		this.z1 = z1;
		this.x2 = x2;
		this.z2 = z2;
		this.arena = arena;
		this.team = team;
		start();
	}
	
	public void start() {
		if(taskID != -1) {
			MessageSender.getInstace().showDebugMessage("Failed BallChecker start");
			return;
		}
		taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Constants.plugin, () -> {	
			if(arena.getBall().getChicken() == null || arena.getBall().getChicken().getLocation() == null) {
				return;
			}
			Location chickenLoc = arena.getBall().getChicken().getLocation();
			double chickenX = chickenLoc.getX();
			double chickenZ = chickenLoc.getZ();
			if(checkCoordinates(x1, x2, chickenX) & checkCoordinates(z1, z2, chickenZ)) {
				team.setScore(team.getScore() + 1);
				Bukkit.getPluginManager().callEvent(new PlayerGoalEvent(arena, arena.getBall().getLastHit(), team));
			}
		}, 20, 20);
		MessageSender.getInstace().showDebugMessage(Component.text("BallChecker start"));
	}
	
	private boolean checkCoordinates(int coord1, int coord2, double loc) {
		int[] coords = new int[2];
		coords[0] = coord1;
		coords[1] = coord2;
		Arrays.sort(coords);
		boolean goal = ((int)loc >= coords[0] && (int)loc <= coords[1]);
		return goal;
	}
	
	public void pause() {
		Bukkit.getScheduler().cancelTask(taskID);
		taskID = -1;
		MessageSender.getInstace().showDebugMessage("BallChecker pause");
	}

}
