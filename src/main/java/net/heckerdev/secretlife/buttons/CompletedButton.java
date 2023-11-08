package net.heckerdev.secretlife.buttons;

import net.heckerdev.secretlife.utils.ParticleColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CompletedButton{
    public static void Pressed(Player player, Block block) {
        // Message has already been sent, see CompletedButton() in PlayerInteractEventListener.java
        // This is because I can't access the configuration file in here.

        // Spawn particles
        Particle.DustOptions dustOptions = new Particle.DustOptions(ParticleColor.GREEN, 1.5F);
        block.getWorld().spawnParticle(Particle.REDSTONE, block.getLocation().add(0.5, 1.0, 0.5), 25, dustOptions);
        Location enchantParticleLocation = new Location(block.getWorld(), 0.5, 70.2, 11.5);
        Objects.requireNonNull(Bukkit.getWorld("world")).spawnParticle(Particle.ENCHANTMENT_TABLE, enchantParticleLocation, 5000, 0, 0, 0, 1.75);
    }
}
