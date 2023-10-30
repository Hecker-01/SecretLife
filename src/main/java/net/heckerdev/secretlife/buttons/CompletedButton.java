package net.heckerdev.secretlife.buttons;

import net.heckerdev.secretlife.SecretLife;
import net.heckerdev.secretlife.utils.ParticleColor;
import net.heckerdev.secretlife.utils.TextColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
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
    }
}
