package de.petropia.chickenLeagueHost;


import de.petropia.chickenLeagueHost.listener.*;
import de.petropia.turtleServer.api.PetropiaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.arena.ArenaMode;
import de.petropia.chickenLeagueHost.commands.ChickenLeagueHostCommand;
import de.petropia.chickenLeagueHost.commands.StartCommand;
import de.petropia.chickenLeagueHost.items.LeaveItem;
import de.petropia.chickenLeagueHost.items.TeamSelectItem;
import de.petropia.chickenLeagueHost.specialItem.MysteryChestListener;
import de.petropia.chickenLeagueHost.specialItem.SpecialItemManager;
import de.petropia.chickenLeagueHost.specialItem.items.BlindnessCrossbow;
import de.petropia.chickenLeagueHost.specialItem.items.ChangeBall;
import de.petropia.chickenLeagueHost.specialItem.items.EggWall;
import de.petropia.chickenLeagueHost.specialItem.items.Enderpearl;
import de.petropia.chickenLeagueHost.specialItem.items.ImposterBall;
import de.petropia.chickenLeagueHost.specialItem.items.LevitationCrossbow;
import de.petropia.chickenLeagueHost.specialItem.items.KnockbackCrossbow;
import de.petropia.chickenLeagueHost.specialItem.items.NetheriteBat;
import de.petropia.chickenLeagueHost.specialItem.items.NetheriteSword;
import de.petropia.chickenLeagueHost.specialItem.items.Rod;
import de.petropia.chickenLeagueHost.specialItem.items.SnowBall;
import de.petropia.chickenLeagueHost.specialItem.items.Trap;
import de.petropia.chickenLeagueHost.team.TeamSelectGUI;
import de.petropia.chickenLeagueHost.util.CloudNetAdapter;

public class ChickenLeagueHost extends PetropiaPlugin {
	
	/**
	 * Entry point of plugin
	 */
	@Override
	public void onEnable() {
		//load config
		saveDefaultConfig();
		saveConfig();
		reloadConfig();
		
		//setting constants
		Constants.plugin = this;
		Constants.config = getConfig();
		Constants.serverName = CloudNetAdapter.getServerInstanceName();
		Constants.setupFile = getResource("dbsetup.sql");
		Constants.debug = getConfig().getBoolean("debug");

		registerCommands();
		registerListener();
		registerSpecialItems();
		createArenas();
	}
	
	/**
	 * register commands and setting executor
	 */
	private void registerCommands() {
		this.getCommand("ChickenLeague").setExecutor(new ChickenLeagueHostCommand());
		this.getCommand("start").setExecutor(new StartCommand());
	}

	/**
	 * Creating Arenas specified in config. Every even number is a 1vs1, every odd a 3vs3
	 */
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
	
	/**
	 * Register special Items with new instance of item
	 */
	private void registerSpecialItems() {
		SpecialItemManager.registerItem(new NetheriteBat());
		SpecialItemManager.registerItem(new Enderpearl());
		SpecialItemManager.registerItem(new NetheriteSword());
		SpecialItemManager.registerItem(new EggWall());
		SpecialItemManager.registerItem(new Rod());
		SpecialItemManager.registerItem(new SnowBall());
		SpecialItemManager.registerItem(new Trap());
		SpecialItemManager.registerItem(new ImposterBall());
		SpecialItemManager.registerItem(new KnockbackCrossbow());
		SpecialItemManager.registerItem(new LevitationCrossbow());
		SpecialItemManager.registerItem(new BlindnessCrossbow());
		SpecialItemManager.registerItem(new ChangeBall());
	}
	
	/**
	 * Register Listener
	 */
	private void registerListener() {
		final PluginManager manager = Bukkit.getServer().getPluginManager();
		manager.registerEvents(new PlayerJoinArenaListener(), this);
		manager.registerEvents(new PlayerLeaveArenaListener(), this);
		manager.registerEvents(new PlayerLeaveServerListener(), this);
		manager.registerEvents(new ChatListener(), this);
		manager.registerEvents(new GameStateChangeListener(), this);
		manager.registerEvents(new PlayerGoalListener(), this);
		manager.registerEvents(new PlayerMoveListener(), this);
		manager.registerEvents(new ChickenDamageListener(), this);
		manager.registerEvents(new DatabaseUpdater(), this);
		manager.registerEvents(new LeaveItem(), this);
		manager.registerEvents(new TeamSelectItem(), this);
		manager.registerEvents(new TeamSelectGUI(null), this);
		manager.registerEvents(new ArenaProtectionListener(), this);
		manager.registerEvents(new ChickenMoveListener(), this);
		manager.registerEvents(new MysteryChestListener(), this);
		manager.registerEvents(new ArenaUpdateResendListener(), this);
		manager.registerEvents(new ArenaJoinRequestListener(), this);
	}
	
	/**
	 * Purge Database on shutdown
	 */
	@Override
	public void onDisable() {
		Arena.getArenas().forEach(a -> getCloudNetAdapter().publishArenaDelete(a.getName()));
	}

}
