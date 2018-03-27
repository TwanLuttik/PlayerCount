package com.twanl.playercount.events;


import com.twanl.playercount.PlayerCount;
import com.twanl.playercount.api.Api;
import com.twanl.playercount.util.ConfigManager;
import com.twanl.playercount.util.Strings;
import com.twanl.playercount.util.UpdateChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class JoinEvent implements Listener {


    private PlayerCount plugin = PlayerCount.getPlugin(PlayerCount.class);
    private UpdateChecker checker = new UpdateChecker(plugin);
    private Api api = new Api();
    public ConfigManager cfgM;



    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (plugin.getConfig().getBoolean("update_message")) {
            if (p.hasPermission("playercounts.update")) {

                if (checker.isConnected()) {
                    if (checker.hasUpdate()) {

                        p.sendMessage(Strings.DgrayBS + "----------------------\n");
                        plugin.nms.sendClickableHovarableMessageURL(p, Strings.red + "PlayerCounter is outdated!", Strings.gold + "Click to go to the download page", "https://www.spigotmc.org/resources/playercount.52758/");
                        p.sendMessage(" \n" +
                                Strings.white + "Your version: " + plugin.getDescription().getVersion() + "\n" +
                                Strings.white + "Newest version: " + Strings.green + this.checker.getLatestVersion() + "\n" +
                                Strings.DgrayBS + "----------------------");
                    } else {
                        p.sendMessage(Strings.DgrayBS + "----------------------\n" +
                        Strings.green + "PlayerCount is up to date.\n" +
                        Strings.DgrayBS + "----------------------");
                    }
                }
            }
        }



        if (!plugin.getConfig().getBoolean("use_custom_file")) {
            api.serverSide(p.getUniqueId());
        } else {
            api.fileSide(p.getUniqueId());
        }

    }



}