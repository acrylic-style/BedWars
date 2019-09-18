package xyz.acrylicstyle.bedwars.gameevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.acrylicstyle.bedwars.utils.GameEvent;

public class KickAll implements GameEvent {
    @Override
    public int getTime() {
        return 60*61; // 61 minutes
    }

    @Override
    public String getName() {
        return "Kick all players";
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(ChatColor.GREEN + "Game is over!"));
        Bukkit.shutdown();
    }
}
