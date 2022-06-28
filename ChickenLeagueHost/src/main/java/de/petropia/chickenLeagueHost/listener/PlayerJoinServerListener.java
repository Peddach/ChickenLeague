package de.petropia.chickenLeagueHost.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.mysql.MySQLManager;
import de.petropia.chickenLeagueHost.util.CloudNetAdapter;
import de.petropia.chickenLeagueHost.util.MessageSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class PlayerJoinServerListener implements Listener{
	
	private static final Component DATA_LOADING_ERROR = Component.text("Felher beim Laden der Daten!").color(NamedTextColor.RED);
	private static final Component ARENA_FULL_OR_STARTED = Component.text("Das Spiel welchem du versuchst beizutreten ist voll oder schon gestartet!").color(NamedTextColor.RED).decorate(TextDecoration.ITALIC);
	
	@EventHandler
	public void onPlayerJoinServerEvent(PlayerJoinEvent event) {
		event.joinMessage(null);
		final Player player = event.getPlayer();
		hideAllPlayers(player);
		Bukkit.getScheduler().runTaskAsynchronously(Constants.plugin, () -> {
			String arena = MySQLManager.readPlayerTeleport(player);
			MySQLManager.deletePlayerFromTeleport(player.getName());
			joinSync(player, arena);
		});
	}
	
	private void joinSync(Player player, String arena) {
		Bukkit.getScheduler().runTask(Constants.plugin, () -> {
			for(Arena i : Arena.getArenas()) {
				if(!i.getName().equalsIgnoreCase(arena)) {
					continue;
				}
				if(!i.addPlayer(player)) {
					MessageSender.INSTANCE.sendMessage(player, ARENA_FULL_OR_STARTED);
					CloudNetAdapter.sendPlayerToLobbyTask(player);
					return;
				}
				else {
					showPlayers(i, player);
					teleportToSpawn(player);
					return;
				}
			}
			MessageSender.INSTANCE.sendMessage(player, DATA_LOADING_ERROR);
			CloudNetAdapter.sendPlayerToLobbyTask(player);
		});
	}
	
	private void showPlayers(Arena arena, Player player) {
		arena.getPlayers().forEach(p -> {
			player.showPlayer(Constants.plugin, p);
			p.showPlayer(Constants.plugin, player);
		});
	}
	
	private void hideAllPlayers(Player player) {
		Bukkit.getServer().getOnlinePlayers().forEach(p -> {
			p.hidePlayer(Constants.plugin, player);
			player.hidePlayer(Constants.plugin, p);
		});
	}
	
	private void teleportToSpawn(Player player) {
		final double x = Constants.config.getDouble("Spawn.X");
		final double y = Constants.config.getDouble("Spawn.Y");
		final double z = Constants.config.getDouble("Spawn.Z");
		final long yaw = Constants.config.getLong("Spawn.Yaw");
		final long pitch = Constants.config.getLong("Spawn.Pitch");
		final Location location = new Location(Bukkit.getWorld("world"), x, y, z, yaw, pitch);
		player.teleport(location);
	}
}
