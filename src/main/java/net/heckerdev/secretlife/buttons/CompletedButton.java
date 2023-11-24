package net.heckerdev.secretlife.buttons;

import net.heckerdev.secretlife.SecretLife;
import net.heckerdev.secretlife.utils.ParticleColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionType;

import java.util.Objects;

public class CompletedButton{
    public static void Pressed(Player player, Block block) {
        int taskDifficulty = player.getPersistentDataContainer().getOrDefault(new NamespacedKey(SecretLife.getPlugin(), "secretDifficulty"), PersistentDataType.INTEGER, 0);
        if (taskDifficulty == 0) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> You don't have a secret task!"));
            return;
        }
        removeTasks(player);
        player.getPersistentDataContainer().set(new NamespacedKey(SecretLife.getPlugin(), "secretDifficulty"), PersistentDataType.INTEGER, 0);
        // Spawn particles
        Particle.DustOptions dustOptions = new Particle.DustOptions(ParticleColor.GREEN, 1.5F);
        block.getWorld().spawnParticle(Particle.REDSTONE, block.getLocation().add(0.5, 1.0, 0.5), 25, dustOptions);
        Location enchantParticleLocation = new Location(block.getWorld(), 0.5, 70.2, 11.5);
        Objects.requireNonNull(Bukkit.getWorld("world")).spawnParticle(Particle.ENCHANTMENT_TABLE, enchantParticleLocation, 5000, 0, 0, 0, 50);
        Bukkit.getScheduler().runTaskLater(SecretLife.getPlugin(), () -> player.playSound(block.getLocation(), "minecraft:secretlife.complete", 1, 1), 16);
        Bukkit.getScheduler().runTaskLater(SecretLife.getPlugin(), () -> {
            Particle.DustOptions completeDustOptions = new Particle.DustOptions(ParticleColor.GREEN, 2F);
            Location completeParticleLocation = new Location(block.getWorld(), 0.5, 69.0, 11.5);
            Objects.requireNonNull(Bukkit.getWorld("world")).spawnParticle(Particle.REDSTONE, completeParticleLocation, 25, completeDustOptions);


            // Give player rewards
            AttributeInstance health = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH));
            int currentHealth = (int) health.getValue();
            int openHealth = 60 - currentHealth;
            int reward = 1000;
            if (taskDifficulty == 1) {
                reward = 20;
            } else if (taskDifficulty == 2) {
                reward = 40;
            }
            int healthCheck = openHealth - reward;
            String completedTitle = SecretLife.getPlugin().getConfig().getString("messages.completed-title");
            if (healthCheck >= 0) {
                health.setBaseValue(currentHealth + reward);
                player.setHealth(currentHealth + reward);
                if (completedTitle != null) {
                    Title title = Title.title(MiniMessage.miniMessage().deserialize("<green><bold>+" + reward / 2 + "</bold> ❤"), MiniMessage.miniMessage().deserialize(completedTitle));
                    player.showTitle(title);
                } else {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> Completed message isn't set up properly, please contact an admin!"));
                    Title title = Title.title(MiniMessage.miniMessage().deserialize("<green><bold>+" + reward / 2 + "</bold> ❤"), Component.empty());
                    player.showTitle(title);
                }
            } else {
                //check if openHealth ends with .5
                String healthReward;
                int restReward;
                if (openHealth % 2 == 0) {
                    restReward = (reward - openHealth) / 2;
                    healthReward = String.valueOf(openHealth / 2);
                } else {
                    restReward = (reward - (openHealth + 1)) / 2;
                    healthReward = String.valueOf((openHealth + 1) / 2);
                }
                health.setBaseValue(60);
                player.setHealth(60);
                if (healthReward.equals("0")) {
                    if (completedTitle != null) {
                        Title title = Title.title(Component.empty(), MiniMessage.miniMessage().deserialize(completedTitle));
                        player.showTitle(title);
                    } else {
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> Completed message isn't set up properly, please contact an admin!"));
                    }
                } else {
                    if (completedTitle != null) {
                        Title title = Title.title(MiniMessage.miniMessage().deserialize("<green><bold>+" + healthReward + "</bold> ❤"), MiniMessage.miniMessage().deserialize(completedTitle));
                        player.showTitle(title);
                    } else {
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> Completed message isn't set up properly, please contact an admin!"));
                        Title title = Title.title(MiniMessage.miniMessage().deserialize("<green><bold>+" + healthReward + "</bold> ❤"), Component.empty());
                        player.showTitle(title);
                    }
                }
                if (restReward <= 0) {
                    String skip;
                } else {

                    ItemStack rewardItem1 = new ItemStack(Material.GOLDEN_APPLE);
                    rewardItem1.setAmount(2);

                    ItemStack rewardItem2 = new ItemStack(Material.EMERALD);

                    ItemStack rewardItem3 = new ItemStack(Material.POTION);
                    PotionMeta rewardItemMeta5 = (PotionMeta) rewardItem3.getItemMeta();
                    rewardItemMeta5.setBasePotionType(PotionType.SLOW_FALLING);
                    rewardItem3.setItemMeta(rewardItemMeta5);

                    ItemStack rewardItem4 = new ItemStack(Material.EXPERIENCE_BOTTLE);
                    rewardItem4.setAmount(5);

                    ItemStack rewardItem5 = new ItemStack(Material.DIAMOND);
                    rewardItem5.setAmount(2);

                    ItemStack rewardItem6 = new ItemStack(Material.IRON_BLOCK);
                    rewardItem6.setAmount(3);

                    ItemStack rewardItem7 = new ItemStack(Material.ENCHANTED_BOOK);
                    EnchantmentStorageMeta rewardItemMeta7 = (EnchantmentStorageMeta) rewardItem7.getItemMeta();
                    rewardItemMeta7.addEnchant(Enchantment.PROTECTION_FALL, 3, true);
                    rewardItem7.setItemMeta(rewardItemMeta7);

                    ItemStack rewardItem8 = new ItemStack(Material.ENCHANTED_BOOK);
                    EnchantmentStorageMeta rewardItemMeta8 = (EnchantmentStorageMeta) rewardItem8.getItemMeta();
                    rewardItemMeta8.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
                    rewardItemMeta8.removeStoredEnchant(Enchantment.PROTECTION_FALL);
                    rewardItem8.setItemMeta(rewardItemMeta8);

                    ItemStack rewardItem9 = new ItemStack(Material.ANCIENT_DEBRIS);

                    ItemStack rewardItem10 = new ItemStack(Material.POTION);
                    PotionMeta rewardItemMeta10 = (PotionMeta) rewardItem10.getItemMeta();
                    rewardItemMeta10.setBasePotionType(PotionType.INVISIBILITY);
                    rewardItem10.setItemMeta(rewardItemMeta10);

                    ItemStack rewardItem12 = new ItemStack(Material.BOOK);
                    rewardItem12.setAmount(3);

                    ItemStack rewardItem14 = new ItemStack(Material.SKELETON_HORSE_SPAWN_EGG);

                    ItemStack rewardItem15 = new ItemStack(Material.ZOMBIE_SPAWN_EGG);

                    ItemStack rewardItem16 = new ItemStack(Material.SPAWNER);

                    ItemStack rewardItem20 = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);

                    ItemStack[] rewardItems = new ItemStack[]{
                            rewardItem1,
                            rewardItem2,
                            rewardItem3,
                            rewardItem4,
                            rewardItem5,
                            rewardItem6,
                            rewardItem7,
                            rewardItem8,
                            rewardItem9,
                            rewardItem10,
                            rewardItem1,
                            rewardItem12,
                            rewardItem3,
                            rewardItem14,
                            rewardItem15,
                            rewardItem16,
                            rewardItem7,
                            rewardItem8,
                            rewardItem9,
                            rewardItem20,
                    };

                    for (int i = 0; i < restReward; i++) {
                        int finalI = i;
                        Bukkit.getScheduler().runTaskLater(SecretLife.getPlugin(), () -> {
                            block.getWorld().dropItem(completeParticleLocation, rewardItems[finalI]);
                            player.playSound(completeParticleLocation, "minecraft:entity.item.pickup", 1, 1);
                        }, 5L * i);
                    }
                }
            }
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
                            String skip;
                        } else if (bookTitle.contains("Secret Task")) {
                            inventory.remove(item);
                        }
                    }
                }
            }
        }
    }
}
