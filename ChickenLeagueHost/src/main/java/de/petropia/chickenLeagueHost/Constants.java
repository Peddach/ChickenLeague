package de.petropia.chickenLeagueHost;

import java.io.InputStream;

import de.petropia.turtleServer.api.PetropiaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class Constants {
	
	/**
	 * Different constant values for lobby and host plugin
	 */
	public static PetropiaPlugin plugin;
	public static FileConfiguration config;
	public static String serverName;
	public static InputStream setupFile;
	public static boolean debug;
	
}
