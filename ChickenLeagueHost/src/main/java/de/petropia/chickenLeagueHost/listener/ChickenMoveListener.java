package de.petropia.chickenLeagueHost.listener;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.arena.ArenaMode;
import de.petropia.chickenLeagueHost.arena.ChickenLeagueBall;
import de.petropia.chickenLeagueHost.events.PlayerGoalEvent;
import de.petropia.chickenLeagueHost.team.ChickenLeagueTeam;
import io.papermc.paper.event.entity.EntityMoveEvent;

/**
 * Listener to check if chicken is in goal and fire playerGoalEvent and bounce chicken from white border line
 */
public class ChickenMoveListener implements Listener {
	
	private static final int E_OVO_X_1 = Constants.config.getInt("ONE_VS_ONE.Edges.First.X");
	private static final int E_OVO_X_2 = Constants.config.getInt("ONE_VS_ONE.Edges.Second.X");
	private static final int E_OVO_Z_1 = Constants.config.getInt("ONE_VS_ONE.Edges.First.Z");
	private static final int E_OVO_Z_2 = Constants.config.getInt("ONE_VS_ONE.Edges.Second.Z");

	private static final int E_TVT_X_1 = Constants.config.getInt("THREE_VS_THREE.Edges.First.X");
	private static final int E_TVT_X_2 = Constants.config.getInt("THREE_VS_THREE.Edges.Second.X");
	private static final int E_TVT_Z_1 = Constants.config.getInt("THREE_VS_THREE.Edges.First.Z");
	private static final int E_TVT_Z_2 = Constants.config.getInt("THREE_VS_THREE.Edges.Second.Z");
	
	@EventHandler
	public void onChickenMoveEvent(EntityMoveEvent event) {
		if(!ChickenLeagueBall.getChickens().keySet().contains(event.getEntity())) {
			return;
		}
		Arena arena = ChickenLeagueBall.getChickens().get(event.getEntity());
		Location chickenLoc = event.getEntity().getLocation();
		//First check if goal
		if(checkTeamGoal(arena.getTeam1(), chickenLoc, arena) | checkTeamGoal(arena.getTeam2(), chickenLoc, arena)) {
			return;
		}
		//than check if it should bounce of wall
		if(arena.getArenaMode() == ArenaMode.THREE_VS_THREE || arena.getArenaMode() == ArenaMode.FIVE_VS_FIVE) {
			if(!checkCoordinates(E_TVT_X_1, E_TVT_X_2, chickenLoc.getX())) {
				bounceX(event.getEntity());
				event.setCancelled(true);
			}
			if(!checkCoordinates(E_TVT_Z_1, E_TVT_Z_2, chickenLoc.getZ())) {
				bounceZ(event.getEntity());
				event.setCancelled(true);
			}
			return;
		}	
		if(arena.getArenaMode() == ArenaMode.ONE_VS_ONE) {
			if(!checkCoordinates(E_OVO_X_1, E_OVO_X_2, chickenLoc.getX())) {
				bounceX(event.getEntity());
				event.setCancelled(true);
			}
			if(!checkCoordinates(E_OVO_Z_1, E_OVO_Z_2, chickenLoc.getZ())) {
				bounceZ(event.getEntity());
				event.setCancelled(true);
			}
			return;
		}
	}
	
	/**
	 * Check if ball is in team goal
	 * 
	 * @param team Team to check
	 * @param chickenLoc Location of ball
	 * @param arena Arena
	 * @return true if in goal
	 */
	private boolean checkTeamGoal(ChickenLeagueTeam team, Location chickenLoc, Arena arena) {
		if(checkCoordinates(team.getX1(), team.getX2(), chickenLoc.getX()) & checkCoordinates(team.getZ1(), team.getZ2(), chickenLoc.getZ())) {
			team.setScore(team.getScore() + 1);
			Bukkit.getPluginManager().callEvent(new PlayerGoalEvent(arena, arena.getBall().getLastHit(), team));
			return true;
		}
		return false;
	}
	
	/**
	 * Bounce chicken from if over max x coord
	 * @param entity ball
	 */
	private void bounceX(Entity entity) {
		Vector chickenVector = entity.getVelocity().clone();
		Vector bounceVector = new Vector(-1, 1, 1);
		Vector newVector = chickenVector.multiply(bounceVector);
		entity.setVelocity(newVector);
	}
	
	/**
	 * Bounce chicken from if over max z coord
	 * @param entity ball
	 */
	private void bounceZ(Entity entity) {
		Vector chickenVector = entity.getVelocity().clone();
		Vector bounceVector = new Vector(1, 1, -1);
		Vector newVector = chickenVector.multiply(bounceVector);
		entity.setVelocity(newVector);
	}
	
	/**
	 * Check if entity is over max angle
	 * 
	 * @param coord1
	 * @param coord2
	 * @param loc
	 * @return
	 */
	private boolean checkCoordinates(int coord1, int coord2, double loc) {
		int[] coords = new int[2];
		coords[0] = coord1;
		coords[1] = coord2;
		Arrays.sort(coords);
		boolean isInside = ((int)loc >= coords[0] && (int)loc <= coords[1]);
		return isInside;
	}
}
