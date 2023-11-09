package net.heckerdev.secretlife.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

public class PlayerDeathEventListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        Player player = event.getPlayer();
        Team team = board.getEntityTeam(player);
        if (team != null) {
            if (team.getName().equals("3-Lives")) {
                team.removeEntry(player.getName());
                Objects.requireNonNull(board.getTeam("2-Lives")).addEntity(player);
            } else if (team.getName().equals("2-Lives")) {
                team.removeEntry(player.getName());
                Objects.requireNonNull(board.getTeam("1-Life")).addEntity(player);
            } else if (team.getName().equals("1-Life")) {
                team.removeEntry(player.getName());
                Objects.requireNonNull(board.getTeam("Dead")).addEntity(player);
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        Player player = event.getPlayer();
        Team team = board.getEntityTeam(player);
        if (team != null) {
            if (team.getName().equals("2-Lives")) {
                player.sendMessage(Component.text("You have died, and are now on 2 lives!").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true));
                AttributeInstance health = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH));
                health.setBaseValue(60);
                player.setHealth(60);
            } else if (team.getName().equals("1-Life")) {
                player.sendMessage(Component.text("You have died, and are now on 1 life!").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true));
                AttributeInstance health = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH));
                health.setBaseValue(60);
                player.setHealth(60);
            } else if (team.getName().equals("Dead")) {
                player.sendMessage(Component.text("You are dead!").color(NamedTextColor.DARK_RED).decoration(TextDecoration.BOLD, true));
                Title title = Title.title(Component.text("You are dead!").color(NamedTextColor.DARK_RED).decoration(TextDecoration.BOLD, true), Component.empty());
                player.showTitle(title);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("SecretLife")), () -> {
                    player.setGameMode(GameMode.SPECTATOR);
                }, 1L);
            }
        }
    }
}
