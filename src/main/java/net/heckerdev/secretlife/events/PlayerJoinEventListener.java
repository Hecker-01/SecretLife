package net.heckerdev.secretlife.events;

import net.heckerdev.secretlife.SecretLife;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

public class PlayerJoinEventListener implements Listener {

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.getPersistentDataContainer().has(new NamespacedKey(SecretLife.getPlugin(), "beenSetup"), PersistentDataType.BOOLEAN)) {
            setupPlayer(player);
        }
        if (!player.getPersistentDataContainer().has(new NamespacedKey(SecretLife.getPlugin(), "usedGift"), PersistentDataType.BOOLEAN)) {
            player.getPersistentDataContainer().set(new NamespacedKey(SecretLife.getPlugin(), "usedGift"), PersistentDataType.BOOLEAN, false);
        }
    }

    public static void setupPlayer(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        AttributeInstance health = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH));
        Team threeLives = Objects.requireNonNull(board.getTeam("3-Lives"));
        threeLives.addEntity(player);
        health.setBaseValue(60);
        player.setHealth(60);
        player.setGameMode(GameMode.SURVIVAL);
        player.teleport(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation().add(0.5, 0, 0.5));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<green><bold>✔</bold> You've been set up and are ready to go!"));
        player.getPersistentDataContainer().set(new NamespacedKey(SecretLife.getPlugin(), "beenSetup"), PersistentDataType.BOOLEAN, true);
    }
}
