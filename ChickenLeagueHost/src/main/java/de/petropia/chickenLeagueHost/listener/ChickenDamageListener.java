package de.petropia.chickenLeagueHost.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import de.petropia.chickenLeagueHost.arena.ChickenLeagueBall;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.Sound.Source;

public class ChickenDamageListener implements Listener {

	private final static Sound SOUND = Sound.sound(org.bukkit.Sound.ENTITY_CHICKEN_HURT, Source.NEUTRAL, 1, 1.3F);
	
	 @EventHandler
	 public void onChickenDamage(EntityDamageEvent event) {
		 if(ChickenLeagueBall.getChickens().containsKey(event.getEntity())) {
			 event.setDamage(0);
			 event.getEntity().getLocation().getNearbyPlayers(5).forEach(player -> player.playSound(SOUND));
		 }
	 }
	 
	 @EventHandler
	 public void onPlayerHitChickenEvent(EntityDamageByEntityEvent event) {
		 if(!ChickenLeagueBall.getChickens().containsKey(event.getEntity())) {
			 return;
		 }
		 if(event.getDamager() instanceof Player == false) {
			 return;
		 }
		 Player player = (Player) event.getDamager();
		 ChickenLeagueBall.getChickens().get(event.getEntity()).getBall().setLastHit(player);
	 }
	 
	 @EventHandler
	 public void onChickenDieEvent(EntityDeathEvent event) {
		 event.setDroppedExp(0);
		 event.getDrops().clear();
	 }
}
