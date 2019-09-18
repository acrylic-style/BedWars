package xyz.acrylicstyle.bedwars.gameevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.acrylicstyle.bedwars.generators.EmeraldGenerator;
import xyz.acrylicstyle.bedwars.utils.GameEvent;

public class EmeraldLevel5 implements GameEvent {
    public void run() {
        EmeraldGenerator.setTime(33);
        Bukkit.broadcastMessage(ChatColor.GREEN + "Emerald Generator has been upgraded to V");
    }

    public int getTime() {
        return 60*40; // 40 minutes
    }

    @Override
    public String getName() {
        return "Emerald V";
    }
}
