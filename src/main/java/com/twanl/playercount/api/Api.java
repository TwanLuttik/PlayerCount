package com.twanl.playercount.api;

import com.twanl.playercount.PlayerCount;
import com.twanl.playercount.util.ConfigManager;
import com.twanl.playercount.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class Api {

    private PlayerCount plugin = PlayerCount.getPlugin(PlayerCount.class);
    private ConfigManager cfgM = new ConfigManager();



    public void fileSide (UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        cfgM.setup();

        String playerUUID = String.valueOf(uuid);

        // player not exist in file
        if (!cfgM.getPlayers().contains(playerUUID)) {
            int counts = cfgM.getPlayers().getInt("counts");
            cfgM.getPlayers().set("counts", counts +1);

            cfgM.getPlayers().set(playerUUID + ".place", counts +1);
            cfgM.savePlayers();


            // send the message
            firstTimeMessage(uuid);
        } else {
            defaultMessage(uuid);

        }
    }

    public void serverSide (UUID uuid) {
        cfgM.setup();

        Player p = Bukkit.getPlayer(uuid);
        int counts = cfgM.getPlayers().getInt("counts");

        if (!p.hasPlayedBefore()) {
            cfgM.getPlayers().set("counts", counts +1);
            cfgM.savePlayers();

            serverSideMessage(uuid);
        }

    }



    private void firstTimeMessage (UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);


        List<String> worlds = new ArrayList<String>();
        worlds = plugin.getConfig().getStringList("enable_worlds");

        List<String> list1 = plugin.getConfig().getStringList("first_join.text");
        String toFirstJoin = String.join("" + Strings.reset + "\n", list1);
        String replacedToFirstJoin = toFirstJoin
                .replace("{player}", p.getName())
                .replace("{total}", String.valueOf(cfgM.getPlayers().getInt("counts")))
                .replace("{number}", String.valueOf(cfgM.getPlayers().getInt(uuid + ".place")));


        //send the message
        if (plugin.getConfig().getBoolean("first_join.broadcast")) {
            for (Player p1 : Bukkit.getServer().getOnlinePlayers()) {
                if (worlds.contains(p1.getWorld().getName())) {
                    Strings.translateColorCodes(p1, replacedToFirstJoin);
                }
            }

        } else {
            Strings.translateColorCodes(p, replacedToFirstJoin);

        }

    }

    private void defaultMessage (UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);


        List<String> worlds = new ArrayList<String>();
        worlds = plugin.getConfig().getStringList("enable_worlds");

        List<String> list1 = plugin.getConfig().getStringList("default_join.text");
        String toDefaultJoin = String.join("" + Strings.reset + "\n", list1);

        String replacedToDefaultJoin = toDefaultJoin
                .replace("{player}", p.getName())
                .replace("{total}", String.valueOf(cfgM.getPlayers().getInt("counts")))
                .replace("{number}", String.valueOf(cfgM.getPlayers().getInt(uuid + ".place")));

        if (plugin.getConfig().getBoolean("default_join.use")) {
            if (plugin.getConfig().getBoolean("default_join.broadcast")) {
                for (Player p1 : Bukkit.getServer().getOnlinePlayers()) {
                    if (worlds.contains(p1.getWorld().getName())) {
                        Strings.translateColorCodes(p1, replacedToDefaultJoin);
                    }
                }
            } else {
                Strings.translateColorCodes(p, replacedToDefaultJoin);
            }
        }
    }

    private void serverSideMessage(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);

        List<String> worlds = new ArrayList<String>();
        worlds = plugin.getConfig().getStringList("enable_worlds");

        List<String> list1 = plugin.getConfig().getStringList("message.text");
        String message = String.join("" + Strings.reset + "\n", list1);

        String replacedMessageJoin = message
                .replace("{player}", p.getName())
                .replace("{number}", String.valueOf(cfgM.getPlayers().getInt("counts")));

        if (plugin.getConfig().getBoolean("message.use")) {
            if (plugin.getConfig().getBoolean("message.broadcast")) {
                for (Player p1 : Bukkit.getServer().getOnlinePlayers()) {
                    if (worlds.contains(p1.getWorld().getName())) {
                        Strings.translateColorCodes(p1, replacedMessageJoin);
                    }
                }
            } else {
                Strings.translateColorCodes(p, replacedMessageJoin);
            }
        }
    }



    public void recoverPlayers(Player p) {
        cfgM.setup();

        File f = new File("server.properties");
        String ValueWas = getString("level-name", f);

        File folder = new File(plugin.getServer().getWorldContainer().getAbsolutePath() + "/" + ValueWas + "/playerdata/");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String uuid = listOfFiles[i].getName().replace(".dat", "");
                //p.sendMessage(uuid);

                if (!cfgM.getPlayers().contains(uuid)) {
                    int counts = cfgM.getPlayers().getInt("counts");
                    cfgM.getPlayers().set("counts", counts +1);
                    cfgM.savePlayers();

                    cfgM.getPlayers().set(uuid + ".place", counts +1);
                    cfgM.savePlayers();
                }

            }
        }
    }

    private static String getString(String s, File f) {
        Properties pr = new Properties();
        try {
            FileInputStream in = new FileInputStream(f);
            pr.load(in);
            String string = pr.getProperty(s);
            return string;
        } catch (IOException e) {

        } return "";
    }








}
