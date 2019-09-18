package xyz.acrylicstyle.bedwars.gameevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.acrylicstyle.bedwars.generators.DiamondGenerator;
import xyz.acrylicstyle.bedwars.utils.GameEvent;

public class DiamondLevel2 implements GameEvent {
    public void run() {
        DiamondGenerator.setTime(20);
        Bukkit.broadcastMessage(ChatColor.AQUA + "Diamond Generator" + ChatColor.GREEN + " has been upgraded to II");
    }

    public int getTime() {
        return 60*6; // 6 minutes
    }

    @Override
    public String getName() {
        return "Diamond II";
    }
}
