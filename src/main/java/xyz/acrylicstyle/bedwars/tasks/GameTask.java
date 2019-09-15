package xyz.acrylicstyle.bedwars.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.utils.Team;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class GameTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Scoreboard scoreboard = BedWars.scoreboards.get(player.getUniqueId());
            Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
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
    }
}
