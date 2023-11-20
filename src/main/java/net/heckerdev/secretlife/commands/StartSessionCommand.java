package net.heckerdev.secretlife.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.heckerdev.secretlife.SecretLife;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;


@CommandAlias("startsession")
@Description("starts the session.")
public class StartSessionCommand extends BaseCommand {

    @Default
    @Syntax("")
    @CommandCompletion("")
    public void onDefault(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("secretlife.command.startsession")) {
                Player[] initialPlayers = Bukkit.getOnlinePlayers().toArray(new Player[0]);
                for (Player initialPlayer : initialPlayers) {
                    initialPlayer.getPersistentDataContainer().set(new NamespacedKey(SecretLife.getPlugin(), "usedGift"), PersistentDataType.BOOLEAN, false);
                }
                Bukkit.getScheduler().runTaskLater(SecretLife.getPlugin(), () -> {
                    Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
                    for (Player p : players) {
                        ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
                        ItemMeta meta = totem.getItemMeta();
                        meta.setCustomModelData(SecretLife.getPlugin().getConfig().getInt("items.secret-totem-custom-model-data"));
                        totem.setItemMeta(meta);

                        p.playSound(p, "minecraft:secretlife.secret", 1, 1);
                        Bukkit.getScheduler().runTaskLater(SecretLife.getPlugin(), () -> {
                            PlayerInventory inventory = p.getInventory();
                            ItemStack mainHand = inventory.getItemInMainHand();
                            inventory.setItemInMainHand(totem);
                            p.playEffect(EntityEffect.TOTEM_RESURRECT);
                            inventory.setItemInMainHand(mainHand);
                        }, 25);
                        Bukkit.getScheduler().runTaskLater(SecretLife.getPlugin(), () -> {
                            p.playSound(p, "minecraft:entity.item.pickup", 1, 1);
                            giveSecret(p);
                            p.getPersistentDataContainer().set(new NamespacedKey(SecretLife.getPlugin(), "secretDifficulty"), PersistentDataType.INTEGER, 1);
                        }, 62);
                    }
                }, 60);
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

    private void giveSecret(Player player) {
        String[] secrets = SecretLife.getPlugin().getConfig().getStringList("secrets").toArray(new String[0]);
        int randomSecrets = (int) (Math.random() * secrets.length);
        Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        int randomPlayers = (int) (Math.random() * players.length);
        String randomPlayer = players[randomPlayers].getName();
        String secret = secrets[randomSecrets].replace("{player}", randomPlayer);
        ItemStack secretBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) secretBook.getItemMeta();
        meta.setTitle("§c" + player.getName() + "'s Secret Task");
        meta.addPages(MiniMessage.miniMessage().deserialize(secret));
        meta.setAuthor("The Secret Keeper");
        meta.setGeneration(BookMeta.Generation.ORIGINAL);
        meta.setCustomModelData(SecretLife.getPlugin().getConfig().getInt("items.secret-book-custom-model-data"));
        secretBook.setItemMeta(meta);
        player.getInventory().addItem(secretBook);
    }
}
