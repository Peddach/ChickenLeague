package de.petropia.chickenLeagueHost.listener;

import de.petropia.chickenLeagueHost.Constants;
import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.util.CloudNetAdapter;
import de.petropia.turtleServer.api.minigame.ArenaUpdateResendRequestEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArenaUpdateResendListener implements Listener {

    @EventHandler
    public void onUpdateresend(ArenaUpdateResendRequestEvent event){
        Constants.plugin.getLogger().info("Recived Update Resend!");
        for(Arena arena : Arena.getArenas()){
            CloudNetAdapter.publishArenaUpdate(arena);
        }
    }
}
