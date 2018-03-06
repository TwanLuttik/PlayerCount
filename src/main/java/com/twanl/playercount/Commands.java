package com.twanl.playercount;

import com.twanl.playercount.util.ConfigManager;
import com.twanl.playercount.util.Strings;
import net.minecraft.server.v1_12_R1.CommandExecute;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;


public class Commands extends CommandExecute implements Listener, CommandExecutor {

    private PlayerCount plugin = PlayerCount.getPlugin(PlayerCount.class);
    public ConfigManager cfgM;
    public File playersF;
    public File cF;





    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (!(sender instanceof Player)) {
            sender.sendMessage(Strings.logName + ChatColor.RED + "Only a player can execute commands!");
            return true;
        }




        if (cmd.getName().equalsIgnoreCase("pc")) {
            if (args.length == 0) {
                if (p.hasPermission("playercount.pc")) {
                    p.sendMessage(Strings.DgrayBIS + "-----------------------------\n" +
                            Strings.goldB + "       Playercount " + plugin.getDescription().getVersion() + "\n" +
                            Strings.DgrayBIS + "-----------------------------\n" +
                            Strings.gold + "              Commands\n" +
                            " \n" +
                            Strings.gold + "/pc " + Strings.white + "show this page.\n" +
                            Strings.gold + "/pc reload " + Strings.white + "reload the config file.\n" +
                            Strings.gold + "/pc count " + Strings.white + "counts how many players already joined.\n" +
                            Strings.gold + "/pc reset " + Strings.white + "reset the counter.\n" +
                            Strings.DgrayBIS + "-----------------------------");
                } else {
                    p.sendMessage(Strings.noPerm);
                }

            } else if (args[0].equalsIgnoreCase("reload")) {
                if (p.hasPermission("playercount.reload")) {
                    cF = new File(plugin.getDataFolder(), "config.yml");
                    if (!cF.exists()) {
                        p.sendMessage(Strings.red + "config.yml file is not found!\n" +
                                Strings.green + "Creating a new config.yml file!");
                        plugin.getConfig().options().copyDefaults(true);
                        plugin.saveConfig();
                    } else {
                        plugin.saveDefaultConfig();
                        plugin.reloadConfig();
                        p.sendMessage(Strings.green + "Config File Reloaded Succsesfully!");
                    }
                } else {
                    p.sendMessage(Strings.noPerm);
                }

            } else if (args[0].equalsIgnoreCase("count")) {
                if (p.hasPermission("playercount.count")) {
                    int counts = (int) plugin.getConfig().getInt("counts");
                    if (counts == 1) {
                        p.sendMessage(Strings.white + "There are currently " + Strings.goldB + String.valueOf(counts) + Strings.white + " players joined on this server!");
                    } else {
                        p.sendMessage(Strings.white + "There are currently " + Strings.goldB + String.valueOf(counts - 1) + Strings.white + " players joined on this server!");
                    }
                } else {
                    p.sendMessage(Strings.noPerm);
                }

            } else if (args[0].equalsIgnoreCase("reset")) {
                if (p.hasPermission("playercount.reset")) {
                    if (plugin.getConfig().getBoolean("use_custom_file")) {












                        playersF = new File(plugin.getDataFolder(), "PlayerData.yml");
                        if (playersF.exists()) {
                            playersF.delete();
                            try {
                                playersF.createNewFile();
                                plugin.getConfig().set("counts", 1);
                                plugin.saveConfig();
                                p.sendMessage(Strings.goldI + "Created succsesfully a new PlayerData.yml file!");
                            } catch (IOException e) {
                                e.printStackTrace();
                                p.sendMessage(Strings.redI + "failed to create a new PlayerData.yml file!");
                            }
                        }


                    } else {
                        p.sendMessage(Strings.redI + "This support only when use_custom_file is enabled!");
                    }

                } else {
                    p.sendMessage(Strings.noPerm);
                }



            }
        }


        return true;
    }




}
