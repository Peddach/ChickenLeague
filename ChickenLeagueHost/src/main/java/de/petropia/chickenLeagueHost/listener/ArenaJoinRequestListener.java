package de.petropia.chickenLeagueHost.listener;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.arena.GameState;
import de.petropia.turtleServer.server.TurtleServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ArenaJoinRequestListener implements Listener {

   private final HashMap<UUID, Arena> joiningPlayers = new HashMap<>();

    public ArenaJoinRequestListener(){
        TurtleServer.getInstance().getCloudNetAdapter().setJoinRequestResolver((string, uuid) -> {
            List<Arena> arenas = new ArrayList<>(Arena.getArenas());
            for(Arena arena : arenas){
                if(!arena.getName().equals(string)){
                    continue;
                }
                if(arena.getPlayers().size() >= arena.getMaxPlayers()){
                    return false;
                }
                if(!(arena.getGameState() == GameState.WAITING || arena.getGameState() == GameState.STARTING)){
                    return false;
                }
                joiningPlayers.put(uuid, arena);
                Bukkit.getScheduler().runTaskLater(Constants.plugin, () -> joiningPlayers.remove(uuid), 20*5);
                return true;
            }
            return false;
        });
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event){
        hideAllPlayers(event.getPlayer());
        Arena arena = joiningPlayers.get(event.getPlayer().getUniqueId());
        joiningPlayers.remove(event.getPlayer().getUniqueId());
        if(arena == null){
            Constants.plugin.getMessageUtil().sendMessage(event.getPlayer(), Component.text("Dir konnte keine Arena zugeordnet werden", NamedTextColor.RED));
            Constants.plugin.getCloudNetAdapter().sendPlayerToLobby(event.getPlayer());
            event.getPlayer().kick();
            return;
        }
        if(!arena.addPlayer(event.getPlayer())){
            Constants.plugin.getMessageUtil().sendMessage(event.getPlayer(), Component.text("Die Arena ist bereits voll oder im Spiel", NamedTextColor.RED));
            Constants.plugin.getCloudNetAdapter().sendPlayerToLobby(event.getPlayer());
            event.getPlayer().kick();
            return;
        }
        event.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(16);
        teleportToSpawn(event.getPlayer());
        showPlayers(arena, event.getPlayer());
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
