package net.heckerdev.secretlife.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.heckerdev.secretlife.SecretLife;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

@CommandAlias("gift")
@Description("Gift someone a heart (only once per session).")
public class GiftCommand extends BaseCommand {

    @Default
    @Syntax(" <player>")
    @CommandCompletion("@players")
    public void onDefault(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Boolean usedGift = player.getPersistentDataContainer().getOrDefault(new NamespacedKey(SecretLife.getPlugin(), "usedGift"), PersistentDataType.BOOLEAN, false);
            if (!usedGift) {
                if (!(args.length == 0) && Bukkit.getPlayer(args[0]) != null) {
                    if (Bukkit.getPlayer(args[0]) != null) {
                        if (Objects.equals(args[0], player.getName())) {
                            player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> You can't gift yourself!"));
                        } else {
                            Player target = Objects.requireNonNull(Bukkit.getPlayer(args[0]));
                            player.sendMessage(MiniMessage.miniMessage().deserialize("<green><bold>✔</bold> Successfully gave " + target.getName() + " a heart!"));
                            AttributeInstance health = Objects.requireNonNull(target.getAttribute(Attribute.GENERIC_MAX_HEALTH));
                            double currentHealth = health.getValue();
                            if (currentHealth <= 30) {
                                health.setBaseValue(currentHealth + 2);
                                target.setHealth(currentHealth + 2);
                            }
                            target.playSound(target.getLocation(), "minecraft:entity.player.levelup", 1, 1);
                            ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
                            ItemMeta totemItemMeta = totem.getItemMeta();
                            totemItemMeta.setCustomModelData(SecretLife.getPlugin().getConfig().getInt("items.heart-totem-custom-model-data"));
                            totem.setItemMeta(totemItemMeta);
                            PlayerInventory inventory = target.getInventory();
                            ItemStack mainHand = inventory.getItemInMainHand();
                            inventory.setItemInMainHand(totem);
                            target.playEffect(EntityEffect.TOTEM_RESURRECT);
                            inventory.setItemInMainHand(mainHand);
                            Title title = Title.title(MiniMessage.miniMessage().deserialize("<green><bold>✔</bold> " + player.getName() + " gave you a heart!"), Component.empty());
                            target.showTitle(title);
                            player.getPersistentDataContainer().set(new NamespacedKey(SecretLife.getPlugin(), "usedGift"), PersistentDataType.BOOLEAN, true);
                        }
                    } else {
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> " + args[0] + " is not a valid player! - Make sure the player is online!<gray>  Usage: /reset <player>"));
                    }
                } else {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> Usage: /gift <player>"));
                }
            } else {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> You've already used your gift for this session!"));
            }
        } else {
            sender.sendMessage("You can only execute this as a player!");
        }
    }
}
