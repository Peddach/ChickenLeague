package de.petropia.chickenLeagueLobby.join;

import java.util.ArrayList;

import javax.annotation.CheckForNull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.ArenaMode;
import de.petropia.chickenLeagueHost.arena.GameState;
import de.petropia.chickenLeagueHost.mysql.ArenaRecord;
import de.petropia.chickenLeagueHost.mysql.MySQLManager;
import de.petropia.chickenLeagueHost.util.MessageSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ArenaData {
	
	private ArenaRecord current1v1;
	private ArenaRecord current3v3;
	private ArrayList<ArenaRecord> arenas;
	private ArrayList<Player> pingList = new ArrayList<>();
	private static ArenaData instance;
	
	public ArenaData() {
		Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(Constants.plugin, () -> {
			arenas = MySQLManager.readArenas();
			if(current1v1 == null || !checkArenaIsValidToJoin(current1v1)) {
				current1v1 = chooseNewArena(ArenaMode.ONE_VS_ONE);
			}
			if(current3v3 == null || !checkArenaIsValidToJoin(current3v3)) {
				current3v3 = chooseNewArena(ArenaMode.THREE_VS_THREE);
			}	
		}, 100, 3*20);
	}
	
	public static void init() {
		instance = new ArenaData();
	}
	private boolean checkArenaIsValidToJoin(ArenaRecord arena) {
		if(arena.gameState() == GameState.ENDING || arena.gameState() == GameState.INGAME) {
			return false;
		}
		if(arena.mode() == ArenaMode.ONE_VS_ONE && arena.players() == 2*1) {
			return false;
		}
		
		if(arena.mode() == ArenaMode.THREE_VS_THREE && arena.players() == 2*3) {
			return false;
		}
		
		if(arena.mode() == ArenaMode.ONE_VS_ONE && arena.players() == 2*5) {
			return false;
		}
		return true;
	}
	
	@CheckForNull
	private ArenaRecord chooseNewArena(ArenaMode mode) {
		for(ArenaRecord arena : arenas) {
			if(arena.mode() != mode) {
				continue;
			}
			if(!checkArenaIsValidToJoin(arena)) {
				continue;
			}
			return arena;
		}
		showWarning(mode);
		return null;
	}
	
	private void showWarning(ArenaMode mode) {
		Bukkit.getOnlinePlayers().forEach(player -> {
			if(player.hasPermission("chickenLeague.admin") && pingList.contains(player)) {
				MessageSender.getInstace().sendMessage(player, Component.text("Keine Arena gefunden: " + mode.name()).color(NamedTextColor.RED));
			}
		});
	}
	
	public static ArenaData getInstance() {
		return instance;
	}
	
	public ArrayList<Player> getPingList(){
		return pingList;
	}
	
	public ArrayList<ArenaRecord> getArenas() {
		return arenas;
	}
	
	public ArenaRecord getCurrent1v1() {
		return current1v1;
	}
	
	public ArenaRecord getCurrent3v3() {
		return current3v3;
	}
}