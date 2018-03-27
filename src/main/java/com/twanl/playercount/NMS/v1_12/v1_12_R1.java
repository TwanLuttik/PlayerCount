package com.twanl.playercount.NMS.v1_12;

import com.twanl.playercount.NMS.VersionHandler;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created by Twan on 3/26/2018.
 **/
public class v1_12_R1 implements VersionHandler {

    public v1_12_R1() {}

    public void sendClickableHovarableMessageURL(Player p, String clickabletext, String hovertext, String URL) {
        IChatBaseComponent chat = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + clickabletext + "\",\"clickEvent\":{\"action\":\"open_url\", " +
                "\"value\":\"" + URL + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"" + hovertext + "\"}]}}}");
        PacketPlayOutChat packet = new PacketPlayOutChat(chat);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
    }

    public void sendClickableMessage(Player player, String clickabletext, String runcommand) {
        IChatBaseComponent chat = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + clickabletext + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":" +
                "\"/" + runcommand + "\"}}");
        PacketPlayOutChat packet = new PacketPlayOutChat(chat);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
}
