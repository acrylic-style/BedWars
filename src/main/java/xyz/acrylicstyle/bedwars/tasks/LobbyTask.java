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
import xyz.acrylicstyle.bedwars.utils.PlayerStatus;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class LobbyTask extends BukkitRunnable {
    private int countdown = 30;

    @SuppressWarnings("deprecation")
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
            Utils.setScoreReplace(ChatColor.GREEN + "Map: " + BedWars.map.getString("name", "???"), 5, objective);
            Utils.setScoreReplace("  ", 4, objective);
            Utils.setScoreReplace(ChatColor.GREEN + "Players: " + Bukkit.getOnlinePlayers().size(), 3, objective);
            Utils.setScoreReplace(" ", 2, objective);
            int minutes = (int) Math.floor((float) countdown / 60F);
            String count = minutes + ":" + (countdown % 60);
            if (Bukkit.getOnlinePlayers().size() < Utils.minimumPlayers) {
                Utils.setScoreReplace(ChatColor.WHITE + "Waiting..." + count, 1, objective);
                Utils.setScoreReplace("", 0, objective);
                return;
            }
            Utils.setScoreReplace(ChatColor.GREEN + "Starting in " + count, 1, objective);
            Utils.setScoreReplace("", 0, objective);
            player.setScoreboard(BedWars.scoreboards.get(player.getUniqueId()));
            if (countdown == 10) {
                player.sendTitle(ChatColor.YELLOW + "10", "");
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 100, 1);
            } else if (countdown == 5) {
                player.sendTitle(ChatColor.YELLOW + "5", "");
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 100, 1);
            } else if (countdown == 4) {
                player.sendTitle(ChatColor.YELLOW + "4", "");
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 100, 1);
            } else if (countdown == 3) {
                player.sendTitle(ChatColor.RED + "3", "");
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 100, 1);
            } else if (countdown == 2) {
                player.sendTitle(ChatColor.RED + "2", "");
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 100, 1);
            } else if (countdown == 1) {
                player.sendTitle(ChatColor.RED + "1", "");
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 100, 1);
            } else if (countdown == 0) {
                BedWars.status.put(player.getUniqueId(), PlayerStatus.ALIVE);
                player.sendTitle("" + ChatColor.RED + ChatColor.BOLD + "Go!", "");
                GameTask gameTask = new GameTask();
                gameTask.runTaskTimer(Utils.getInstance(), 0, 20);
                Utils.setGameTask(gameTask);
            }
        }
        countdown--;
    }
}
