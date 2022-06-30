package de.petropia.chickenLeagueHost.team;

import org.bukkit.entity.Player;

import de.petropia.chickenLeagueHost.arena.BallChecker;
import net.kyori.adventure.text.Component;

public class ChickenLeagueTeam {
	private final Player[] players;
	private int score = 0;
	private final Component name;
	private BallChecker ballChecker;

	public ChickenLeagueTeam(int teamSize, Component name) {
		players = new Player[teamSize - 1];
		this.name = name;
	}

	public boolean addPlayer(Player player) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] == null) {
				players[i] = player;
				return true;
			}
		}
		return false;
	}

	public boolean isFull() {
		for (int i = 0; i < players.length; i++) {
			if (players[i] == null) {
				return false;
			}
		}
		return true;
	}

	public void removePlayer(Player player) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] == player) {
				players[i] = null;
			}
		}
	}
	
	public int teamSize() {
		int playerCount = 0;
		for(int i = 0; i < players.length; i++) {
			if(players[i] == null) {
				continue;
			}
			playerCount++;
		}
		return playerCount;
	}
	
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
	
	public BallChecker getBallChecker() {
		return ballChecker;
	}

	public void setBallChecker(BallChecker ballChecker) {
		this.ballChecker = ballChecker;
	}
}
