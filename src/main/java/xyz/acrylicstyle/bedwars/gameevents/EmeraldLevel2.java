package xyz.acrylicstyle.bedwars.gameevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.acrylicstyle.bedwars.generators.EmeraldGenerator;
import xyz.acrylicstyle.bedwars.utils.GameEvent;

public class EmeraldLevel2 implements GameEvent {
    public void run() {
        EmeraldGenerator.setTime(55);
        Bukkit.broadcastMessage(ChatColor.GREEN + "Emerald Generator has been upgraded to II");
    }

    public int getTime() {
        return 60*10; // 10 minutes
    }

    @Override
    public String getName() {
        return "Emerald II";
    }
}
