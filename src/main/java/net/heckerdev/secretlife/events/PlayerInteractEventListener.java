package net.heckerdev.secretlife.events;

import net.heckerdev.secretlife.SecretLife;
import net.heckerdev.secretlife.utils.ParticleColor;
import net.heckerdev.secretlife.utils.TextColor;
import net.heckerdev.secretlife.buttons.*;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerInteractEventListener implements Listener {

    public void CompletedButton(Player player, Block block) {
        String CompletedMessage = this.plugin.getConfig().getString("messages.completed-button");

        // Send message to player when the completed button is clicked.
        // player.sendMessage(Component.text("✔ You clicked The Completed Button!").color(TextColor.GREEN).decoration(TextDecoration.BOLD, true));
        if (CompletedMessage != null) {
            player.sendMessage(LegacyComponentSerializer.legacy('&').deserialize(CompletedMessage));
        } else {
            player.sendMessage(Component.text("❌ Completed message isn't set up properly, please contact an admin!").color(TextColor.RED).decoration(TextDecoration.BOLD, true));
        }
        // The other stuff is in the file below.
        CompletedButton.Pressed(player, block);
    }

    public void FailedButton(Player player, Block block) {
        String FailedMessage = this.plugin.getConfig().getString("messages.failed-button");

        // Send message to player when the failed button is clicked.
        // player.sendMessage(Component.text("❌ You clicked The Failed Button!").color(TextColor.DARK_RED).decoration(TextDecoration.BOLD, true));
        if (FailedMessage != null) {
            player.sendMessage(LegacyComponentSerializer.legacy('&').deserialize(FailedMessage));
        } else {
            player.sendMessage(Component.text("❌ Failed message isn't set up properly, please contact an admin!").color(TextColor.RED).decoration(TextDecoration.BOLD, true));
        }
        // The other stuff is in the file below.
        FailedButton.Pressed(player, block);
    }

    public void InfoButton(Player player, Block block) {
        String InfoMessage = this.plugin.getConfig().getString("messages.info-button");

        // Send message to player when the info button is clicked.
        // player.sendMessage(Component.text("ⓘ You clicked The Info Button!").color(TextColor.BLUE).decoration(TextDecoration.BOLD, true));
        if (InfoMessage != null) {
            player.sendMessage(LegacyComponentSerializer.legacy('&').deserialize(InfoMessage));
        } else {
            player.sendMessage(Component.text("❌ Info message isn't set up properly, please contact an admin!").color(TextColor.RED).decoration(TextDecoration.BOLD, true));
        }
        // The other stuff is in the file below.
        InfoButton.Pressed(player, block);
    }

    public  void RerollButton(Player player, Block block) {
        String RerollMessage = this.plugin.getConfig().getString("messages.reroll-button");

        // Send message to player when the reroll button is clicked.
        // player.sendMessage(Component.text("\uD83D\uDD01").color(TextColor.DARK_GREEN).decoration(TextDecoration.BOLD, false).append(Component.text(" You clicked The Reroll Button!").color(TextColor.DARK_GREEN).decoration(TextDecoration.BOLD, true)));
        if (RerollMessage != null) {
            player.sendMessage(LegacyComponentSerializer.legacy('&').deserialize(RerollMessage));
        } else {
            player.sendMessage(Component.text("❌ Reroll message isn't set up properly, please contact an admin!").color(TextColor.RED).decoration(TextDecoration.BOLD, true));
        }
        // The other stuff is in the file below.
        RerollButton.Pressed(player, block);
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        Location location = block.getLocation();
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_BLOCK) {
            // Completed Button
            if (location.getBlockX() == 2 && location.getBlockY() == 66 && location.getBlockZ() == 3 && block.getType().toString().contains("BUTTON")) {

                long cooldownTime1 = 1000; // IN MILLISECONDS

                if (!this.cooldown1.containsKey(player.getUniqueId()) || ((this.cooldown1.get(player.getUniqueId())) + cooldownTime1) - (System.currentTimeMillis()) <= 0) {
                    this.cooldown1.put(player.getUniqueId(), System.currentTimeMillis());
                    CompletedButton(player, block);
                }
            // Re-roll Button
            } else if (location.getBlockX() == 0 && location.getBlockY() == 66 && location.getBlockZ() == 3 && block.getType().toString().contains("BUTTON")) {

                long cooldownTime2 = 1000; // IN MILLISECONDS

                if (!this.cooldown2.containsKey(player.getUniqueId()) || ((this.cooldown2.get(player.getUniqueId())) + cooldownTime2) - (System.currentTimeMillis()) <= 0) {
                    this.cooldown2.put(player.getUniqueId(), System.currentTimeMillis());
                    RerollButton(player, block);
                }
            // Failed Button
            } else if (location.getBlockX() == -2 && location.getBlockY() == 66 && location.getBlockZ() == 3 && block.getType().toString().contains("BUTTON")) {

                long cooldownTime3 = 1000; // IN MILLISECONDS

                if (!this.cooldown3.containsKey(player.getUniqueId()) || ((this.cooldown3.get(player.getUniqueId())) + cooldownTime3) - (System.currentTimeMillis()) <= 0) {
                    this.cooldown3.put(player.getUniqueId(), System.currentTimeMillis());
                    FailedButton(player, block);
                }
            // Info Button
            } else if (location.getBlockX() == 0 && location.getBlockY() == 66 && location.getBlockZ() == 7 && block.getType().toString().contains("BUTTON")) {

                long cooldownTime4 = 1000; // IN MILLISECONDS

                if (!this.cooldown4.containsKey(player.getUniqueId()) || ((this.cooldown4.get(player.getUniqueId())) + cooldownTime4) - (System.currentTimeMillis()) <= 0) {
                    this.cooldown4.put(player.getUniqueId(), System.currentTimeMillis());
                    InfoButton(player, block);
                }
            }
        }
    }
    private final SecretLife plugin;
    private final HashMap<UUID, Long> cooldown1;
    private final HashMap<UUID, Long> cooldown2;
    private final HashMap<UUID, Long> cooldown3;
    private final HashMap<UUID, Long> cooldown4;
    public PlayerInteractEventListener(SecretLife plugin) {
        this.plugin = plugin;
        this.cooldown1 = new HashMap<>();
        this.cooldown2 = new HashMap<>();
        this.cooldown3 = new HashMap<>();
        this.cooldown4 = new HashMap<>();
    }
}
