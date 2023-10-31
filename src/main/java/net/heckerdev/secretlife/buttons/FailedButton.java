package net.heckerdev.secretlife.buttons;

import net.heckerdev.secretlife.utils.ParticleColor;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class FailedButton {
    public static void Pressed(Player player, Block block) {
        // Message has already been sent, see FailedButton() in PlayerInteractEventListener.java
        // This is because I can't access the configuration file in here.

        // Spawn particles
        Particle.DustOptions dustOptions = new Particle.DustOptions(ParticleColor.DARK_RED, 1.5F);
        block.getWorld().spawnParticle(Particle.REDSTONE, block.getLocation().add(0.5, 1.0, 0.5), 25, dustOptions);
    }
}