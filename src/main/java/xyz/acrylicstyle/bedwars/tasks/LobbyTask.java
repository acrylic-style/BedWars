package xyz.acrylicstyle.bedwars.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.effects.ActionBar;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class LobbyTask extends BukkitRunnable {
    private int countdown = 30;

    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().size() <= 0) {
            BedWars.startedLobbyTask = false;
            this.cancel();
            return;
        }
        BedWars.startedLobbyTask = true;
        for (Player player : Bukkit.getOnlinePlayers()) {
            Scoreboard scoreboard = BedWars.scoreboards.get(player.getUniqueId());
            Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
            Utils.setScoreReplace(ChatColor.GREEN + "Map: " + BedWars.map.getString("name", "???"), 4, objective);
            Utils.setScoreReplace(ChatColor.GREEN + "Players: " + Bukkit.getOnlinePlayers().size(), 3, objective);
            Utils.setScoreReplace(" ", 2, objective);
            Utils.setScoreReplace("", 1, objective);
            Utils.setScoreReplace("", 0, objective);
            player.setScoreboard(BedWars.scoreboards.get(player.getUniqueId()));
            if (countdown == 10) {
                ActionBar.setActionBarWithoutException(player, ChatColor.YELLOW + "10");
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 100, 1);
            } else if (countdown == 5) {
                ActionBar.setActionBarWithoutException(player, ChatColor.YELLOW + "5");
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 100, 1);
            } else if (countdown == 4) {
                ActionBar.setActionBarWithoutException(player, ChatColor.YELLOW + "4");
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 100, 1);
            } else if (countdown == 3) {
                ActionBar.setActionBarWithoutException(player, ChatColor.RED + "3");
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 100, 1);
            } else if (countdown == 2) {
                ActionBar.setActionBarWithoutException(player, ChatColor.RED + "2");
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 100, 1);
            } else if (countdown == 1) {
                ActionBar.setActionBarWithoutException(player, ChatColor.RED + "1");
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 100, 1);
            } else if (countdown == 0) {
                player.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Go!");
                GameTask gameTask = new GameTask();
                gameTask.runTaskTimer(Utils.getInstance(), 0, 20);
                Utils.setGameTask(gameTask);
            }
        }
        countdown--;
    }
}
