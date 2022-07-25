package de.petropia.chickenLeagueHost.util;

import org.bukkit.entity.Player;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.wrapper.Wrapper;

public class CloudNetAdapter {
	/**
	 * @return Name of current server instance in CloudNet
	 */
	public static String getServerInstanceName() {
		return Wrapper.getInstance().getCurrentServiceInfoSnapshot().getName();
	}

	/*
	 * split players to all lobby servers
	 */
	public static void sendPlayerToLobbyTask(Player player) {
		IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
		playerManager.getPlayerExecutor(playerManager.getOnlinePlayer(player.getUniqueId())).connectToFallback();
	}
}
