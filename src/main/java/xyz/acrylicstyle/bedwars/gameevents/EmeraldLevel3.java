package xyz.acrylicstyle.bedwars.gameevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.acrylicstyle.bedwars.generators.EmeraldGenerator;
import xyz.acrylicstyle.bedwars.utils.GameEvent;

public class EmeraldLevel3 implements GameEvent {
    public void run() {
        EmeraldGenerator.setTime(45);
        Bukkit.broadcastMessage(ChatColor.GREEN + "Emerald Generator has been upgraded to III");
    }

    public int getTime() {
        return 60*20; // 20 minutes
    }

    @Override
    public String getName() {
        return "Emerald III";
    }
}
