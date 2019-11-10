package xyz.acrylicstyle.bedwars.gameevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.utils.GameEvent;
import xyz.acrylicstyle.bedwars.utils.Team;
import xyz.acrylicstyle.bedwars.utils.Utils;

import java.util.Arrays;

public class SuddenDeath implements GameEvent {
    @SuppressWarnings("deprecation")
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 100, 1);
            player.sendTitle("" + ChatColor.RED + ChatColor.BOLD + "SUDDEN DEATH", "");
        });
        Arrays.asList(Team.values()).forEach(team -> {
            BedWars.world.spawnEntity(Utils.getConfigUtils().getTeamEnderDragonSpawnPoint(team), EntityType.ENDER_DRAGON);
            Bukkit.broadcastMessage(ChatColor.RED + "Sudden Death! " + team.color + Utils.capitalize(team.name()) + ChatColor.RED + " +1 Dragon!");
        });
    }

    public int getTime() {
        return (int) (60*55/Utils.eventTime); // 55 minutes
    }

    @Override
    public String getName() {
        return "Sudden Death";
    }
}
