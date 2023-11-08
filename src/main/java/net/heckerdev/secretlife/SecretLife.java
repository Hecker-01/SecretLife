package net.heckerdev.secretlife;

import co.aikar.commands.PaperCommandManager;
import net.heckerdev.secretlife.commands.*;
import net.heckerdev.secretlife.events.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
        manager.registerCommand(new TestCommand(this));
        manager.registerCommand(new SetupCommand(this));
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
    }

    private void setupTeams() {
        // Registering teams.
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();

        Team threeLives = board.registerNewTeam("3-Lives");
        Team twoLives = board.registerNewTeam("2-Lives");
        Team oneLife = board.registerNewTeam("1-life");

        threeLives.color(NamedTextColor.GREEN);
        twoLives.color(NamedTextColor.YELLOW);
        oneLife.color(NamedTextColor.RED);

        threeLives.prefix(Component.text("[3] ").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, false));
        twoLives.prefix(Component.text("[2] ").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, false));
        oneLife.prefix(Component.text("[1] ").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, false));
    }

    public static SecretLife getPlugin() {
        return plugin;
    }

    public static Permission getPermissions() {
        return perms;
    }
}
