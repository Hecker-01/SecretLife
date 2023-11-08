package net.heckerdev.secretlife.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.heckerdev.secretlife.SecretLife;
import net.heckerdev.secretlife.events.PlayerJoinEventListener;
import net.heckerdev.secretlife.utils.TextColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

@CommandAlias("reset")
@Description("reset someone to full health and lives.")
public class SetupCommand extends BaseCommand {

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
                        PlayerJoinEventListener.setupPlayer(target);
                        player.sendMessage(Component.text("✔").color(TextColor.GREEN).decoration(TextDecoration.BOLD, true).append(Component.text(" Reset " + target.getName() + " successfully!").color(TextColor.GREEN).decoration(TextDecoration.BOLD, false)));
                    } else {
                        player.sendMessage(Component.text("❌").color(TextColor.RED).decoration(TextDecoration.BOLD, true).append(Component.text(" " + args[0] + " is not a valid player! - Make sure the player is online!").color(TextColor.RED).decoration(TextDecoration.BOLD, false)).append(Component.text(" Usage: /reset <player>").color(TextColor.GRAY).decoration(TextDecoration.BOLD, false)));
                    }
                } else {
                    player.sendMessage(Component.text("❌").color(TextColor.RED).decoration(TextDecoration.BOLD, true).append(Component.text(" Usage: /reset <player>").color(TextColor.RED).decoration(TextDecoration.BOLD, false)));
                }
            } else {
                noPerms(player);
            }
        } else {
            sender.sendMessage(NamedTextColor.DARK_RED + "You can only execute this as a player!");
        }
    }

    private void noPerms(Player player) {
        String noPermsMessage = this.plugin.getConfig().getString("messages.command-no-permission");

        if (noPermsMessage != null) {
            player.sendMessage(LegacyComponentSerializer.legacy('&').deserialize(noPermsMessage));
        } else {
            player.sendMessage(Component.text("❌").color(TextColor.RED).decoration(TextDecoration.BOLD, true).append(Component.text(" Command messages aren't set up properly, please contact an admin!").color(TextColor.RED).decoration(TextDecoration.BOLD, false)));
        }
    }
    private final SecretLife plugin;
    public SetupCommand(SecretLife plugin) {
        this.plugin = plugin;
    }
}
