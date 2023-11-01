package net.heckerdev.secretlife.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.heckerdev.secretlife.SecretLife;
import net.heckerdev.secretlife.utils.TextColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@CommandAlias("setup")
@Description("setup secretlife")
public class SetupCommand extends BaseCommand {

    @Subcommand("everyone")
    @Syntax("everyone")
    @CommandCompletion("")
    public void onEveryone(@NotNull CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("secretlife.command.setup")) {
                if (!(args.length == 0) && args[0].equalsIgnoreCase("confirm")) {
                        for (Player target : Bukkit.getOnlinePlayers()) {
                            setupPlayer(target);
                        }
                        player.sendMessage(Component.text("✔").color(TextColor.GREEN).decoration(TextDecoration.BOLD, true).append(Component.text(" Setup complete!").color(TextColor.GREEN).decoration(TextDecoration.BOLD, false)));
                } else {
                    player.sendMessage(Component.text("❌").color(TextColor.RED).decoration(TextDecoration.BOLD, true).append(Component.text(" Please first confirm that everyone is online then complete the setup by typing /setup everyone confirm!").color(TextColor.RED).decoration(TextDecoration.BOLD, false)));
                }
            } else {
                noPerms(player);
            }
        } else {
            sender.sendMessage(NamedTextColor.DARK_RED + "You can only execute this as a player!");
        }
    }

    @Subcommand("only")
    @Syntax("only <player>")
    @CommandCompletion("@players")
    public void onOnly(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("secretlife.command.setup")) {
                if (!(args.length == 0) && Bukkit.getPlayer(args[0]) != null) {
                    if (Bukkit.getPlayer(args[0]) != null) {
                        Player target = Objects.requireNonNull(Bukkit.getPlayer(args[0]));
                        setupPlayer(target);
                        player.sendMessage(Component.text("✔").color(TextColor.GREEN).decoration(TextDecoration.BOLD, true).append(Component.text(" Setup " + target.getName() + " successfully!").color(TextColor.GREEN).decoration(TextDecoration.BOLD, false)));
                    } else {
                        player.sendMessage(Component.text("❌").color(TextColor.RED).decoration(TextDecoration.BOLD, true).append(Component.text(" " + args[0] + " is not a valid player! - Make sure the player is online!").color(TextColor.RED).decoration(TextDecoration.BOLD, false)).append(Component.text(" Usage: /setup only <player>").color(TextColor.GRAY).decoration(TextDecoration.BOLD, false)));
                    }
                } else {
                    player.sendMessage(Component.text("❌").color(TextColor.RED).decoration(TextDecoration.BOLD, true).append(Component.text(" Usage: /setup only <player>").color(TextColor.RED).decoration(TextDecoration.BOLD, false)));
                }
            } else {
                noPerms(player);
            }
        } else {
            sender.sendMessage(NamedTextColor.DARK_RED + "You can only execute this as a player!");
        }
    }

    @HelpCommand
    public void doHelp(CommandSender sender) {
        sender.sendMessage(Component.text("Usage: /setup everyone OR only").color(TextColor.GRAY));
    }

    private void setupPlayer(Player target) {
        AttributeInstance health = Objects.requireNonNull(target.getAttribute(Attribute.GENERIC_MAX_HEALTH));
        health.setBaseValue(60);
        target.setHealth(60);
        target.sendMessage(Component.text("✔").color(TextColor.GREEN).decoration(TextDecoration.BOLD, true).append(Component.text(" You've been set up and are ready to go!").color(TextColor.GREEN).decoration(TextDecoration.BOLD, false)));
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
