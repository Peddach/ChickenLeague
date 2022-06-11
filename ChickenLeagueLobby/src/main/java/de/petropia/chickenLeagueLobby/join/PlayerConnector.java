package de.petropia.chickenLeagueLobby.join;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.mysql.ArenaRecord;
import de.petropia.chickenLeagueHost.mysql.MySQLManager;

public class PlayerConnector {
	
	private static final ArrayList<Player> BLACKLIST = new ArrayList<>();
	
	private final Player player;
	private final ArenaRecord arena;
	
	public PlayerConnector(Player player, ArenaRecord arena) {
		this.player = player;
		this.arena = arena;
		if(BLACKLIST.contains(player)) {
			return;
		}
		addToBlackList(player);
		writeToMySQLAndConnect();
	}
	
	private void writeToMySQLAndConnect() {
		MySQLManager.addPlayerToTeleport(player.getName(), arena.name(), arena.server());
		Bukkit.getScheduler().runTaskLaterAsynchronously(Constants.plugin, () -> {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF(arena.server());
			player.sendPluginMessage(Constants.plugin, "BungeeCord", out.toByteArray());
		}, 30);
	}
	
	private void addToBlackList(Player player) {
		BLACKLIST.add(player);
		Bukkit.getScheduler().runTaskLater(Constants.plugin, () -> {
			BLACKLIST.remove(player);
		}, 6*20);
	}
}
