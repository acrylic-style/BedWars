package xyz.acrylicstyle.bedwars.gameevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.acrylicstyle.bedwars.generators.DiamondGenerator;
import xyz.acrylicstyle.bedwars.utils.GameEvent;

public class DiamondLevel4 implements GameEvent {
    public void run() {
        DiamondGenerator.setTime(10);
        Bukkit.broadcastMessage(ChatColor.AQUA + "Diamond Generator" + ChatColor.GREEN + " has been upgraded to IV");
    }

    public int getTime() {
        return 60*25; // 25 minutes
    }

    @Override
    public String getName() {
        return "Diamond IV";
    }
}
