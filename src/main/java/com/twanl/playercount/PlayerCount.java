package com.twanl.playercount;

import com.twanl.playercount.NMS.VersionHandler;
import com.twanl.playercount.NMS.v1_10.v1_10_R1;
import com.twanl.playercount.NMS.v1_11.v1_11_R1;
import com.twanl.playercount.NMS.v1_12.v1_12_R1;
import com.twanl.playercount.NMS.v1_13.v1_13_R1;
import com.twanl.playercount.NMS.v1_8.v1_8_R1;
import com.twanl.playercount.NMS.v1_8.v1_8_R2;
import com.twanl.playercount.NMS.v1_8.v1_8_R3;
import com.twanl.playercount.NMS.v1_9.v1_9_R1;
import com.twanl.playercount.NMS.v1_9.v1_9_R2;
import com.twanl.playercount.events.JoinEvent;
import com.twanl.playercount.sql.SqlLib;
import com.twanl.playercount.util.ConfigManager;
import com.twanl.playercount.util.Metrics;
import com.twanl.playercount.util.Strings;
import com.twanl.playercount.util.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;


public class PlayerCount extends JavaPlugin {


    public ConfigManager cfgM;
    public VersionHandler nms;
    private UpdateChecker checker;
    protected PluginDescriptionFile pdfFile = getDescription();
    private final String PluginVersionOn = Strings.green + "(" + pdfFile.getVersion() + ")";
    private final String PluginVersionOff = Strings.red + "(" + pdfFile.getVersion() + ")";

    private Connection connection;
    public String host, database, username, password, table;
    public int port;


    public void onEnable() {

        // enable mySQL if enabled in config
        if (getConfig().get("database").equals("sql")) {
            SqlLib sql = new SqlLib();
            mysqlSetup();
            sql.createTable();
        }

        getServerVersion();
        checker = new UpdateChecker(this);
        if (checker.isConnected()) {
            if (checker.hasUpdate()) {
                getServer().getConsoleSender().sendMessage(Strings.green + "");
                getServer().getConsoleSender().sendMessage(Strings.green + "------------------------");
                getServer().getConsoleSender().sendMessage(Strings.red + "PlayerCount is outdated!");
                getServer().getConsoleSender().sendMessage(Strings.white + "Newest version: " + checker.getLatestVersion());
                getServer().getConsoleSender().sendMessage(Strings.white + "Your version: " + Strings.green + getDescription().getVersion());
                getServer().getConsoleSender().sendMessage("Please download the new version at https://www.spigotmc.org/resources/playercount.52758/download?version=208548");
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
        Load();
        Bukkit.getConsoleSender().sendMessage(Strings.logName + Strings.green + "Has been enabled " + PluginVersionOn);
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(Strings.logName + Strings.red + "Has been disabled " + PluginVersionOff);
    }


    public void Load() {
        // Register listeners
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new SqlLib(), this);

        // Register commands
        Commands commands = new Commands();
        getCommand("playercount").setExecutor(commands);

        //LoadConfig
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        reloadConfig();
    }

    public void loadConfigManager() {
        cfgM = new ConfigManager();
        cfgM.setup();
        cfgM.savePlayers();
        cfgM.reloadplayers();
    }

    private void getServerVersion() {
        String a = getServer().getClass().getPackage().getName();
        String version = a.substring(a.lastIndexOf('.') + 1);

        // Check
        if (version.equalsIgnoreCase("v1_8_R1")) {
            nms = new v1_8_R1();
        } else if (version.equalsIgnoreCase("v1_8_R2")) {
            nms = new v1_8_R2();
        } else if (version.equalsIgnoreCase("v1_8_R3")) {
            nms = new v1_8_R3();
        } else if (version.equalsIgnoreCase("v1_9_R1")) {
            nms = new v1_9_R1();
        } else if (version.equalsIgnoreCase("v1_9_R2")) {
            nms = new v1_9_R2();
        } else if (version.equalsIgnoreCase("v1_10_R1")) {
            nms = new v1_10_R1();
        } else if (version.equalsIgnoreCase("v1_11_R1")) {
            nms = new v1_11_R1();
        } else if (version.equalsIgnoreCase("v1_12_R1")) {
            nms = new v1_12_R1();
        } else if (version.equalsIgnoreCase("v1_13_R1")) {
            nms = new v1_13_R1();
        } else {
            getServer().getConsoleSender().sendMessage(Strings.red + "This plugin wil not work properly with version" + version);
        }
    }

    private HashMap<String, Boolean> dbMap = new HashMap<String, Boolean>();

    public void mysqlSetup() {
        host = getConfig().getString("mySQL.host");
        port = getConfig().getInt("mySQL.port");
        database = getConfig().getString("mySQL.database");
        username = getConfig().getString("mySQL.username");
        password = getConfig().getString("mySQL.password");
        table = getConfig().getString("mySQL.table");

        try {

            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }

                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":"
                        + this.port + "/" + this.database, this.username, this.password));


                Bukkit.getConsoleSender().sendMessage(Strings.logName + "mySQL connected to database: " +Strings.green + database);
            }
        } catch (SQLException | ClassNotFoundException e) {
            Bukkit.getConsoleSender().sendMessage(Strings.logName + Strings.red + "mySQL cannot find database");
            //e.printStackTrace();

        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
