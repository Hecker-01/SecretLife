package net.heckerdev.secretlife.events;

import net.heckerdev.secretlife.SecretLife;
import net.heckerdev.secretlife.utils.TextColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
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
    }

    public static void setupPlayer(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        AttributeInstance health = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH));
        Team team = Objects.requireNonNull(board.getTeam("3-Lives"));
        team.addEntity(player);
        health.setBaseValue(60);
        player.setHealth(60);
        player.sendMessage(Component.text("✔").color(TextColor.GREEN).decoration(TextDecoration.BOLD, true).append(Component.text(" You've been set up and are ready to go!").color(TextColor.GREEN).decoration(TextDecoration.BOLD, false)));
        player.getPersistentDataContainer().set(new NamespacedKey(SecretLife.getPlugin(), "beenSetup"), PersistentDataType.BOOLEAN, true);
    }
}
