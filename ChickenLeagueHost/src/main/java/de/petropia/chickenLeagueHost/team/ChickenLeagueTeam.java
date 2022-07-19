package de.petropia.chickenLeagueHost.team;

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

	public ChickenLeagueTeam(int teamSize, Component name, int x1, int x2, int z1, int z2) {
		players = new Player[teamSize - 1];
		this.name = name;
		this.x1 = x1;
		this.x2 = x2;
		this.z1 = z1;
		this.z2 = z2;
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
