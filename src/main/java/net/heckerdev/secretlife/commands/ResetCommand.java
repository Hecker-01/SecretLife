package net.heckerdev.secretlife.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.heckerdev.secretlife.SecretLife;
import net.heckerdev.secretlife.events.PlayerJoinEventListener;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

@CommandAlias("reset")
@Description("reset someone to full health and lives.")
public class ResetCommand extends BaseCommand {

    @Default
    @Syntax(" <player>")
    @CommandCompletion("@players")
    public void onDefault(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("secretlife.command.reset")) {
                if (!(args.length == 0) && Bukkit.getPlayer(args[0]) != null) {
                    if (Bukkit.getPlayer(args[0]) != null) {
                        Player target = Objects.requireNonNull(Bukkit.getPlayer(args[0]));
                        removeTasks(target);
                        PlayerJoinEventListener.setupPlayer(target);
                        target.getPersistentDataContainer().set(new NamespacedKey(SecretLife.getPlugin(), "usedGift"), PersistentDataType.BOOLEAN, false);
                        player.getPersistentDataContainer().set(new NamespacedKey(SecretLife.getPlugin(), "secretDifficulty"), PersistentDataType.INTEGER, 0);
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<green><bold>✔</bold> Reset " + target.getName() + " successfully!"));
                    } else {
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> " + args[0] + " is not a valid player! - Make sure the player is online!<gray>  Usage: /reset <player>"));
                    }
                } else {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> Usage: /reset <player>"));
                }
            } else {
                noPerms(player);
            }
        } else {
            sender.sendMessage("You can only execute this as a player!");
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

    private void noPerms(Player player) {
        String noPermsMessage = this.plugin.getConfig().getString("messages.command-no-permission");

        if (noPermsMessage != null) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(noPermsMessage));
        } else {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> Command messages aren't set up properly, please contact an admin!"));        }
    }
    private final SecretLife plugin;
    public ResetCommand(SecretLife plugin) {
        this.plugin = plugin;
    }
}
