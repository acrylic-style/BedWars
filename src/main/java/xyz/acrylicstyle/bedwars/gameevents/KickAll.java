package xyz.acrylicstyle.bedwars.gameevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.acrylicstyle.bedwars.utils.GameEvent;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class KickAll implements GameEvent {
    @Override
    public int getTime() {
        return (int) (60*600/Utils.eventTime); // 61 minutes
    }

    @Override
    public String getName() {
        return "None!";
    }

    @Override
    public void run() {
        Bukkit.shutdown();
    }
}
