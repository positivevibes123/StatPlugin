package me.damor.statplugin;

import me.damor.statplugin.commands.StatDisableCommand;
import me.damor.statplugin.commands.StatEnableCommand;
import me.damor.statplugin.commands.StatGetCommand;
import me.damor.statplugin.commands.StatResetCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class StatPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        // If data folder that stores player stats doesn't exist, then create it
        if (!getDataFolder().exists()){
            getLogger().info("Data folder doesn't exist, creating one now...");

            if (getDataFolder().mkdir()){
                getLogger().info("Data folder successfully created!");
            } else {
                getLogger().severe("Data folder could not be created!");
            }
        }

        // Register player listener
        PlayerManager playerManager = new PlayerManager(this);
        getServer().getPluginManager().registerEvents(new PlayerListener(playerManager), this);

        // Register commands
        getCommand("statget").setExecutor(new StatGetCommand(playerManager));
        getCommand("statreset").setExecutor(new StatResetCommand(playerManager));
        getCommand("statenable").setExecutor(new StatEnableCommand(playerManager));
        getCommand("statdisable").setExecutor(new StatDisableCommand(playerManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
