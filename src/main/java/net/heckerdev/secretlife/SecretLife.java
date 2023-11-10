package net.heckerdev.secretlife;

import co.aikar.commands.PaperCommandManager;
import net.heckerdev.secretlife.commands.*;
import net.heckerdev.secretlife.events.*;
import net.kyori.adventure.text.format.NamedTextColor;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public final class SecretLife extends JavaPlugin {

    private static SecretLife plugin;

    private static Permission perms = null;

    @Override
    public void onEnable() {
        // Plugin startup logic.
        setupTeams();
        saveDefaultConfig();
        setupListeners();
        setupPermissions();
        setupCommands();
        getLogger().info("Successfully loaded SecretLife!");
        plugin = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("SecretLife disabled!");
    }

    private void setupCommands() {
        // Registering commands.
        PaperCommandManager manager= new PaperCommandManager(this);
        manager.registerCommand(new ResetCommand(this));
        manager.registerCommand(new TotemCommand(this));
    }

    private boolean setupPermissions() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().warning(" - Disabled because Vault is not installed!");
            getServer().getPluginManager().disablePlugin(this);
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            return false;
        }
        perms = rsp.getProvider();
        return perms != null;
    }

    private void setupListeners() {
        // Registering listeners.
        Bukkit.getPluginManager().registerEvents(new PlayerInteractEventListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathEventListener(), this);
    }

    private void setupTeams() {
        // Registering teams.
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();

        if (board.getTeam("3-Lives") == null) {
            Team threeLives = board.registerNewTeam("3-Lives");
            threeLives.color(NamedTextColor.GREEN);
            threeLives.setCanSeeFriendlyInvisibles(false);
        }
        if (board.getTeam("2-Lives") == null) {
            Team twoLives = board.registerNewTeam("2-Lives");
            twoLives.color(NamedTextColor.YELLOW);
            twoLives.setCanSeeFriendlyInvisibles(false);
        }
        if (board.getTeam("1-Life") == null) {
            Team oneLife = board.registerNewTeam("1-Life");
            oneLife.color(NamedTextColor.RED);
            oneLife.setCanSeeFriendlyInvisibles(false);
        }
        if (board.getTeam("Dead") == null) {
            Team dead = board.registerNewTeam("Dead");
            dead.color(NamedTextColor.DARK_GRAY);
            dead.setCanSeeFriendlyInvisibles(true);
        }
    }

    public static SecretLife getPlugin() {
        return plugin;
    }

    public static Permission getPermissions() {
        return perms;
    }
}
