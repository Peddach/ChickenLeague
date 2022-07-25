package de.petropia.chickenLeagueHost.specialItem;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Parent of all special items
 */
public abstract class SpecialItem implements Listener {
	
	/**
	 * Activate the item on ex. pickup
	 * @param player Player
	 */
	public abstract void activate(Player player);

}
