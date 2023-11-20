package net.heckerdev.secretlife.buttons;

import net.heckerdev.secretlife.SecretLife;
import net.heckerdev.secretlife.utils.ParticleColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class FailedButton {
    public static void Pressed(Player player, Block block) {
        int taskDifficulty = player.getPersistentDataContainer().getOrDefault(new NamespacedKey(SecretLife.getPlugin(), "secretDifficulty"), PersistentDataType.INTEGER, 0);
        if (taskDifficulty == 0) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> You don't have a secret task!"));
            return;
        }
        player.getPersistentDataContainer().set(new NamespacedKey(SecretLife.getPlugin(), "secretDifficulty"), PersistentDataType.INTEGER, 0);
        CompletedButton.removeTasks(player);
        Particle.DustOptions dustOptions = new Particle.DustOptions(ParticleColor.DARK_RED, 1.5F);
        block.getWorld().spawnParticle(Particle.REDSTONE, block.getLocation().add(0.5, 1.0, 0.5), 25, dustOptions);

        Location enchantParticleLocation = new Location(block.getWorld(), 0.5, 70.2, 11.5);
        Objects.requireNonNull(Bukkit.getWorld("world")).spawnParticle(Particle.ENCHANTMENT_TABLE, enchantParticleLocation, 5000, 0, 0, 0, 50);
        Bukkit.getScheduler().runTaskLater(SecretLife.getPlugin(), () -> {
            if (taskDifficulty == 1) {
                player.playSound(block.getLocation(), "minecraft:secretlife.fail", 1, 1);
                Bukkit.getScheduler().runTaskLater(SecretLife.getPlugin(), () -> {
                    String FailedTitle = SecretLife.getPlugin().getConfig().getString("messages.failed-title");
                    if (FailedTitle != null) {
                        Title title = Title.title(MiniMessage.miniMessage().deserialize("<dark_red><bold>-5</bold> ❤"), MiniMessage.miniMessage().deserialize(FailedTitle));
                        player.showTitle(title);
                    } else {
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> Failed message isn't set up properly, please contact an admin!"));
                        Title title = Title.title(MiniMessage.miniMessage().deserialize("<dark_red><bold>-5</bold> ❤"), Component.empty());
                        player.showTitle(title);
                    }
                    player.damage(10);
                }, 25);
            } else if (taskDifficulty == 2) {
                player.playSound(block.getLocation(), "minecraft:secretlife.fail", 1, 1);
                Bukkit.getScheduler().runTaskLater(SecretLife.getPlugin(), () -> {
                    String FailedTitle = SecretLife.getPlugin().getConfig().getString("messages.failed-title");
                    if (FailedTitle != null) {
                        Title title = Title.title(MiniMessage.miniMessage().deserialize("<dark_red><bold>-10</bold> ❤"), MiniMessage.miniMessage().deserialize(FailedTitle));
                        player.showTitle(title);
                    } else {
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> Failed message isn't set up properly, please contact an admin!"));
                        Title title = Title.title(MiniMessage.miniMessage().deserialize("<dark_red><bold>-10</bold> ❤"), Component.empty());
                        player.showTitle(title);
                    }
                    player.damage(20);
                }, 24);
            }
        }, 16);
        Bukkit.getScheduler().runTaskLater(SecretLife.getPlugin(), () -> {
            Particle.DustOptions failDustOptions = new Particle.DustOptions(ParticleColor.DARK_RED, 3F);
            Location failParticleLocation = new Location(block.getWorld(), 0.5, 69.0, 11.5);
            Objects.requireNonNull(Bukkit.getWorld("world")).spawnParticle(Particle.REDSTONE, failParticleLocation, 25, failDustOptions);
        }, 40);
    }
}