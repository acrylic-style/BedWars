package xyz.acrylicstyle.bedwars.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.utils.PlayerStatus;
import xyz.acrylicstyle.bedwars.utils.Team;
import xyz.acrylicstyle.bedwars.utils.Utils;

import java.util.UUID;

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
            Objective objective = Utils.getObjective(player.getUniqueId());
            UUID uuid = player.getUniqueId();
            Utils.setScoreReplace(ChatColor.GREEN + "Map: " + BedWars.map.getString("name", "???"), 5, objective, uuid);
            Utils.setScoreReplace("  ", 4, objective, uuid);
            Utils.setScoreReplace(ChatColor.GREEN + "Players: " + Bukkit.getOnlinePlayers().size(), 3, objective, uuid);
            Utils.setScoreReplace(" ", 2, objective, uuid);
            Utils.setScoreReplace(ChatColor.YELLOW + BedWars.config.getString("domain", "www.acrylicstyle.xyz"), -1, objective, uuid);
            if (Bukkit.getOnlinePlayers().size() < Utils.minimumPlayers) {
                Utils.setScoreReplace(ChatColor.WHITE + "Waiting...", 1, objective, uuid);
                Utils.setScoreReplace("", 0, objective, uuid);
                return;
            }
            Utils.setScoreReplace("Starting in " + ChatColor.GREEN + Utils.secondsToTime(countdown), 1, objective, uuid);
            Utils.setScoreReplace("", 0, objective, uuid);
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
                if (BedWars.team.values(Team.RED).size() < Utils.teamSize) {
                    BedWars.team.add(player.getUniqueId(), Team.RED);
                } else if (BedWars.team.values(Team.BLUE).size() < Utils.teamSize) {
                    BedWars.team.add(player.getUniqueId(), Team.BLUE);
                } else if (BedWars.team.values(Team.YELLOW).size() < Utils.teamSize) {
                    BedWars.team.add(player.getUniqueId(), Team.YELLOW);
                } else if (BedWars.team.values(Team.GREEN).size() < Utils.teamSize) {
                    BedWars.team.add(player.getUniqueId(), Team.GREEN);
                } else if (BedWars.team.values(Team.WHITE).size() < Utils.teamSize) {
                    BedWars.team.add(player.getUniqueId(), Team.WHITE);
                } else if (BedWars.team.values(Team.BLACK).size() < Utils.teamSize) {
                    BedWars.team.add(player.getUniqueId(), Team.BLACK);
                } else if (BedWars.team.values(Team.PINK).size() < Utils.teamSize) {
                    BedWars.team.add(player.getUniqueId(), Team.PINK);
                } else if (BedWars.team.values(Team.AQUA).size() < Utils.teamSize) {
                    BedWars.team.add(player.getUniqueId(), Team.AQUA);
                }
                player.sendTitle("" + ChatColor.RED + ChatColor.BOLD + "Go!", "");
                player.teleport(Utils.getConfigUtils().getTeamSpawnPoint(BedWars.team.get(player.getUniqueId())));
            }
            if (countdown == 0) {
                if (BedWars.team.values(Team.RED).size() > 0) BedWars.aliveTeam.add(Team.RED);
                if (BedWars.team.values(Team.BLUE).size() > 0) BedWars.aliveTeam.add(Team.BLUE);
                if (BedWars.team.values(Team.YELLOW).size() > 0) BedWars.aliveTeam.add(Team.YELLOW);
                if (BedWars.team.values(Team.GREEN).size() > 0) BedWars.aliveTeam.add(Team.GREEN);
                if (BedWars.team.values(Team.WHITE).size() > 0) BedWars.aliveTeam.add(Team.WHITE);
                if (BedWars.team.values(Team.BLACK).size() > 0) BedWars.aliveTeam.add(Team.BLACK);
                if (BedWars.team.values(Team.PINK).size() > 0) BedWars.aliveTeam.add(Team.PINK);
                if (BedWars.team.values(Team.AQUA).size() > 0) BedWars.aliveTeam.add(Team.AQUA);
                GameTask gameTask = new GameTask();
                gameTask.runTaskTimer(Utils.getInstance(), 0, 20);
                Utils.setGameTask(gameTask);
                GeneratorTask generatorTask = new GeneratorTask();
                generatorTask.runTask(Utils.getInstance());
                EventTask eventTask = new EventTask();
                eventTask.scheduleEvents();
                eventTask.runTaskTimer(Utils.getInstance(), 0, 20);
                this.cancel();
                this.countdown = 30;
                return;
            }
        }
        countdown--;
    }
}
