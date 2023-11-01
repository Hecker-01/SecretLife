package net.heckerdev.secretlife.events;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageEventListener implements Listener{
    public void onDamage(EntityDamageEvent event) {
        Bukkit.getLogger().info("damag!");
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            AttributeInstance health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            health.setBaseValue(health.getValue() - event.getFinalDamage());
            event.setCancelled(true);
            player.sendMessage("damag!");
        }
    }
}
