package com.twanl.playercount;

import com.twanl.playercount.events.JoinEvent;
import com.twanl.playercount.util.ConfigManager;
import com.twanl.playercount.util.Metrics;
import com.twanl.playercount.util.Strings;
import com.twanl.playercount.util.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;


public class PlayerCount extends JavaPlugin {

    public ConfigManager cfgM;
    private UpdateChecker checker;
    protected PluginDescriptionFile pdfFile = getDescription();
    private final String PluginVersionOn = ChatColor.GREEN + "(" + pdfFile.getVersion() + ")";
    private final String PluginVersionOff = ChatColor.RED + "(" + pdfFile.getVersion() + ")";


    public void onEnable() {

        this.checker = new UpdateChecker(this);
        if (this.checker.isConnected()) {
            if (this.checker.hasUpdate()) {
                getServer().getConsoleSender().sendMessage(Strings.green + "");
                getServer().getConsoleSender().sendMessage(Strings.green + "------------------------");
                getServer().getConsoleSender().sendMessage(Strings.red + "PlayerCount is outdated!");
                getServer().getConsoleSender().sendMessage(Strings.white + "Newest version: " + this.checker.getLatestVersion());
                getServer().getConsoleSender().sendMessage(Strings.white + "Your version: " + Strings.green + this.getDescription().getVersion());
                getServer().getConsoleSender().sendMessage("Please download the new version at https://www.spigotmc.org/resources/admincontrol-new-update./52758");
                getServer().getConsoleSender().sendMessage(Strings.green + "------------------------");
                getServer().getConsoleSender().sendMessage(Strings.green + "");
            } else {
                getServer().getConsoleSender().sendMessage(Strings.green + "");
                getServer().getConsoleSender().sendMessage(Strings.green + "---------------------------------");
                getServer().getConsoleSender().sendMessage(Strings.green + "PlayerCount is up to date.");
                getServer().getConsoleSender().sendMessage(Strings.green + "---------------------------------");
                getServer().getConsoleSender().sendMessage(Strings.green + "");
            }
        }

        Metrics metrics = new Metrics(this);
        loadConfigManager();
        LOAD();
        Bukkit.getConsoleSender().sendMessage(Strings.logName + ChatColor.RED + "Has been enabled " + PluginVersionOn);
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(Strings.logName + ChatColor.RED + "Has been disabled " + PluginVersionOff);
    }



    public void LOAD() {
        // Register listeners
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);

        // Register commands
        Commands commands = new Commands();
        getCommand("pc").setExecutor(commands);

        //LoadConfig
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public void loadConfigManager() {
        cfgM = new ConfigManager();
        cfgM.setup();
        cfgM.savePlayers();
        cfgM.reloadplayers();
    }


}
