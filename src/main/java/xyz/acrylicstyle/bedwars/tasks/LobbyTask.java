package xyz.acrylicstyle.bedwars.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
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
            if (countdown == 5) {
                ActionBar.setActionBarWithoutException(player, ChatColor.YELLOW + "5");
            } else if (countdown == 4) {
                ActionBar.setActionBarWithoutException(player, ChatColor.YELLOW + "4");
            } else if (countdown == 3) {
                ActionBar.setActionBarWithoutException(player, ChatColor.RED + "3");
            } else if (countdown == 2) {
                ActionBar.setActionBarWithoutException(player, ChatColor.RED + "3");
            } else if (countdown == 1) {
                ActionBar.setActionBarWithoutException(player, ChatColor.RED + "3");
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
