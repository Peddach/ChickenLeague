package de.petropia.chickenLeagueHost.team;

import org.bukkit.entity.Player;

public class ChickenLeagueTeam {
	private final Player[] players;
	private int score = 0;
	
	public ChickenLeagueTeam(int teamSize) {
		players = new Player[teamSize];
	}
	
	public boolean addPlayer(Player player) {
		for(int i = 0; i < players.length; i++) {
			if(players[i] == null) {
				players[i] = player;
				return true;
			}
		}
		return false;
	}
	
	public boolean isFull() {
		for(int i = 0; i < players.length; i++) {
			if(players[i] == null) {
				return false;
			}
		}
		return true;
	}
	
	public void removePlayer(Player player) {
		for(int i = 0; i < players.length; i++) {
			if(players[i] == player) {
				players[i] = null;
			}
		}
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
}
