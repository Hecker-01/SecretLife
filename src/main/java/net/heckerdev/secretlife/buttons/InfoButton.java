package net.heckerdev.secretlife.buttons;

import net.heckerdev.secretlife.SecretLife;
import net.heckerdev.secretlife.utils.ParticleColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class InfoButton {
    public static void Pressed(Player player, Block block) {
        String InfoMessage = SecretLife.getPlugin().getConfig().getString("messages.info-chat");
        if (InfoMessage != null) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(InfoMessage));
        } else {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> Info message isn't set up properly, please contact an admin!"));
        }

        // Spawn particles
        Particle.DustOptions dustOptions = new Particle.DustOptions(ParticleColor.BLUE, 1.5F);
        block.getWorld().spawnParticle(Particle.REDSTONE, block.getLocation().add(0.5, 1.0, 0.5), 25, dustOptions);
    }
}
