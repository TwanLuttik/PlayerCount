package com.twanl.playercount.util;

import com.twanl.playercount.PlayerCount;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {

    private PlayerCount plugin = PlayerCount.getPlugin(PlayerCount.class);
    public String version;


    public UpdateChecker(PlayerCount plugin) {
        plugin = plugin;
        version = getLatestVersion();
    }

    public String getLatestVersion() {
        try {
            int resource = 52758;
            HttpURLConnection con = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resource).openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            String key = "key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + resource;
            con.getOutputStream().write(key.getBytes("UTF-8"));
            String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (version.length() <= 7) {
                return version;
            }
        } catch (Exception ex) {
            System.out.println("---------------------------------");
            plugin.getLogger().info("Failed to check for a update on spigot.");
            System.out.println("---------------------------------");
        }
        return null;

    }

    public boolean isConnected() {
        return version != null;
    }

    public boolean hasUpdate() {
        return !version.equals(plugin.getDescription().getVersion());
    }
}
