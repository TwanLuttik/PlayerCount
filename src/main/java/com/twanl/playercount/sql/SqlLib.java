package com.twanl.playercount.sql;

import com.twanl.playercount.PlayerCount;
import com.twanl.playercount.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Twan
 */
public class SqlLib implements Listener {


    private PlayerCount plugin = PlayerCount.getPlugin(PlayerCount.class);


    // create a DataBase if not exist (it will check itself if the database not exist)
    //TODO: create a database on the startup/reload from the server (by creating it will check automaticly first if its exist), >> NOT FIXED  we need to connect to the database first before we can create a database or do even execute a command
    public void createDB() {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + plugin.database);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //TODO: create a table if not exist || Fixed that we could not create a table
    // create a table if not exist (i think it will check itself if the table not exist)
    public void createTable() {

        // check if the table exist if not it will operate this methode
        if (!tableExist()) {
            try {
                PreparedStatement statement = plugin.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + plugin.table + " (UUID varchar(255), COUNT INT);");
                Bukkit.getConsoleSender().sendMessage(Strings.logName + "created table: " + Strings.green + plugin.table);
                statement.executeUpdate();


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //TODO: clear the the data inside the database
    // clears the data inside the table (it will note delete the table itself)
    public void clearTable() {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("TRUNCATE " + plugin.table + ";");
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //TODO: get a return methode from the player number
    // get the player join number (return methode(INT) )
    public int getPlayerNumber(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID='" + uuid + "';");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int i1 = rs.getInt("COUNT");
//                Bukkit.getConsoleSender().sendMessage(Strings.aqua + uuid + " " + i1);
                rs.close();
                return i1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    //TODO: get the highest value from the table
    // get the total already joined players
    public int getTotalJoinedPlayers() {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT COUNT(*) FROM " + plugin.table + ";");
            ResultSet rs = statement.executeQuery();
            rs.first();
            int numberOfRows = rs.getInt("COUNT(*)");
            rs.close();

            return numberOfRows;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    //TODO: check if the player exist in sql database
    // check if player is in database (will be used as if player has played before method)
    public boolean playerExist(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();
            if (results.next()) {
//                plugin.getServer().broadcastMessage(Strings.yellow + "Player Found");
                return true;
            }

//            plugin.getServer().broadcastMessage(Strings.red + "Player NOT Found");
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    //TODO: check if table exist else return false (return methode)
    public boolean tableExist() {
        try {
            DatabaseMetaData dbm = plugin.getConnection().getMetaData();
            ResultSet tables = dbm.getTables(null, null, "player_data", null);
            if (tables.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //TODO: add a player to the database(table)
    public void addPlayer(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();
            rs.next();


            int joinedPlayers = getTotalJoinedPlayers() + 1;

            PreparedStatement insert = plugin.getConnection().prepareStatement("INSERT INTO " + plugin.table + " (UUID,COUNT) VALUES (?,?)");
            insert.setString(1, uuid.toString());
            insert.setInt(2, joinedPlayers);
            insert.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
