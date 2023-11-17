package net.heckerdev.secretlife.buttons;

import net.heckerdev.secretlife.SecretLife;
import net.heckerdev.secretlife.utils.ParticleColor;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class RerollButton {
    public static void Pressed(Player player, Block block) {
        if (player.getPersistentDataContainer().getOrDefault(new NamespacedKey(SecretLife.getPlugin(), "secretDifficulty"), PersistentDataType.INTEGER, 0) == 2) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> You've rerolled already!"));
            return;
        } else if (player.getPersistentDataContainer().getOrDefault(new NamespacedKey(SecretLife.getPlugin(), "secretDifficulty"), PersistentDataType.INTEGER, 0) == 0) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> You don't have a secret task!"));
            return;
        }
        player.getPersistentDataContainer().set(new NamespacedKey(SecretLife.getPlugin(), "secretDifficulty"), PersistentDataType.INTEGER, 2);
        String rerollTitle = SecretLife.getPlugin().getConfig().getString("messages.reroll-title");
        Title title = Title.title(MiniMessage.miniMessage().deserialize(rerollTitle), Component.empty());

        // Spawn particles
        Particle.DustOptions dustOptions = new Particle.DustOptions(ParticleColor.DARK_GREEN, 1.5F);
        block.getWorld().spawnParticle(Particle.REDSTONE, block.getLocation().add(0.5, 1.0, 0.5), 25, dustOptions);

        ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta totemItemMeta = totem.getItemMeta();
        totemItemMeta.setCustomModelData(SecretLife.getPlugin().getConfig().getInt("items.totem-custom-model-data"));
        totem.setItemMeta(totemItemMeta);
        removeTasks(player);

        player.playSound(player, "minecraft:secretlife.secret", 1, 1);
        Bukkit.getScheduler().runTaskLater(SecretLife.getPlugin(), () -> {
            PlayerInventory inventory = player.getInventory();
            ItemStack mainHand = inventory.getItemInMainHand();
            inventory.setItemInMainHand(totem);
            player.playEffect(EntityEffect.TOTEM_RESURRECT);
            player.stopSound(SoundStop.named(Key.key("item.totem.use")));
            inventory.setItemInMainHand(mainHand);
            Bukkit.getScheduler().runTaskLater(SecretLife.getPlugin(), () -> {
                player.stopSound(SoundStop.named(Key.key("item.totem.use")));
            }, 1);
        }, 25);
        Bukkit.getScheduler().runTaskLater(SecretLife.getPlugin(), () -> {
            player.playSound(player, "minecraft:entity.item.pickup", 1, 1);
            giveReroll(player);
        }, 62);
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

    private static void giveReroll(Player player) {
        String[] secrets = SecretLife.getPlugin().getConfig().getStringList("reroll-secrets").toArray(new String[0]);
        int randomSecrets = (int) (Math.random() * secrets.length);
        Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        int randomPlayers = (int) (Math.random() * players.length);
        String randomPlayer = players[randomPlayers].getName();
        String secret = secrets[randomSecrets].replace("{player}", randomPlayer);
        ItemStack secretBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) secretBook.getItemMeta();
        meta.setTitle("§4" + player.getName() + "'s Harder Secret Task");
        meta.addPages(MiniMessage.miniMessage().deserialize(secret));
        meta.setAuthor("The Secret Keeper");
        meta.setGeneration(BookMeta.Generation.ORIGINAL);
        meta.setCustomModelData(SecretLife.getPlugin().getConfig().getInt("items.secret-book-custom-model-data"));
        secretBook.setItemMeta(meta);
        player.getInventory().addItem(secretBook);
    }
}
