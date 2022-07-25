package de.petropia.chickenLeagueLobby;

import org.bukkit.plugin.java.JavaPlugin;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.mysql.MySQLManager;
import de.petropia.chickenLeagueHost.util.CloudNetAdapter;
import de.petropia.chickenLeagueLobby.commands.ChickenLeagueLobbyCommand;
import de.petropia.chickenLeagueLobby.join.ArenaData;
import de.petropia.chickenLeagueLobby.listener.PlayerPortalListener;

public class ChickenLeagueLobby extends JavaPlugin {
	
	private static ChickenLeagueLobby plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		saveDefaultConfig();
		saveConfig();
		reloadConfig();
		setConstants();
		if(!MySQLManager.setup()) {
			this.getServer().getPluginManager().disablePlugin(this);
		}
		this.getCommand("chickenLeagueLobby").setExecutor(new ChickenLeagueLobbyCommand());
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getPluginManager().registerEvents(new PlayerPortalListener(), plugin);
		ArenaData.init();
	}
	
	private void setConstants() {
		Constants.plugin = plugin;
		Constants.config = getConfig();
		Constants.debug = getConfig().getBoolean("debug");
		Constants.serverName = CloudNetAdapter.getServerInstanceName();
		Constants.setupFile = getResource("dbsetup.sql");
	}
}
