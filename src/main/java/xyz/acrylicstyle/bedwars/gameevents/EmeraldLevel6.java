package xyz.acrylicstyle.bedwars.gameevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.acrylicstyle.bedwars.generators.EmeraldGenerator;
import xyz.acrylicstyle.bedwars.utils.GameEvent;

public class EmeraldLevel6 implements GameEvent {
    public void run() {
        EmeraldGenerator.setTime(30);
        Bukkit.broadcastMessage(ChatColor.GREEN + "Emerald Generator has been upgraded to VI");
    }

    public int getTime() {
        return 60*48; // 48 minutes
    }

    @Override
    public String getName() {
        return "Emerald VI";
    }
}
