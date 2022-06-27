package de.petropia.chickenLeagueHost;


import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;

import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.arena.ArenaMode;
import de.petropia.chickenLeagueHost.commands.ChickenLeagueHostCommand;
import de.petropia.chickenLeagueHost.listener.ChatListener;
import de.petropia.chickenLeagueHost.listener.ChickenDamageListener;
import de.petropia.chickenLeagueHost.listener.GameStateChangeListener;
import de.petropia.chickenLeagueHost.listener.PlayerGoalListener;
import de.petropia.chickenLeagueHost.listener.PlayerJoinArenaListener;
import de.petropia.chickenLeagueHost.listener.PlayerJoinServerListener;
import de.petropia.chickenLeagueHost.listener.PlayerLeaveArenaListener;
import de.petropia.chickenLeagueHost.listener.PlayerLeaveServerListener;
import de.petropia.chickenLeagueHost.listener.PlayerMoveListener;
import de.petropia.chickenLeagueHost.mysql.MySQLManager;
import de.petropia.chickenLeagueHost.util.CloudNetAdapter;

public class ChickenLeagueHost extends JavaPlugin{
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		saveConfig();
		reloadConfig();
		
		Constants.plugin = this;
		Constants.config = getConfig();
		Constants.serverName = CloudNetAdapter.getServerInstanceName();
		Constants.setupFile = getResource("dbsetup.sql");
		Constants.debug = getConfig().getBoolean("debug");
		
		MultiverseCore mvCore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		MVWorldManager worldManager = mvCore.getMVWorldManager();
		worldManager.loadWorld("ONE_VS_ONE");
		worldManager.loadWorld("THREE_VS_THREE");
		worldManager.loadWorld("FIVE_VS_FIVE");
		
		if(!MySQLManager.setup()) {
			getLogger().warning("Could not Connect to database!!!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		registerCommands();
		registerListener();
		createArenas();
	}
	
	private void registerCommands() {
		this.getCommand("ChickenLeague").setExecutor(new ChickenLeagueHostCommand());
	}

	private void createArenas() {
		int arenas = getConfig().getInt("Arenas");
		for(int i = 0; i < arenas; i++) {
			if(i % 2 == 0) {
				new Arena(ArenaMode.ONE_VS_ONE);
				continue;
			}
			new Arena(ArenaMode.THREE_VS_THREE);
		}
	}
	
	private void registerListener() {
		final PluginManager manager = Bukkit.getServer().getPluginManager();
		manager.registerEvents(new PlayerJoinArenaListener(), this);
		manager.registerEvents(new PlayerLeaveArenaListener(), this);
		manager.registerEvents(new PlayerJoinServerListener(), this);
		manager.registerEvents(new PlayerLeaveServerListener(), this);
		manager.registerEvents(new ChatListener(), this);
		manager.registerEvents(new GameStateChangeListener(), this);
		manager.registerEvents(new PlayerGoalListener(), this);
		manager.registerEvents(new PlayerMoveListener(), this);
		manager.registerEvents(new ChickenDamageListener(), this);
	}
	
	@Override
	public void onDisable() {
		MySQLManager.purgeDatabase();
	}

}
