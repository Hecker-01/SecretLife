package net.heckerdev.secretlife.events;

import net.heckerdev.secretlife.SecretLife;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
                String message = this.plugin.getConfig().getString("messages.player-respawn-2-lives");
                if (message == null) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> Messages aren't set up properly, please contact an admin!"));
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>But you now only have 2 lives left"));
                } else {
                    player.sendMessage(MiniMessage.miniMessage().deserialize(message));
                }
                AttributeInstance health = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH));
                health.setBaseValue(60);
                player.setHealth(60);
            } else if (team.getName().equals("1-Life")) {
                String message = this.plugin.getConfig().getString("messages.player-respawn-1-lif");
                if (message == null) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> Messages aren't set up properly, please contact an admin!"));
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>But you now only have 1 life left"));
                } else {
                    player.sendMessage(MiniMessage.miniMessage().deserialize(message));
                }
                AttributeInstance health = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH));
                health.setBaseValue(60);
                player.setHealth(60);
            } else if (team.getName().equals("Dead")) {
                String message = this.plugin.getConfig().getString("messages.player-respawn-no-lives");
                if (message == null) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> Messages aren't set up properly, please contact an admin!"));
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<dark_red>But you have died"));
                } else {
                    player.sendMessage(MiniMessage.miniMessage().deserialize(message));
                }
                Title title = Title.title(MiniMessage.miniMessage().deserialize("<dark_red><bold>You are Dead!</bold>"), Component.empty());
                player.showTitle(title);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("SecretLife")), () -> {
                    player.setGameMode(GameMode.SPECTATOR);
                }, 1L);
            }
        }
    }

    private final SecretLife plugin;
    public PlayerDeathEventListener(SecretLife plugin) {
        this.plugin = plugin;
    }
}
