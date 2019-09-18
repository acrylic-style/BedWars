package xyz.acrylicstyle.bedwars.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.utils.Team;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class GameTask extends BukkitRunnable {
    public static int playedTime = 0;

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Objective objective = Utils.getObjective(player.getUniqueId());
            Utils.setScoreReplace(ChatColor.YELLOW + BedWars.config.getString("domain", "www.acrylicstyle.xyz"), -1, objective);
            Utils.setScoreReplace("   ", 9, objective);
            Utils.teamSB(Team.RED, 8, objective);
            Utils.teamSB(Team.BLUE, 7, objective);
            Utils.teamSB(Team.YELLOW, 6, objective);
            Utils.teamSB(Team.GREEN, 5, objective);
            Utils.teamSB(Team.WHITE, 4, objective);
            Utils.teamSB(Team.BLACK, 3, objective);
            Utils.teamSB(Team.PINK, 2, objective);
            Utils.teamSB(Team.AQUA, 1, objective);
            player.setScoreboard(BedWars.scoreboards.get(player.getUniqueId()));
        }
        playedTime++;
    }
}
