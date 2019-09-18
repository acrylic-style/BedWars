package xyz.acrylicstyle.bedwars.gameevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.acrylicstyle.bedwars.generators.DiamondGenerator;
import xyz.acrylicstyle.bedwars.utils.GameEvent;

public class DiamondLevel5 implements GameEvent {
    public void run() {
        DiamondGenerator.setTime(9);
        Bukkit.broadcastMessage(ChatColor.AQUA + "Diamond Generator" + ChatColor.GREEN + " has been upgraded to V");
    }

    public int getTime() {
        return 60*35; // 35 minutes
    }

    @Override
    public String getName() {
        return "Diamond V";
    }
}
