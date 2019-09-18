package xyz.acrylicstyle.bedwars.gameevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.acrylicstyle.bedwars.generators.DiamondGenerator;
import xyz.acrylicstyle.bedwars.utils.GameEvent;

public class DiamondLevel6 implements GameEvent {
    public void run() {
        DiamondGenerator.setTime(8);
        Bukkit.broadcastMessage(ChatColor.AQUA + "Diamond Generator" + ChatColor.GREEN + " has been upgraded to VI");
    }

    public int getTime() {
        return 60*45; // 45 minutes
    }

    @Override
    public String getName() {
        return "Diamond VI";
    }
}
