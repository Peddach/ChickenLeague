package de.petropia.chickenLeagueHost.specialItem;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class SpecialItem implements Listener {
	
	public abstract void activate(Player player);

}
