package de.petropia.chickenLeagueHost;


import org.bukkit.plugin.java.JavaPlugin;

public class ChickenLeagueHost extends JavaPlugin{
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		saveConfig();
		reloadConfig();
		
		Constants.plugin = this;
		Constants.config = getConfig();
		Constants.serverName = "ChickenLeague-1";	//TODO Implement CloudNet
		Constants.setupFile = getResource("dbsetup.sql");
		
	}

}
