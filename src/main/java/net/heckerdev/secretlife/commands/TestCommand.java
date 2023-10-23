package net.heckerdev.secretlife.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

@CommandAlias("test")
@Description("test")
public class TestCommand extends BaseCommand {

    private final HashMap<UUID, Long> cooldown;
    public TestCommand() {
        this.cooldown = new HashMap<>();
    }

    @Default
    @Syntax("")
    @CommandCompletion("")
    public void onDefault(@NotNull CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            long cooldownTime = 20; // IN TICKS (20 ticks = 1 second)

            if (!this.cooldown.containsKey(player.getUniqueId()) || ((this.cooldown.get(player.getUniqueId())) + cooldownTime * 50) - (System.currentTimeMillis()) <= 0) {
                this.cooldown.put(player.getUniqueId(), System.currentTimeMillis());
                sender.sendMessage("test");
            } else {
                long timeLeft = ((this.cooldown.get(player.getUniqueId())) + cooldownTime * 50) - (System.currentTimeMillis());
                if (timeLeft > 0) {
                    player.sendMessage(NamedTextColor.RED.toString() + TextDecoration.BOLD + "You must wait " + timeLeft / 50 + " milliseconds before using this command again!");
                }
            }
        } else {
            sender.sendMessage(NamedTextColor.DARK_RED + "You can only execute this as a player!");
        }
    }
}
