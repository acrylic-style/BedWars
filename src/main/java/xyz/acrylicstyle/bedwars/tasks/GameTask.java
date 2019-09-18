package xyz.acrylicstyle.bedwars.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.utils.Team;
import xyz.acrylicstyle.bedwars.utils.Utils;

import java.util.UUID;

public class GameTask extends BukkitRunnable {
    public static int playedTime = 0;

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            Objective objective = Utils.getObjective(player.getUniqueId());
            Utils.setScoreReplace("    ", 11, objective, uuid);
            // Event scoreboard will appear at score 10
            Utils.setScoreReplace("   ", 9, objective, uuid);
            Utils.teamSB(Team.RED, 8, objective, uuid);
            Utils.teamSB(Team.BLUE, 7, objective, uuid);
            Utils.teamSB(Team.GREEN, 6, objective, uuid);
            Utils.teamSB(Team.YELLOW, 5, objective, uuid);
            Utils.teamSB(Team.AQUA, 4, objective, uuid);
            Utils.teamSB(Team.WHITE, 3, objective, uuid);
            Utils.teamSB(Team.PINK, 2, objective, uuid);
            Utils.teamSB(Team.BLACK, 1, objective, uuid);
            Utils.setScoreReplace(ChatColor.YELLOW + BedWars.config.getString("domain", "www.acrylicstyle.xyz"), -1, objective, uuid);
            player.setScoreboard(BedWars.scoreboards.get(player.getUniqueId()));
        }
        playedTime++;
    }
}
