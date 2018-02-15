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
        this.plugin = plugin;
        this.version = getLatestVersion();
    }

    public String getLatestVersion() {
        try {
            int resource = 52758;
            HttpURLConnection con = (HttpURLConnection)new URL("https://api.spigotmc.org/legacy/update.php?resource=52758").openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream().write("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=52758".getBytes("UTF-8"));
            String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (version.length() <= 7) {
                return version;
            }
        } catch (Exception ex) {
            System.out.println("---------------------------------");
            this.plugin.getLogger().info("Failed to check for a update on spigot.");
            System.out.println("---------------------------------");
        }
        return null;

    }

    public boolean isConnected() {
        return this.version != null;
    }

    public boolean hasUpdate() {
        return !this.version.equals(this.plugin.getDescription().getVersion());
    }
}