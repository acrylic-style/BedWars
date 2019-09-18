package xyz.acrylicstyle.bedwars.gameevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.acrylicstyle.bedwars.generators.DiamondGenerator;
import xyz.acrylicstyle.bedwars.utils.GameEvent;

public class DiamondLevel3 implements GameEvent {
    public void run() {
        DiamondGenerator.setTime(15);
        Bukkit.broadcastMessage(ChatColor.AQUA + "Diamond Generator" + ChatColor.GREEN + " has been upgraded to III");
    }

    public int getTime() {
        return 60*15; // 15 minutes
    }

    @Override
    public String getName() {
        return "Diamond III";
    }
}
