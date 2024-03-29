package de.petropia.chickenLeagueHost.listener;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.petropia.chickenLeagueHost.arena.Arena;
import de.petropia.chickenLeagueHost.arena.ChickenLeagueBall;
import de.petropia.chickenLeagueHost.chickenbats.DiamondBat;
import de.petropia.chickenLeagueHost.chickenbats.GoldenBat;
import de.petropia.chickenLeagueHost.chickenbats.WoodenBat;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.Sound.Source;

/**
 * Listener to give the ball custom behavior
 * @author Professor_Sam
 *
 */
public class ChickenDamageListener implements Listener {

	private final static Sound SOUND = Sound.sound(org.bukkit.Sound.ENTITY_CHICKEN_HURT, Source.NEUTRAL, 1, 1.3F);
	
	//Play hurt sound and remove damage
	 @EventHandler
	 public void onChickenDamage(EntityDamageEvent event) {
		 if(ChickenLeagueBall.getChickens().containsKey(event.getEntity())) {
			 event.setDamage(0);
			 event.getEntity().getLocation().getNearbyPlayers(5).forEach(player -> player.playSound(SOUND));
			 if(event.getEntity() instanceof LivingEntity livingEntity) {
				 livingEntity.setNoDamageTicks(5);
			 }
		 }
	 }
	 
	 //Let chicken jump a bit on hit and give player speedbuff depending on the bat
	 @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	 public void onPlayerHitChickenEvent(EntityDamageByEntityEvent event) {
		 if(!ChickenLeagueBall.getChickens().containsKey(event.getEntity())) {
			 return;
		 }
		 if(event.getDamager() instanceof Player == false) {
			 return;
		 }
		 Player player = (Player) event.getDamager();
		 ChickenLeagueBall.getChickens().get(event.getEntity()).getBall().setLastHit(player);
		 if(event.getEntity().isOnGround()) {
			 Vector oldVector = event.getEntity().getVelocity().clone();
			 Vector addVector = new Vector(0, 1.2, 0);
			 Vector newVector = oldVector.add(addVector);
			 event.getEntity().setVelocity(newVector);	//Boost chicken up if its on ground
		 }
		 Arena arena = ChickenLeagueBall.getChickens().get(event.getEntity());
		 ItemStack handItem = player.getInventory().getItemInMainHand();
		 if(handItem == null) {
			 return;
		 }
		 //speedbuffing player
		 if(handItem.equals(WoodenBat.getInstance().getItem())) {
			 arena.getBatManager().speedBuffPlayer(player, WoodenBat.getInstance().getSpeedBuff());
		 }
		 if(handItem.equals(GoldenBat.getInstance().getItem())) {
			 arena.getBatManager().speedBuffPlayer(player, GoldenBat.getInstance().getSpeedBuff());
		 }
		 if(handItem.equals(DiamondBat.getInstance().getItem())) {
			 arena.getBatManager().speedBuffPlayer(player, DiamondBat.getInstance().getSpeedBuff());
			 Vector oldVector = player.getLocation().getDirection().clone();
			 Vector multiply = new Vector(-1.6, 1, -1.6);
			 Vector add = new Vector(0, 0.6, 0);
			 Vector newVector = oldVector.multiply(multiply).add(add);	
			 player.setVelocity(newVector);	//give player knockback when hitting with diamond bat
		 }
	 }
	 
	 //remove drops from dead entities
	 @EventHandler
	 public void onChickenDieEvent(EntityDeathEvent event) {
		 event.setDroppedExp(0);
		 event.getDrops().clear();
	 }
}
