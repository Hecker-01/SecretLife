package net.heckerdev.secretlife.buttons;

import net.heckerdev.secretlife.SecretLife;
import net.heckerdev.secretlife.utils.ParticleColor;
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
        CompletedButton.removeTasks(player);
        player.getPersistentDataContainer().set(new NamespacedKey(SecretLife.getPlugin(), "secretDifficulty"), PersistentDataType.INTEGER, 0);
        String rerollTitle = SecretLife.getPlugin().getConfig().getString("messages.reroll-title");
        if (rerollTitle != null) {
            Title title = Title.title(MiniMessage.miniMessage().deserialize(rerollTitle), Component.empty());
            player.showTitle(title);
        } else {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> Reroll title isn't set up properly, please contact an admin!"));
        }

        // Spawn particles
        Particle.DustOptions dustOptions = new Particle.DustOptions(ParticleColor.DARK_GREEN, 1.5F);
        block.getWorld().spawnParticle(Particle.REDSTONE, block.getLocation().add(0.5, 1.0, 0.5), 25, dustOptions);
        Location enchantParticleLocation = new Location(block.getWorld(), 0.5, 70.2, 11.5);
        Objects.requireNonNull(Bukkit.getWorld("world")).spawnParticle(Particle.ENCHANTMENT_TABLE, enchantParticleLocation, 5000, 0, 0, 0, 50);
        Bukkit.getScheduler().runTaskLater(SecretLife.getPlugin(), () -> {
            player.playSound(block.getLocation(), "minecraft:secretlife.secret", 1, 1);
            Bukkit.getScheduler().runTaskLater(SecretLife.getPlugin(), () -> {
                ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
                ItemMeta totemItemMeta = totem.getItemMeta();
                totemItemMeta.setCustomModelData(SecretLife.getPlugin().getConfig().getInt("items.reroll-secret-totem-custom-model-data"));
                totem.setItemMeta(totemItemMeta);
                PlayerInventory inventory = player.getInventory();
                ItemStack mainHand = inventory.getItemInMainHand();
                inventory.setItemInMainHand(totem);
                player.playEffect(EntityEffect.TOTEM_RESURRECT);
                inventory.setItemInMainHand(mainHand);
            }, 24);
            Bukkit.getScheduler().runTaskLater(SecretLife.getPlugin(), () -> {
                player.playSound(player, "minecraft:entity.item.pickup", 1, 1);
                giveReroll(player);
                player.getPersistentDataContainer().set(new NamespacedKey(SecretLife.getPlugin(), "secretDifficulty"), PersistentDataType.INTEGER, 2);
            }, 62);
        }, 16);
        Bukkit.getScheduler().runTaskLater(SecretLife.getPlugin(), () -> {
            Particle.DustOptions rerollDustOptions = new Particle.DustOptions(ParticleColor.DARK_GREEN, 3F);
            Location rerollParticleLocation = new Location(block.getWorld(), 0.5, 69.0, 11.5);
            Objects.requireNonNull(Bukkit.getWorld("world")).spawnParticle(Particle.REDSTONE, rerollParticleLocation, 25, rerollDustOptions);
        }, 40);
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
        meta.setCustomModelData(SecretLife.getPlugin().getConfig().getInt("items.reroll-secret-book-custom-model-data"));
        secretBook.setItemMeta(meta);
        player.getInventory().addItem(secretBook);
    }
}
