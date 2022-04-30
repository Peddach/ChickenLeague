package de.petropia.chickenLeagueHost.arena;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Player;

public class Arena {
	private final String name;
	private GameState gamestate;
	private final ArenaMode arenaMode;
	private final List<Player> players = new ArrayList<>();
	
	public Arena(ArenaMode mode) {
		setGamestate(GameState.WAITING);
		this.name = getRandomString();
		this.arenaMode = mode;
		
	}
	
	//Copied the 5th time :/.  Dont know author
	private String getRandomString() {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 5;
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		return generatedString;
	}


	public String getName() {
		return name;
	}

	public GameState getGameState() {
		return gamestate;
	}

	public void setGamestate(GameState gamestate) {
		this.gamestate = gamestate;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public ArenaMode getArenaMode() {
		return arenaMode;
	}
	
	
}
