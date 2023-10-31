package net.heckerdev.secretlife.events;

import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.swing.text.html.parser.Entity;

public class DamageEventListener implements Listener{
    public void onDamage(EntityDamageEvent event, Entity entity) {
        if (event.getEntityType() == EntityType.PLAYER) {
            event.getFinalDamage();
            event.setCancelled(true);
        }
    }
}
