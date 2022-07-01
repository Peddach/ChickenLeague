package de.petropia.chickenLeagueHost.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.arena.ArenaMode;
import de.petropia.chickenLeagueHost.arena.GameState;
import fr.mrmicky.fastboard.FastBoard;

public class ScoreboardManager {

	private HashMap<Player, FastBoard> playerBoardMap = new HashMap<>();
	private int taskID;

	public ScoreboardManager(final Arena arena) {
		Runnable updateTask = () -> {
			if (arena.getGameState() == GameState.WAITING) {
				for (FastBoard fastBoard : playerBoardMap.values()) {
					fastBoard.updateLines(" ", "§7Warte auf weitere", "§7Spieler", " ");
				}
			}
			if (arena.getGameState() == GameState.STARTING) {
				int maxPlayer = 2;
				if (arena.getArenaMode() == ArenaMode.THREE_VS_THREE) {
					maxPlayer = 6;
				}
				if (arena.getArenaMode() == ArenaMode.FIVE_VS_FIVE) {
					maxPlayer = 10;
				}
				for (FastBoard fastboard : playerBoardMap.values()) {
					fastboard.updateLines(" ", "§a§lSpieler", "§7>> " + arena.getPlayers().size() + "/" + maxPlayer, " ", "§a§lModus", "§7>>" + arenaModeAsReadableString(arena.getArenaMode()), " ");
				}
			}
			if (arena.getGameState() == GameState.INGAME) {
				for (Player player : arena.getPlayers()){
					if (player == null) {
						continue;
					}
					FastBoard fastboard = playerBoardMap.get(player);
					List<String> lines = new ArrayList<>();
					lines.add(" ");
					lines.add("§a§lDein Team");
					if(arena.getTeam1().isPlayerPresent(player)) {
						lines.add("§7>> " + "§9Team 1");
						lines.add(" ");
						lines.add("§a§lSpielstand");
						lines.add("§7>> §9" + arena.getTeam1().getScore() + "§7 - §c" + arena.getTeam2().getScore());
					}
					if(arena.getTeam2().isPlayerPresent(player)) {
						lines.add("§7>> " + "§cTeam 2");
						lines.add(" ");
						lines.add("§a§lSpielstand");
						lines.add("§7>> §c" + arena.getTeam2().getScore() + " §7-§9 " + arena.getTeam1().getScore());
					}
					lines.add(" ");
					lines.add("§a§lZeit");
					lines.add("§7>> " + arena.getGameTime().getTimeAsString());
					lines.add(" ");
					fastboard.updateLines(lines);
				}
			}
			if (arena.getGameState() == GameState.ENDING) {
				for (Player player : arena.getPlayers()) {
					FastBoard fastboard = playerBoardMap.get(player);
					fastboard.updateLines(" ", "§a§lZeit:", "§7>> " + arena.getGameTime().getTimeAsString(), " ");
				}
			}
		};

		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Constants.plugin, updateTask, 20, 20);

	}

	public void addPlayer(Player player) {
		FastBoard fastboard = new FastBoard(player);
		fastboard.updateTitle("§6§lPetropia.de");
		playerBoardMap.put(player, fastboard);
	}

	public void removePlayer(Player player) {
		if (!playerBoardMap.get(player).isDeleted()) {
			playerBoardMap.get(player).delete();
		}
		playerBoardMap.remove(player);
	}

	public void deleteScordboardManager() {
		Bukkit.getScheduler().cancelTask(taskID);
		for (FastBoard fastboard : playerBoardMap.values()) {
			if (!fastboard.isDeleted()) {
				fastboard.delete();
			}
		}
		playerBoardMap.clear();
	}

	private String arenaModeAsReadableString(ArenaMode mode) {
		String[] stringArr = mode.name().split("_");
		String finalString = "";
		for (String string : stringArr) {
			finalString = finalString + " " + string;
		}
		return finalString.toLowerCase();
	}

}
