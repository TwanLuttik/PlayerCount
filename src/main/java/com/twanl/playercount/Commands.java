package com.twanl.playercount;

import com.twanl.playercount.lib.Lib;
import com.twanl.playercount.sql.SqlLib;
import com.twanl.playercount.util.ConfigManager;
import com.twanl.playercount.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public  class Commands implements CommandExecutor, TabCompleter {

    private PlayerCount plugin = PlayerCount.getPlugin(PlayerCount.class);
    private ConfigManager cfgM = new ConfigManager();
    private Lib api = new Lib();
    private SqlLib sql = new SqlLib();


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Strings.logName + ChatColor.RED + "Only a player can execute commands!");
            return true;
        }

        Player p = (Player) sender;
        cfgM.setup();




        if (cmd.getName().equalsIgnoreCase("playercount")) {
            if (args.length == 0) {
                if (p.hasPermission("playercount")) {
                    p.sendMessage(Strings.DgrayBIS + "-----------------------------\n" +
                            Strings.goldB + "       Playercount " + plugin.getDescription().getVersion() + "\n" +
                            Strings.DgrayBIS + "-----------------------------\n" +
                            Strings.gold + "              Commands\n" +
                            " \n" +
                            Strings.gold + "/pc " + Strings.white + "show this page.\n" +
                            Strings.gold + "/pc reload " + Strings.white + "reload the config file.\n" +
                            Strings.gold + "/pc count " + Strings.white + "counts how many players already joined.\n" +
                            Strings.gold + "/pc reset " + Strings.white + "reset the counter.\n" +
                            Strings.gold + "/pc recover " + Strings.white + "recover already joined players\n" +
                            Strings.DgrayBIS + "-----------------------------");
                }

            } else if (args[0].equalsIgnoreCase("reload")) {
                if (p.hasPermission("playercount.reload")) {


                    plugin.saveDefaultConfig();
                    plugin.reloadConfig();

                    cfgM.savePlayers();
                    cfgM.reloadplayers();


                    p.sendMessage(Strings.prefix + Strings.green + "Config File Reloaded Succsesfully!");
                    /*
                    File cF = new File(plugin.getDataFolder(), "config.yml");
                    if (!cF.exists()) {
                        p.sendMessage(Strings.red + "config.yml file is not found!\n" +
                                Strings.green + "Creating a new config.yml file!");
                        plugin.saveDefaultConfig();
                    } else {
                        plugin.saveDefaultConfig();
                        p.sendMessage(Strings.green + "Config File Reloaded Succsesfully!");
                    }
                    */



                }

            } else if (args[0].equalsIgnoreCase("count")) {
                if (p.hasPermission("playercount.count")) {

                    if (plugin.getConfig().get("database").equals("none")) {

                    } else if (plugin.getConfig().get("database").equals("file")) {
                        int counts = cfgM.getPlayers().getInt("counts");
                        if (counts == 1) {
                            p.sendMessage(Strings.prefix + Strings.white + "There are currently " + Strings.goldB + String.valueOf(counts) + Strings.white + " players joined on this server!");
                        } else {
                            p.sendMessage(Strings.prefix + Strings.white + "There are currently " + Strings.goldB + String.valueOf(counts -1) + Strings.white + " players joined on this server!");
                        }
                    } else if (plugin.getConfig().get("database").equals("sql")) {
                        p.sendMessage(Strings.prefix + Strings.white + "There are currently " + Strings.goldB + sql.getTotalJoinedPlayers() + Strings.white + " players joined on this server!");
                        return true;
                    }


                }

            } else if (args[0].equalsIgnoreCase("reset")) {
                if (p.hasPermission("playercount.reset")) {

                    if (plugin.getConfig().get("database").equals("none")) {
                        p.sendMessage(Strings.prefix + Strings.redI + "This support only when use_custom_file is enabled!");
                        return true;
                    } else if (plugin.getConfig().get("database").equals("file")) {
                        File playersF = new File(plugin.getDataFolder(), "PlayerData.yml");
                        if (playersF.exists()) {
                            playersF.delete();
                            try {
                                playersF.createNewFile();

                                p.sendMessage(Strings.prefix + Strings.green + "Created succsesfully a new PlayerData.yml file!");
                            } catch (IOException e) {
                                e.printStackTrace();
                                p.sendMessage(Strings.prefix + Strings.redI + "failed to create a new PlayerData.yml file!");
                            }
                        }
                    } else if (plugin.getConfig().get("database").equals("sql")) {
                        sql.clearTable();
                        p.sendMessage(Strings.prefix + Strings.green + "DataBase has been cleared!");
                        return true;
                    }

                }

            } else if (args[0].equalsIgnoreCase("recover")) {
                if (args.length == 1) {



                    if (p.hasPermission("playercount.recover")) {
                        if (plugin.getConfig().get("database").equals("none")) {
                            p.sendMessage(Strings.prefix + Strings.red + "this is only compatible with file!");
                            return true;
                        } else if (plugin.getConfig().get("database").equals("file")) {
                            p.sendMessage(Strings.prefix + Strings.green + "do /pc recover confirm");
                            confirmCommand.put("1", true);


//                            String confirm = " \n" + Strings.Dgreen + "[" + Strings.green + "CONFIRM" + Strings.Dgreen + "]\n" + " ";
//                            plugin.nms.sendClickableMessage(p, confirm, Strings.prefix + "playercount recover confirm");

                        } else if (plugin.getConfig().get("database").equals("sql")) {
                            p.sendMessage(Strings.prefix + Strings.red + "sql is not yet supported!");
                        }



                    }
                } else if (args[1].equalsIgnoreCase("confirm")) {
                    if (confirmCommand.get("1")) {
                        for(int i=0; i < 100; i ++) {
                            sender.sendMessage("");
                        }
                        api.recoverPlayers(p);
                        p.sendMessage(Strings.prefix + Strings.green + "Succsesfully recoverd already joined players!");
                        confirmCommand.put("1", false);
                    }

                }
            }

            return true;
        }


        return true;
    }

    private HashMap<String, Boolean> confirmCommand = new HashMap<String, Boolean>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String String, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("reload", "count", "reset", "recover");
        }

        return null;
    }


}
