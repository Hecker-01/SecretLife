package net.heckerdev.secretlife.events;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageEventListener implements Listener{

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            AttributeInstance health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            double currentHealth = health.getValue();
            double finalDamage = event.getFinalDamage();
            health.setBaseValue(currentHealth - finalDamage);
        }
    }
}
