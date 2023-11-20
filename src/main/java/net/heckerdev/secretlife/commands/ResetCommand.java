package net.heckerdev.secretlife.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.heckerdev.secretlife.SecretLife;
import net.heckerdev.secretlife.buttons.CompletedButton;
import net.heckerdev.secretlife.events.PlayerJoinEventListener;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
                        CompletedButton.removeTasks(target);
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

    private void noPerms(Player player) {
        String noPermsMessage = SecretLife.getPlugin().getConfig().getString("messages.command-no-permission");

        if (noPermsMessage != null) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(noPermsMessage));
        } else {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> Command messages aren't set up properly, please contact an admin!"));        }
    }
}
