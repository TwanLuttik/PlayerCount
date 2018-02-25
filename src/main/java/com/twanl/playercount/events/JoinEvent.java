package com.twanl.playercount.events;

import com.twanl.playercount.PlayerCount;
import com.twanl.playercount.util.ConfigManager;
import com.twanl.playercount.util.Strings;
import com.twanl.playercount.util.UpdateChecker;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class JoinEvent implements Listener {


    private PlayerCount plugin = PlayerCount.getPlugin(PlayerCount.class);
    public ConfigManager cfgM;


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();


        if (plugin.getConfig().getBoolean("update_message")) {
            if (p.hasPermission("playercounts.update")) {
                final Player p1 = e.getPlayer();
                final PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
                final PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"PlayerCount is outdated!\",\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://www.spigotmc.org/resources/playercount.52758/download?version=208548\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Click to download the newest version of PlayerCount\",\"color\":\"gold\"}]}}}"));

                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public UpdateChecker checker;

                    public void run() {
                        this.checker = new UpdateChecker(plugin);

                        if (this.checker.isConnected()) {
                            if (this.checker.hasUpdate()) {
                                p1.sendMessage(Strings.DgrayBS + "----------------------\n");
                                connection.sendPacket(packet);
                                p1.sendMessage(" \n" +
                                        Strings.white + "Your version: " + plugin.getDescription().getVersion() + "\n" +
                                        Strings.white + "Newest version: " + Strings.green + this.checker.getLatestVersion() + "\n" +
                                        Strings.DgrayBS + "----------------------");
                            } else {
                                p1.sendMessage(Strings.DgrayBS + "----------------------\n" +
                                        Strings.green + "PlayerCount is up to date.\n" +
                                        Strings.DgrayBS + "----------------------");
                            }
                        }
                    }
                }, 20);

            }
        }



        cfgM = new ConfigManager();
        cfgM.setup();

        UUID uuid = p.getUniqueId();
        int counts = plugin.getConfig().getInt("counts");

        List<String> worlds = new ArrayList<String>();
        worlds = plugin.getConfig().getStringList("enable_worlds");

        List<String> list1 = plugin.getConfig().getStringList("first_join.text");
        List<String> list2 = plugin.getConfig().getStringList("default_join.text");
        String toFirstJoin = String.join(""+ Strings.reset + "\n", list1);
        String toDefaultJoin = String.join(""+ Strings.reset + "\n", list2);

        /*
        String toFirstJoin = plugin.getConfig().getString("first_join.text");
        String toDefaultJoin = plugin.getConfig().getString("default_join.text");
        */


        String replacedToFirstJoin = toFirstJoin
                .replace("{player}", p.getName())
                .replace("{total}", String.valueOf(plugin.getConfig().getInt("counts")))
                .replace("{number}", String.valueOf(cfgM.getPlayers().getInt("uuid." + uuid + ".place")));
        String replacedToDefaultJoin = toDefaultJoin
                .replace("{player}", p.getName())
                .replace("{total}", String.valueOf(plugin.getConfig().getInt("counts")))
                .replace("{number}", String.valueOf(cfgM.getPlayers().getInt("uuid." + uuid + ".place")));




        if (!plugin.getConfig().getBoolean("use_custom_file")) {

            if (!p.hasPlayedBefore()) {
                plugin.getConfig().set("counts", counts + 1);
                plugin.saveConfig();

                if (plugin.getConfig().getBoolean("first_join.broadcast")) {
                    for (Player p1 : Bukkit.getServer().getOnlinePlayers()) {
                        if (worlds.contains(p1.getWorld().getName())) {
                            p1.sendMessage(ChatColor.translateAlternateColorCodes('&', replacedToFirstJoin));
                        }
                    }
                } else {

                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', replacedToFirstJoin));
                }
            }


        } else {
            if (!cfgM.getPlayers().contains("uuid." + uuid)) {

                cfgM.getPlayers().set("uuid." + uuid + ".name", p.getName());
                cfgM.savePlayers();


                //set the place for the player
                if (cfgM.getPlayers().contains("uuid." + uuid)) {
                    cfgM.getPlayers().set("uuid." + uuid + ".place", plugin.getConfig().getInt("counts"));
                    cfgM.savePlayers();
                }

                //send the message
                if (plugin.getConfig().getBoolean("first_join.broadcast")) {
                    for (Player p1 : Bukkit.getServer().getOnlinePlayers()) {
                        if (worlds.contains(p1.getWorld().getName())) {
                            p1.sendMessage(ChatColor.translateAlternateColorCodes('&', replacedToFirstJoin));
                        }
                    }

                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', replacedToFirstJoin));

                }

                plugin.getConfig().set("counts", counts + 1);
                plugin.saveConfig();


            } else {

                if (plugin.getConfig().getBoolean("default_join.use")) {
                    if (cfgM.getPlayers().contains("uuid." + uuid)) {

                        if (plugin.getConfig().getBoolean("default_join.broadcast")) {

                            for (Player p1 : Bukkit.getServer().getOnlinePlayers()) {
                                if (worlds.contains(p1.getWorld().getName())) {
                                    p1.sendMessage(ChatColor.translateAlternateColorCodes('&', replacedToDefaultJoin));

                                }
                            }


                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', replacedToDefaultJoin));
                        }
                    }
                }
            }



        }
    }
}