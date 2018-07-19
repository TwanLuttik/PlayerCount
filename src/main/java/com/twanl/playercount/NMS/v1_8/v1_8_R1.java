package com.twanl.playercount.NMS.v1_8;

import com.twanl.playercount.NMS.VersionHandler;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created by Twan on 3/26/2018.
 **/
public class v1_8_R1 implements VersionHandler {

    public v1_8_R1() {}

    public void sendClickableHovarableMessageURL(Player p, String clickabletext, String hovertext, String URL) {
        IChatBaseComponent chat = ChatSerializer.a("{\"text\":\"" + clickabletext + "\",\"clickEvent\":{\"action\":\"open_url\", " +
                "\"value\":\"" + URL + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"" + hovertext + "\"}]}}}");
        PacketPlayOutChat packet = new PacketPlayOutChat(chat);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
    }

    public void sendClickableMessage(Player player, String clickabletext, String runcommand) {
        IChatBaseComponent chat = ChatSerializer.a("{\"text\":\"" + clickabletext + "\",\"clickEvent\":{\"action\":\"run_command\", " +
                "\"value\":\"" + runcommand + "\"}}");
        PacketPlayOutChat packet = new PacketPlayOutChat(chat);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

    public void sendTitleMessage(Player p, String Title, String subTitle, int time) {
        PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;

        if (Title != null) {
            IChatBaseComponent chatBaseTitle = ChatSerializer.a("{\"text\": \"" + Title + "\"}");
            PacketPlayOutTitle pTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatBaseTitle);
            connection.sendPacket(pTitle);
        }

        if (subTitle != null) {
            IChatBaseComponent chatBaseSubTitle = ChatSerializer.a("{\"text\": \"" + subTitle + "\"}");
            PacketPlayOutTitle pSubTitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatBaseSubTitle);
            connection.sendPacket(pSubTitle);

        }

        PacketPlayOutTitle length = new PacketPlayOutTitle(5, time, 5);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(length);
    }
}
