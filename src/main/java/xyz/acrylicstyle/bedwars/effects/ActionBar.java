package xyz.acrylicstyle.bedwars.effects;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class ActionBar {
    private static void setActionBar(Player player, String text) throws InvocationTargetException {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = manager.createPacket(PacketType.Play.Server.CHAT);
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(text));
        packet.getBytes().write(0, (byte) 2);
        manager.sendServerPacket(player, packet);
    }

    public static void setActionBarWithoutException(Player player, String text) {
        try {
            ActionBar.setActionBar(player, text);
        } catch (InvocationTargetException e) {
            // we shouldn't ignore it
        }
    }
}