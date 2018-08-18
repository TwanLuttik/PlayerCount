package com.twanl.playercount.lib;

import com.twanl.playercount.PlayerCount;
import com.twanl.playercount.sql.SqlLib;
import com.twanl.playercount.util.ConfigManager;
import com.twanl.playercount.util.Strings;
import com.twanl.playercount.util.loadManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class Lib {

    private PlayerCount plugin = PlayerCount.getPlugin(PlayerCount.class);
    private ConfigManager config = new ConfigManager();
    private SqlLib sql = new SqlLib();


    public void firstJoin(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);

        if (loadManager.firstJoin_BroadCast()) {
            for (String w : loadManager.worlds()) {
                for (Player p1 : Bukkit.getWorld(w).getPlayers()) {
                    for (String key : loadManager.firstJoin_message()) {
                        if (key.contains("' '")) {
                            p1.sendMessage(" ");
                        }
                        p1.sendMessage(stringconvert(key, p));
                    }
                }
            }
        } else {
            if (playerInWorld(uuid)) {
                for (String key : loadManager.firstJoin_message()) {
                    if (key.contains("' '")) {
                        p.sendMessage(" ");
                    } else {
                        p.sendMessage(stringconvert(key, p));
                    }
                }
            }
        }


        if (loadManager.firstJoin_Title_BroadCast()) {
            if (!loadManager.firstJoin_Title().isEmpty() || !loadManager.firstJoin_SubTitle().isEmpty()) {
                for (String key : loadManager.worlds()) {
                    for (Player p1 : Bukkit.getWorld(key).getPlayers()) {
                        String title = Strings.translateColorCodes1(loadManager.firstJoin_Title()
                                .replace("{player}", p.getName())
                                .replace("{total}", sql.getTotalJoinedPlayers() + "")
                                .replace("{number}", sql.getPlayerNumber(uuid) + ""));

                        String subTitle = Strings.translateColorCodes1(loadManager.firstJoin_SubTitle()
                                .replace("{player}", p.getName())
                                .replace("{total}", sql.getTotalJoinedPlayers() + "")
                                .replace("{number}", sql.getPlayerNumber(uuid) + ""));

                        plugin.nms.sendTitleMessage(p1, title, subTitle, loadManager.firstJoin_TitleShow_Time() * 20);
                    }
                }
            }
        } else {
            if (playerInWorld(uuid)) {
                if (!loadManager.firstJoin_Title().isEmpty() || !loadManager.firstJoin_SubTitle().isEmpty()) {
                    String title = Strings.translateColorCodes1(loadManager.firstJoin_Title()
                            .replace("{player}", p.getName())
                            .replace("{total}", sql.getTotalJoinedPlayers() + "")
                            .replace("{number}", sql.getPlayerNumber(uuid) + ""));

                    String subTitle = Strings.translateColorCodes1(loadManager.firstJoin_SubTitle()
                            .replace("{player}", p.getName())
                            .replace("{total}", sql.getTotalJoinedPlayers() + "")
                            .replace("{number}", sql.getPlayerNumber(uuid) + ""));

                    plugin.nms.sendTitleMessage(p, title, subTitle, loadManager.firstJoin_TitleShow_Time() * 20);
                }
            }
        }
    }

    public void defaultJoin(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);

        if (loadManager.defaultJoin_BroadCast()) {
            for (String w : loadManager.worlds()) {
                for (Player p1 : Bukkit.getWorld(w).getPlayers()) {
                    for (String key : loadManager.defaultJoin_Message()) {
                        if (key.contains("' '")) {
                            p1.sendMessage(" ");
                        }
                        p1.sendMessage(stringconvert(key, p));
                    }
                }
            }
        } else {
            if (playerInWorld(uuid)) {
                for (String key : loadManager.defaultJoin_Message()) {
                    if (key.contains("' '")) {
                        p.sendMessage(" ");
                    } else {
                        p.sendMessage(stringconvert(key, p));
                    }
                }
            }
        }


        if (loadManager.defaultJoin_Title_BroadCast()) {
            if (!loadManager.defaultJoin_Title().isEmpty() || !loadManager.defaultJoin_SubTitle().isEmpty()) {
                for (String key : loadManager.worlds()) {
                    for (Player p1 : Bukkit.getWorld(key).getPlayers()) {
                        String title = Strings.translateColorCodes1(loadManager.defaultJoin_Title()
                                .replace("{player}", p.getName())
                                .replace("{total}", sql.getTotalJoinedPlayers() + "")
                                .replace("{number}", sql.getPlayerNumber(uuid) + ""));

                        String subTitle = Strings.translateColorCodes1(loadManager.defaultJoin_SubTitle()
                                .replace("{player}", p.getName())
                                .replace("{total}", sql.getTotalJoinedPlayers() + "")
                                .replace("{number}", sql.getPlayerNumber(uuid) + ""));

                        plugin.nms.sendTitleMessage(p1, title, subTitle, loadManager.defaultJoin_TitleShow_Time() * 20);
                    }
                }
            }
        } else {
            if (playerInWorld(uuid)) {
                if (!loadManager.defaultJoin_Title().isEmpty() || !loadManager.defaultJoin_SubTitle().isEmpty()) {
                    String title = Strings.translateColorCodes1(loadManager.defaultJoin_Title()
                            .replace("{player}", p.getName())
                            .replace("{total}", sql.getTotalJoinedPlayers() + "")
                            .replace("{number}", sql.getPlayerNumber(uuid) + ""));

                    String subTitle = Strings.translateColorCodes1(loadManager.defaultJoin_SubTitle()
                            .replace("{player}", p.getName())
                            .replace("{total}", sql.getTotalJoinedPlayers() + "")
                            .replace("{number}", sql.getPlayerNumber(uuid) + ""));

                    plugin.nms.sendTitleMessage(p, title, subTitle, loadManager.defaultJoin_TitleShow_Time() * 20);
                }
            }
        }
    }


    // check if player has played before
    public boolean playerHasPlayedBefore(UUID uuid) {
        if (sqlUse()) {
            return sql.playerExist(uuid);
        } else {
            config.setup();
            return config.getPlayers().contains(uuid.toString());
        }
    }


    // if player is in tha same world as in the config
    private boolean playerInWorld(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        return loadManager.worlds().contains(p.getWorld().getName());
    }

    public String stringconvert(String s, Player p) {
        return Strings.translateColorCodes1(s
                .replace("{player}", p.getName())
                .replace("{total}", getTotalJoinedPlayers() + "")
                .replace("{number}", getPlayerNumber(p.getUniqueId()) + ""));
    }


    // add the player to the database
    public void addPlayer(UUID uuid) {
        if (sqlUse()) {
            sql.addPlayer(uuid);
        } else {
            config.setup();
            config.getPlayers().set(uuid + ".place", getTotalJoinedPlayers() + 1);
            config.savePlayers();
        }
    }


    // check if sql is used
    public boolean sqlUse() {
        return loadManager.database().equals("sql");
    }


    // get the total joined players
    public Integer getTotalJoinedPlayers() {
        if (sqlUse()) {
            return sql.getTotalJoinedPlayers();
        } else {
            config.setup();

            int totalPlayers = 0;
            for (String key : config.getPlayers().getConfigurationSection("").getKeys(false)) {
                totalPlayers++;
            }
            return totalPlayers;
        }
    }


    // get the joined number from the player
    public Integer getPlayerNumber(UUID uuid) {
        if (sqlUse()) {
            return sql.getPlayerNumber(uuid);
        } else {
            config.setup();
            return config.getPlayers().getInt(uuid + ".place");
        }
    }


    public void recoverPlayers() {
        config.setup();

        File f = new File("server.properties");
        String ValueWas = getString("level-name", f);

        File folder = new File(plugin.getServer().getWorldContainer().getAbsolutePath() + "/" + ValueWas + "/playerdata/");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                UUID uuid = UUID.fromString(listOfFiles[i].getName().replace(".dat", ""));
                if (!playerHasPlayedBefore(uuid)) {
                    addPlayer(uuid);
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

        }
        return "";
    }


}
