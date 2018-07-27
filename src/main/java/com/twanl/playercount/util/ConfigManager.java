package com.twanl.playercount.util;

import com.twanl.playercount.PlayerCount;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {


    private PlayerCount plugin = PlayerCount.getPlugin(PlayerCount.class);

    //Files & Config Files
    public FileConfiguration playersC;
    public File playersF;
    //--------------------


    public void setup() {
        playersF = new File(plugin.getDataFolder(), "PlayerData.yml");
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }


        if (!playersF.exists()) {
            try {
                playersF.createNewFile();
                Bukkit.getServer().getConsoleSender().sendMessage(Strings.green + "The PlayerData.yml file has been created");
            } catch (IOException e) {
                Bukkit.getServer().getConsoleSender().sendMessage(Strings.red + "Could not create the PlayerData.yml file");
            }
        }

        playersC = YamlConfiguration.loadConfiguration(playersF);



    }


    public FileConfiguration getPlayers() {
        return playersC;

    }





    public void savePlayers() {
        playersF = new File(plugin.getDataFolder(), "PlayerData.yml");

        try {
            playersC.save(playersF);
            Bukkit.getServer().getConsoleSender().sendMessage(Strings.green + "The PlayerData.yml file has been saved");

        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(Strings.red + "Could not save the PlayerData.yml file");

        }
    }


    public void reloadplayers() {
        playersC = YamlConfiguration.loadConfiguration(playersF);
        Bukkit.getServer().getConsoleSender().sendMessage(Strings.green + "The PlayerData.yml file has been reloaded");

    }






}
