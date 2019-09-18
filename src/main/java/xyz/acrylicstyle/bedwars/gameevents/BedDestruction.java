package xyz.acrylicstyle.bedwars.gameevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.acrylicstyle.bedwars.utils.GameEvent;

public class BedDestruction implements GameEvent {
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle("" + ChatColor.RED + ChatColor.BOLD + "BED DESTROYED!", "You will no longer respawn!"));
        Bukkit.broadcastMessage("" + ChatColor.RED + ChatColor.BOLD + "All bed has been destroyed!");
    }

    public int getTime() {
        return 60*50; // 50 minutes
    }

    @Override
    public String getName() {
        return "Bed gone";
    }
}
