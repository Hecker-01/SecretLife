package net.heckerdev.secretlife.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.heckerdev.secretlife.SecretLife;
import net.heckerdev.secretlife.utils.TextColor;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

@CommandAlias("totem")
@Description("Plays the totem animation.")
public class TotemCommand extends BaseCommand {

    @Default
    @Syntax("")
    @CommandCompletion("")
    public void onDefault(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("secretlife.command.totem")) {
                PlayerInventory inventory = player.getInventory();
                ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
                ItemMeta meta = totem.getItemMeta();
                meta.setCustomModelData(this.plugin.getConfig().getInt("items.totem-custom-model-data"));
                totem.setItemMeta(meta);

                ItemStack mainHand = inventory.getItemInMainHand();

                inventory.setItemInMainHand(totem);
                player.playEffect(EntityEffect.TOTEM_RESURRECT);
                player.stopSound(SoundStop.named(Key.key("item.totem.use")));
                player.playSound(player,"minecraft:secretlife.secret", 1, 1);
                inventory.setItemInMainHand(mainHand);
            } else {
                String noPermsMessage = this.plugin.getConfig().getString("messages.command-no-permission");

                if (noPermsMessage != null) {
                    player.sendMessage(LegacyComponentSerializer.legacy('&').deserialize(noPermsMessage));
                } else {
                    player.sendMessage(Component.text("❌").color(TextColor.RED).decoration(TextDecoration.BOLD, true).append(Component.text(" Command messages aren't set up properly, please contact an admin!").color(TextColor.RED).decoration(TextDecoration.BOLD, false)));
                }
            }
        } else {
            sender.sendMessage("You can only execute this as a player!");
        }
    }

    private final SecretLife plugin;
    public TotemCommand(SecretLife plugin) {
        this.plugin = plugin;
    }
}
