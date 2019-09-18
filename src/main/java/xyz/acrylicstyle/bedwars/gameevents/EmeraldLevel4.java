package xyz.acrylicstyle.bedwars.gameevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.acrylicstyle.bedwars.generators.EmeraldGenerator;
import xyz.acrylicstyle.bedwars.utils.GameEvent;

public class EmeraldLevel4 implements GameEvent {
    public void run() {
        EmeraldGenerator.setTime(35);
        Bukkit.broadcastMessage(ChatColor.GREEN + "Emerald Generator has been upgraded to IV");
    }

    public int getTime() {
        return 60*30; // 30 minutes
    }

    @Override
    public String getName() {
        return "Emerald IV";
    }
}
