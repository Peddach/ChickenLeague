package de.petropia.chickenLeagueHost.team;

import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;

public class ChickenLeagueTeam {
	private final Player[] players;
	private int score = 0;
	private final Component name;

	public ChickenLeagueTeam(int teamSize, Component name) {
		players = new Player[teamSize];
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
