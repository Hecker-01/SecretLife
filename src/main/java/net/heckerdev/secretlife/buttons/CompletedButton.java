package net.heckerdev.secretlife.buttons;

import net.heckerdev.secretlife.SecretLife;
import net.heckerdev.secretlife.utils.ParticleColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Objects;

public class CompletedButton{
    public static void Pressed(Player player, Block block) {
        // Spawn particles
        Particle.DustOptions dustOptions = new Particle.DustOptions(ParticleColor.GREEN, 1.5F);
        block.getWorld().spawnParticle(Particle.REDSTONE, block.getLocation().add(0.5, 1.0, 0.5), 25, dustOptions);
        Location enchantParticleLocation = new Location(block.getWorld(), 0.5, 70.2, 11.5);
        Objects.requireNonNull(Bukkit.getWorld("world")).spawnParticle(Particle.ENCHANTMENT_TABLE, enchantParticleLocation, 5000, 0, 0, 0, 50);
        Bukkit.getScheduler().runTaskLater(SecretLife.getPlugin(), () -> {
            player.playSound(block.getLocation(), "minecraft:secretlife.complete", 1, 1);
        }, 16);
        Bukkit.getScheduler().runTaskLater(SecretLife.getPlugin(), () -> {
            Particle.DustOptions completeDustOptions = new Particle.DustOptions(ParticleColor.GREEN, 2F);
            Location completeParticleLocation = new Location(block.getWorld(), 0.5, 69.0, 11.5);
            Objects.requireNonNull(Bukkit.getWorld("world")).spawnParticle(Particle.REDSTONE, completeParticleLocation, 25, completeDustOptions);
        }, 40);
    }

    public static void removeTasks(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getContents();
        for (ItemStack item : contents) {
            if (item != null) {
                if (item.getType() == Material.WRITTEN_BOOK) {
                    BookMeta meta = (BookMeta) item.getItemMeta();
                    if (meta != null) {
                        String bookTitle = meta.getTitle();
                        if (bookTitle == null) {
                            String skip = "";
                        } else if (bookTitle.contains("Secret Task")) {
                            inventory.remove(item);
                        }
                    }
                }
            }
        }
    }
}
