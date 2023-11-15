package net.heckerdev.secretlife.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.heckerdev.secretlife.SecretLife;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
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

                player.playSound(player,"minecraft:secretlife.secret", 1, 1);
                Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    inventory.setItemInMainHand(totem);
                    player.playEffect(EntityEffect.TOTEM_RESURRECT);
                    inventory.setItemInMainHand(mainHand);
                    player.stopSound(SoundStop.named(Key.key("item.totem.use")));
                }, 25);
                Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    player.playSound(player, "minecraft:entity.item.pickup", 1, 1);
                    giveSecret(player);
                }, 62);
                player.stopSound(SoundStop.named(Key.key("item.totem.use")));

            } else {
                noPerms(player);
            }
        } else {
            sender.sendMessage("You can only execute this as a player!");
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
    public TotemCommand(SecretLife plugin) {
        this.plugin = plugin;
    }

    private void giveSecret(Player player) {
        String[] secrets = this.plugin.getConfig().getStringList("secrets").toArray(new String[0]);
        int randomSecrets = (int) (Math.random() * secrets.length);
        Player[] players = player.getWorld().getPlayers().toArray(new Player[0]);
        int randomPlayers = (int) (Math.random() * players.length);
        String randomPlayer = players[randomPlayers].getName();
        String secret = secrets[randomSecrets].replace("{player}", randomPlayer);
        ItemStack secretBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) secretBook.getItemMeta();
        meta.setTitle("§b" + player.getName() + "'s Secret");
        meta.addPages(MiniMessage.miniMessage().deserialize(secret));
        meta.setAuthor("The Secret Keeper");
        meta.setGeneration(BookMeta.Generation.ORIGINAL);
        meta.setCustomModelData(this.plugin.getConfig().getInt("items.secret-book-custom-model-data"));
        secretBook.setItemMeta(meta);
        player.getInventory().addItem(secretBook);
    }
}
