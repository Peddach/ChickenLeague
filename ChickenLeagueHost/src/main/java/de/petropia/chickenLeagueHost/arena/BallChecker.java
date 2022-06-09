package de.petropia.chickenLeagueHost.arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.team.ChickenLeagueTeam;
import de.petropia.chickenLeagueHost.util.MessageSender;

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
			
			Location chickenLoc = arena.getBall().getChicken().getLocation();
			double chickenX = chickenLoc.getX();
			double chickenZ = chickenLoc.getZ();
			
			if(checkCoordinates(x1, x2, (int) chickenX) && checkCoordinates(z1, z2, (int) chickenZ)) {
				team.setScore(team.getScore() + 1);
				MessageSender.INSTANCE.showDebugMessage("Goal - " + team.getName());
			}
		}, 20, 20);
		MessageSender.getInstace().showDebugMessage("BallChecker start");
	}
	
	private boolean checkCoordinates(int coord1, int coord2, int loc) {
		return(loc >= coord1 && loc <=coord2);
	}
	
	public void pause() {
		Bukkit.getScheduler().cancelTask(taskID);
		taskID = -1;
		MessageSender.getInstace().showDebugMessage("BallChecker pause");
	}

}
