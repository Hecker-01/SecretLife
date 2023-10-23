package net.heckerdev.secretlife.events;

import net.heckerdev.secretlife.buttons.*;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerInteractEventListener implements Listener {

    private final HashMap<UUID, Long> cooldown1;
    private final HashMap<UUID, Long> cooldown2;
    private final HashMap<UUID, Long> cooldown3;
    private final HashMap<UUID, Long> cooldown4;
    public PlayerInteractEventListener() {
        this.cooldown1 = new HashMap<>();
        this.cooldown2 = new HashMap<>();
        this.cooldown3 = new HashMap<>();
        this.cooldown4 = new HashMap<>();
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
                    CompletedButton.Pressed(player, block);
                }
            // Re-roll Button
            } else if (location.getBlockX() == 0 && location.getBlockY() == 66 && location.getBlockZ() == 3 && block.getType().toString().contains("BUTTON")) {

                long cooldownTime2 = 1000; // IN MILLISECONDS

                if (!this.cooldown2.containsKey(player.getUniqueId()) || ((this.cooldown2.get(player.getUniqueId())) + cooldownTime2) - (System.currentTimeMillis()) <= 0) {
                    this.cooldown2.put(player.getUniqueId(), System.currentTimeMillis());
                    RerollButton.Pressed(player, block);
                }
            // Failed Button
            } else if (location.getBlockX() == -2 && location.getBlockY() == 66 && location.getBlockZ() == 3 && block.getType().toString().contains("BUTTON")) {

                long cooldownTime3 = 1000; // IN MILLISECONDS

                if (!this.cooldown3.containsKey(player.getUniqueId()) || ((this.cooldown3.get(player.getUniqueId())) + cooldownTime3) - (System.currentTimeMillis()) <= 0) {
                    this.cooldown3.put(player.getUniqueId(), System.currentTimeMillis());
                    FailedButton.Pressed(player, block);
                }
            // Info Button
            } else if (location.getBlockX() == 0 && location.getBlockY() == 66 && location.getBlockZ() == 7 && block.getType().toString().contains("BUTTON")) {

                long cooldownTime4 = 1000; // IN MILLISECONDS

                if (!this.cooldown4.containsKey(player.getUniqueId()) || ((this.cooldown4.get(player.getUniqueId())) + cooldownTime4) - (System.currentTimeMillis()) <= 0) {
                    this.cooldown4.put(player.getUniqueId(), System.currentTimeMillis());
                    InfoButton.Pressed(player, block);
                }
            }
        }
    }
}
