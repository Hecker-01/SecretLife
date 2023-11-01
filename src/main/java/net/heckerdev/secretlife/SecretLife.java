package net.heckerdev.secretlife;

import co.aikar.commands.PaperCommandManager;
import net.heckerdev.secretlife.commands.*;
import net.heckerdev.secretlife.events.*;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class SecretLife extends JavaPlugin {

    private static Permission perms = null;

    @Override
    public void onEnable() {
        // Plugin startup logic.
        saveDefaultConfig();
        setupListeners();
        setupPermissions();
        setupCommands();
        getLogger().info("Successfully loaded SecretLife!");
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
    }

    public static Permission getPermissions() {
        return perms;
    }
}
