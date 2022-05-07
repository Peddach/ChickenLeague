package de.petropia.chickenLeagueHost;


import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;

import de.petropia.chickenLeagueHost.listener.ChatListener;
import de.petropia.chickenLeagueHost.listener.GameStateChangeListener;
import de.petropia.chickenLeagueHost.listener.PlayerJoinArenaListener;
import de.petropia.chickenLeagueHost.listener.PlayerJoinServerListener;
import de.petropia.chickenLeagueHost.listener.PlayerLeaveArenaListener;
import de.petropia.chickenLeagueHost.listener.PlayerLeaveServerListener;
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
		
		MultiverseCore mvCore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		MVWorldManager worldManager = mvCore.getMVWorldManager();
		worldManager.loadWorld("Big_Arena");
		worldManager.loadWorld("Small_Arena");
		
		if(!MySQLManager.setup()) {
			getLogger().warning("Could not Connect to database!!!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		registerListener();
		
	}
	
	private void registerListener() {
		final PluginManager manager = Bukkit.getServer().getPluginManager();
		manager.registerEvents(new PlayerJoinArenaListener(), this);
		manager.registerEvents(new PlayerLeaveArenaListener(), this);
		manager.registerEvents(new PlayerJoinServerListener(), this);
		manager.registerEvents(new PlayerLeaveServerListener(), this);
		manager.registerEvents(new ChatListener(), this);
		manager.registerEvents(new GameStateChangeListener(), null);
	}
	
	@Override
	public void onDisable() {
		MySQLManager.purgeDatabase();
	}

}
