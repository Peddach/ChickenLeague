package de.petropia.chickenLeagueHost;

import java.io.InputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Constants {
	
	/**
	 * Different constant values for lobby and host plugin
	 */
	public static JavaPlugin plugin;
	public static FileConfiguration config;
	public static String serverName;
	public static InputStream setupFile;
	public static boolean debug;
	
}
