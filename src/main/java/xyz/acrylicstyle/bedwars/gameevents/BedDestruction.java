package xyz.acrylicstyle.bedwars.gameevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.utils.GameEvent;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class BedDestruction implements GameEvent {
    @SuppressWarnings("deprecation")
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 100, 0.9F);
            player.sendTitle("" + ChatColor.RED + ChatColor.BOLD + "BED DESTROYED!", "You will no longer respawn!");
            BedWars.aliveTeam.forEach(team -> BedWars.aliveTeam.remove(team));
        });
        Bukkit.broadcastMessage("" + ChatColor.RED + ChatColor.BOLD + "All bed has been destroyed!");
    }

    public int getTime() {
        return (int) (60*50/Utils.eventTime); // 50 minutes
    }

    @Override
    public String getName() {
        return "Bed gone";
    }
}
