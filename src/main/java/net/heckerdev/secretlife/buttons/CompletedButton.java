package net.heckerdev.secretlife.buttons;

import net.heckerdev.secretlife.util.ParticleColor;
import net.heckerdev.secretlife.util.TextColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class CompletedButton {
    public static void Pressed(Player player, Block block) {
        // Send message to player
        player.sendMessage(Component.text("✔ You clicked The Completed Button!").color(TextColor.GREEN).decoration(TextDecoration.BOLD, true));

        // Spawn particles
        Particle.DustOptions dustOptions = new Particle.DustOptions(ParticleColor.GREEN, 1.5F);
        block.getWorld().spawnParticle(Particle.REDSTONE, block.getLocation().add(0.5, 1.0, 0.5), 25, dustOptions);
    }
}
