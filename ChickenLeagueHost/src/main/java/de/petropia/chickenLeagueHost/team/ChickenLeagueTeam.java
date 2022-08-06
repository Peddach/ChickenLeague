package de.petropia.chickenLeagueHost.team;

import de.petropia.turtleServer.server.prefix.PrefixManager;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;

public class ChickenLeagueTeam {
	private final Player[] players;
	private int score = 0;
	private final Component name;
	private final int x1;
	private final int x2;
	private final int z1;
	private final int z2;

	/**
	 * Object to represent a team in chickenleague
	 * 
	 * @param teamSize max players
	 * @param name DisplayName of team
	 * @param x1 x coord of goal
	 * @param x2 z coord of goal
	 * @param z1 x coord of goal
	 * @param z2 z coord of goal
	 */
	public ChickenLeagueTeam(int teamSize, Component name, int x1, int x2, int z1, int z2) {
		players = new Player[teamSize];
		this.name = name;
		this.x1 = x1;
		this.x2 = x2;
		this.z1 = z1;
		this.z2 = z2;
	}

	/**
	 * Add player to team and set name color
	 * @param player Player
	 * @return false when full
	 */
	public boolean addPlayer(Player player) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] == null) {
				players[i] = player;
				PrefixManager.getInstance().setPlayerNameColor(name.color(), player);
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @return true when full
	 */
	public boolean isFull() {
		for (Player player : players) {
			if (player == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Remove player from team
	 * @param player Player
	 */
	public void removePlayer(Player player) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] == player) {
				players[i] = null;
				PrefixManager.getInstance().resetPlayerNameColor(player);
			}
		}
	}
	
	/**
	 * @return How many players are in the team
	 */
	public int teamSize() {
		int playerCount = 0;
		for (Player player : players) {
			if (player == null) {
				continue;
			}
			playerCount++;
		}
		return playerCount;
	}
	
	/**
	 * 
	 * @param player
	 * @return true when player is in team
	 */
	public boolean isPlayerPresent(Player player) {
		for(Player p : players) {
			if(p == null) {
				continue;
			}
			if(player == p) {
				return true;
			}
		}
		return false;
	}

	public int getX1() {
		return x1;
	}

	public int getX2() {
		return x2;
	}

	public int getZ1() {
		return z1;
	}

	public int getZ2() {
		return z2;
	}
	
	public Player[] getPlayers() {
		return players;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Component getName() {
		return name;
	}
}
