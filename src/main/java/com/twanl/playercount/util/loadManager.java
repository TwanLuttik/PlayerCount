package com.twanl.playercount.util;

import com.twanl.playercount.PlayerCount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Twan on 8/17/2018.
 **/
public class loadManager {

    private static HashMap<Integer, Boolean> firstJoin_BroadCast = new HashMap<>();
    private static HashMap<Integer, Boolean> firstJoin_Title_BroadCast = new HashMap<>();
    private static ArrayList<String> firstJoin_Message = new ArrayList<>();
    private static HashSet<String> firstJoin_Title = new HashSet<>();
    private static HashSet<String> firstJoin_SubTitle = new HashSet<>();
    private static HashSet<Integer> firstJoin_TitleShow_Time = new HashSet<>();

    private static HashMap<Integer, Boolean> defaultJoin_BroadCast = new HashMap<>();
    private static HashMap<Integer, Boolean> defaultJoin_Title_BroadCast = new HashMap<>();
    private static ArrayList<String> defaultJoin_Message = new ArrayList<>();
    private static HashSet<String> defaultJoin_Title = new HashSet<>();
    private static HashSet<String> defaultJoin_SubTitle = new HashSet<>();
    private static HashSet<Integer> defaultJoin_TitleShow_Time = new HashSet<>();

    private static ArrayList<String> worlds = new ArrayList<>();
    private static HashMap<Integer, Boolean> update_Message = new HashMap<>();
    private static HashSet<String> database = new HashSet<>();



    public static void  loadHashSet() {
        PlayerCount plugin = PlayerCount.getPlugin(PlayerCount.class);

        firstJoin_BroadCast.put(1, plugin.getConfig().getBoolean("first_join.broadcast"));
        firstJoin_Title_BroadCast.put(1, plugin.getConfig().getBoolean("first_join.broadcast_title"));
        for (String key : plugin.getConfig().getStringList("first_join.text")) {
            if (plugin.getConfig().isSet("first_join.text")) {
                firstJoin_Message.add(key);
            }
        }
        firstJoin_Title.add(plugin.getConfig().getString("first_join.title"));
        firstJoin_SubTitle.add(plugin.getConfig().getString("first_join.subtitle"));
        firstJoin_TitleShow_Time.add(plugin.getConfig().getInt("first_join.time"));



        defaultJoin_BroadCast.put(1, plugin.getConfig().getBoolean("default_join.broadcast"));
        defaultJoin_Title_BroadCast.put(1, plugin.getConfig().getBoolean("default_join.broadcast_title"));
        for (String key : plugin.getConfig().getStringList("default_join.text")) {
            if (plugin.getConfig().isSet("default_join.text")) {
                defaultJoin_Message.add(key);
            }
        }
        defaultJoin_Title.add(plugin.getConfig().getString("default_join.title"));
        defaultJoin_SubTitle.add(plugin.getConfig().getString("default_join.subtitle"));
        defaultJoin_TitleShow_Time.add(plugin.getConfig().getInt("default_join.time"));


        update_Message.put(1, plugin.getConfig().getBoolean("update_message"));
        database.add(plugin.getConfig().getString("database"));
        for (String key : plugin.getConfig().getStringList("enable_worlds")) {
            worlds.add(key);
        }

    }


    public static boolean firstJoin_BroadCast() {
        return firstJoin_BroadCast.get(1);
    }

    public static boolean firstJoin_Title_BroadCast() {
        return firstJoin_Title_BroadCast.get(1);
    }

    public static ArrayList<String> firstJoin_message() {
        return firstJoin_Message;
    }

    public static String firstJoin_Title() {
        return cleanString(firstJoin_Title.toString());
    }

    public static String firstJoin_SubTitle() {
        return cleanString(firstJoin_SubTitle.toString());
    }

    public static Integer firstJoin_TitleShow_Time() {
        return Integer.valueOf(cleanString(firstJoin_TitleShow_Time.toString()));
    }


    public static boolean defaultJoin_BroadCast() {
        return defaultJoin_BroadCast.get(1);
    }

    public static boolean defaultJoin_Title_BroadCast() {
        return defaultJoin_Title_BroadCast.get(1);
    }

    public static ArrayList<String> defaultJoin_Message() {
        return defaultJoin_Message;
    }

    public static String defaultJoin_Title() {
        return cleanString(defaultJoin_Title.toString());
    }

    public static String defaultJoin_SubTitle() {
        return cleanString(defaultJoin_SubTitle.toString());
    }

    public static Integer defaultJoin_TitleShow_Time() {
        return Integer.valueOf(cleanString(defaultJoin_TitleShow_Time.toString()));
    }


    public static ArrayList<String> worlds() {
        return worlds;
    }

    public static Boolean update_Message() {
        return update_Message.get(1);
    }

    public static String database() {
        return cleanString(database.toString());
    }





    // remove the brackets from the string
    private static String cleanString(String s) {
        return s.replace("[", "").replace("]", "");
    }







}
