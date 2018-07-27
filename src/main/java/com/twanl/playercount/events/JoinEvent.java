package com.twanl.playercount.events;


import com.twanl.playercount.PlayerCount;
import com.twanl.playercount.lib.Lib;
import com.twanl.playercount.sql.SqlLib;
import com.twanl.playercount.util.ConfigManager;
import com.twanl.playercount.util.Strings;
import com.twanl.playercount.util.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class JoinEvent implements Listener {


    private PlayerCount plugin = PlayerCount.getPlugin(PlayerCount.class);
    private UpdateChecker checker = new UpdateChecker(plugin);
    private Lib api = new Lib();
    public ConfigManager cfgM;


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();


        String pluginName = plugin.getDescription().getName();
        if (plugin.getConfig().getBoolean("update_message")) {
            if (p.hasPermission("playercounts.update")) {

                if (checker.isConnected()) {
                    if (checker.hasUpdate()) {

                        p.sendMessage(Strings.DgrayBS + "----------------------\n");
                        plugin.nms.sendClickableHovarableMessageURL(p, Strings.red + pluginName + " is outdated!", Strings.gold + "Click to go to the download page", "https://www.spigotmc.org/resources/playercount.52758/");
                        p.sendMessage(" \n" +
                                Strings.white + "Your version: " + plugin.getDescription().getVersion() + "\n" +
                                Strings.white + "Newest version: " + Strings.green + this.checker.getLatestVersion() + "\n" +
                                Strings.DgrayBS + "----------------------");
                    } else {
                        p.sendMessage(Strings.DgrayBS + "----------------------\n" +
                                Strings.green + pluginName + " is up to date.\n" +
                                Strings.DgrayBS + "----------------------");
                    }
                }
            }
        }


        // we put a 5tick delay for this becase else the player don't the join message
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {

                if (plugin.getConfig().get("database").equals("none")) {
//                    Bukkit.getConsoleSender().sendMessage(Strings.yellow + "none");
                    api.serverSide(p.getUniqueId());
                } else if (plugin.getConfig().get("database").equals("file")) {
//                    Bukkit.getConsoleSender().sendMessage(Strings.yellow + "file");
                    api.fileSide(p.getUniqueId());
                } else if (plugin.getConfig().get("database").equals("sql")) {
//                    Bukkit.getConsoleSender().sendMessage(Strings.yellow + "sql");
                    api.sql(p.getUniqueId());
                }



            }
        }, 5L);



    }


}