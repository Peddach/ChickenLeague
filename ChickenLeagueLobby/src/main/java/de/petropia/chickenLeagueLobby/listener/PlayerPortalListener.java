package de.petropia.chickenLeagueLobby.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueLobby.join.ArenaData;
import de.petropia.chickenLeagueLobby.join.PlayerConnector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class PlayerPortalListener implements Listener {
	
	private static final List<Player> BLACKLIST = new ArrayList<>();
	
	private final int x1_1v1 = Constants.config.getInt("Portal.ONE_VS_ONE.X1");
	private final int z1_1v1 = Constants.config.getInt("Portal.ONE_VS_ONE.Z1");
	private final int x2_1v1 = Constants.config.getInt("Portal.ONE_VS_ONE.X2");
	private final int z2_1v1 = Constants.config.getInt("Portal.ONE_VS_ONE.Z2");
	
	private final int x1_3v3 = Constants.config.getInt("Portal.THREE_VS_THREE.X1");
	private final int z1_3v3 = Constants.config.getInt("Portal.THREE_VS_THREE.Z1");
	private final int x2_3v3 = Constants.config.getInt("Portal.THREE_VS_THREE.X2");
	private final int z2_3v3 = Constants.config.getInt("Portal.THREE_VS_THREE.Z2");
	
	@EventHandler
	public void onPlayerPortalEvent(EntityPortalEnterEvent event) {
		if(event.getEntity() instanceof Player == false) {
			return;
		}
		Player player = (Player) event.getEntity();
		if(BLACKLIST.contains(player)) {
			return;
		}
		BLACKLIST.add(player);
		removeDelayedFromBlacklist(player);
		double playerX = player.getLocation().getX();
		double playerZ = player.getLocation().getZ();
		if(check1v1(playerX, playerZ)) {
			if(ArenaData.getInstance().getCurrent1v1() == null) {
				Constants.plugin.getMessageUtil().sendMessage(player, Component.text("Leider ist zur Zeit keine Arena für 1vs1 verfügbar!").color(NamedTextColor.RED));
				return;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 10, 5, false, false));
			new PlayerConnector(player, ArenaData.getInstance().getCurrent1v1());
		}
		if(check3v3(playerX, playerZ)) {
			if(ArenaData.getInstance().getCurrent3v3() == null) {
				Constants.plugin.getMessageUtil().sendMessage(player, Component.text("Leider ist zur Zeit keine Arena für 3vs3 verfügbar!").color(NamedTextColor.RED));
				return;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 10, 5, false, false));
			new PlayerConnector(player, ArenaData.getInstance().getCurrent3v3());
		}
	}
	
	private void removeDelayedFromBlacklist(Player player) {
		Bukkit.getScheduler().runTaskLater(Constants.plugin, () -> {
			BLACKLIST.remove(player);
		}, 10*20);
	}
	
	private boolean check1v1(double playerX, double playerZ) {
		return (checkCoordinates(x1_1v1, x2_1v1, playerX) && checkCoordinates(z1_1v1, z2_1v1, playerZ));
	}
	
	private boolean check3v3(double playerX, double playerZ) {
		return (checkCoordinates(x1_3v3, x2_3v3, playerX) && checkCoordinates(z1_3v3, z2_3v3, playerZ));
	}
	
	private boolean checkCoordinates(int coord1, int coord2, double loc) {
		int[] coords = new int[2];
		coords[0] = coord1;
		coords[1] = coord2;
		Arrays.sort(coords);
		boolean isInside = ((int)loc >= coords[0] && (int)loc <= coords[1]);
		return isInside;
	}
}
