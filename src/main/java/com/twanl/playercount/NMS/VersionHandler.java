package com.twanl.playercount.NMS;


import org.bukkit.entity.Player;

/**
 * Created by Twan on 3/26/2018.
 **/
public abstract interface VersionHandler {

    public abstract void sendClickableHovarableMessageURL(Player p, String clickabletext, String hovertext, String URL);

    public abstract void sendClickableMessage(Player player, String clickabletext, String runcommand);
}
