package net.heckerdev.secretlife.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.heckerdev.secretlife.SecretLife;
import net.heckerdev.secretlife.utils.TextColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandAlias("test")
@Description("test")
public class TestCommand extends BaseCommand {
    @Default
    @Syntax("")
    @CommandCompletion("")
    public void onDefault(@NotNull CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("secretlife.command.spawn")) {
                player.sendMessage("test complete!");
            } else {
                String CompletedMessage = this.plugin.getConfig().getString("messages.command-no-permission");

                if (CompletedMessage != null) {
                    player.sendMessage(LegacyComponentSerializer.legacy('&').deserialize(CompletedMessage));
                } else {
                    player.sendMessage(Component.text("❌ Command messages aren't set up properly, please contact an admin!").color(TextColor.RED).decoration(TextDecoration.BOLD, true));
                }
            }
        } else {
            sender.sendMessage(NamedTextColor.DARK_RED + "You can only execute this as a player!");
        }
    }
    private final SecretLife plugin;
    public TestCommand(SecretLife plugin) {
        this.plugin = plugin;
    }
}
