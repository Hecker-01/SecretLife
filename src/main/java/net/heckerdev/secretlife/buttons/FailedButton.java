package net.heckerdev.secretlife.buttons;

import net.heckerdev.secretlife.SecretLife;
import net.heckerdev.secretlife.utils.ParticleColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class FailedButton {
    public static void Pressed(Player player, Block block) {
        if (player.getPersistentDataContainer().getOrDefault(new NamespacedKey(SecretLife.getPlugin(), "secretDifficulty"), PersistentDataType.INTEGER, 0) == 0) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> You don't have a secret task!"));
            return;
        }

        // check what difficulty the player is on
        if (player.getPersistentDataContainer().getOrDefault(new NamespacedKey(SecretLife.getPlugin(), "secretDifficulty"), PersistentDataType.INTEGER, 0) == 1) {
            removeTasks(player);
            Particle.DustOptions dustOptions = new Particle.DustOptions(ParticleColor.DARK_RED, 1.5F);
            block.getWorld().spawnParticle(Particle.REDSTONE, block.getLocation().add(0.5, 1.0, 0.5), 25, dustOptions);
            String FailedTitle = SecretLife.getPlugin().getConfig().getString("messages.failed-title");
            if (FailedTitle != null) {
                Title title = Title.title(MiniMessage.miniMessage().deserialize("<dark_red><bold>-5</bold> ❤"), MiniMessage.miniMessage().deserialize(FailedTitle));
                player.showTitle(title);
            } else {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> Failed message isn't set up properly, please contact an admin!"));
                Title title = Title.title(MiniMessage.miniMessage().deserialize("<red><bold>-5</bold> ❤"), Component.empty());
                player.showTitle(title);
            }
            player.damage(10);
            player.getPersistentDataContainer().set(new NamespacedKey(SecretLife.getPlugin(), "secretDifficulty"), PersistentDataType.INTEGER, 0);
        } else if (player.getPersistentDataContainer().getOrDefault(new NamespacedKey(SecretLife.getPlugin(), "secretDifficulty"), PersistentDataType.INTEGER, 0) == 2) {
            removeTasks(player);
            Particle.DustOptions dustOptions = new Particle.DustOptions(ParticleColor.DARK_RED, 1.5F);
            block.getWorld().spawnParticle(Particle.REDSTONE, block.getLocation().add(0.5, 1.0, 0.5), 25, dustOptions);
            String FailedTitle = SecretLife.getPlugin().getConfig().getString("messages.failed-title");
            if (FailedTitle != null) {
                Title title = Title.title(MiniMessage.miniMessage().deserialize("<red><bold>-10</bold> ❤"), MiniMessage.miniMessage().deserialize(FailedTitle));
                player.showTitle(title);
            } else {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> Failed message isn't set up properly, please contact an admin!"));
                Title title = Title.title(MiniMessage.miniMessage().deserialize("<red><bold>-10</bold> ❤"), Component.empty());
                player.showTitle(title);
            }
            player.damage(20);
            player.getPersistentDataContainer().set(new NamespacedKey(SecretLife.getPlugin(), "secretDifficulty"), PersistentDataType.INTEGER, 0);
        }
    }

    private static void removeTasks(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getContents();
        for (ItemStack item : contents) {
            if (item != null) {
                if (item.getType() == Material.WRITTEN_BOOK) {
                    BookMeta meta = (BookMeta) item.getItemMeta();
                    if (meta != null) {
                        if (Objects.requireNonNull(meta.getTitle()).contains("Secret Task")) {
                            inventory.remove(item);
                        }
                    }
                }
            }
        }
    }
}