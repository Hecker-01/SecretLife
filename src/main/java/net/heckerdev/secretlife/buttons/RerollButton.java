package net.heckerdev.secretlife.buttons;

import net.heckerdev.secretlife.utils.ParticleColor;
import net.heckerdev.secretlife.utils.TextColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class RerollButton {
    public static void Pressed(Player player, Block block) {
        // Message has already been sent, see RerollButton() in PlayerInteractEventListener.java
        // This is because I can't access the configuration file in here.

        // Spawn particles
        Particle.DustOptions dustOptions = new Particle.DustOptions(ParticleColor.DARK_GREEN, 1.5F);
        block.getWorld().spawnParticle(Particle.REDSTONE, block.getLocation().add(0.5, 1.0, 0.5), 25, dustOptions);
    }
}
