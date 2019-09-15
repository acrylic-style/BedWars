package xyz.acrylicstyle.bedwars;

import com.comphenix.protocol.injector.PlayerLoggedOutException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import xyz.acrylicstyle.bedwars.tasks.LobbyTask;
import xyz.acrylicstyle.bedwars.utils.Collection;
import xyz.acrylicstyle.bedwars.utils.Utils;
import xyz.acrylicstyle.tomeito_core.providers.ConfigProvider;
import xyz.acrylicstyle.tomeito_core.utils.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static xyz.acrylicstyle.bedwars.utils.Utils.getInstance;

public class BedWars extends JavaPlugin {
    public static ConfigProvider config = null;
    public static String mapName = null;
    public static ConfigProvider map = null;
    public static String worldName = null;
    public static World world = null;
    public static ScoreboardManager manager = null;
    public static Collection<UUID, Scoreboard> scoreboards = new Collection<>();
    public static boolean startedLobbyTask = false;

    @Override
    public void onEnable() {
        new BukkitRunnable() {
            public void run() {
                try {
                    Utils.reset();
                } catch (Exception e) {
                    Log.error("An error occurred while loading config!");
                    e.printStackTrace();
                    Bukkit.getPluginManager().disablePlugin(getInstance());
                }
            }
        }.runTaskLater(this, 1);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().setGameMode(GameMode.ADVENTURE);
        scoreboards.put(e.getPlayer().getUniqueId(), manager.getNewScoreboard());
        LobbyTask lobbyTask = new LobbyTask();
        Utils.setLobbyTask(lobbyTask);
        lobbyTask.runTaskTimer(this, 0, 20);
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        if (Bukkit.getOnlinePlayers().size() >= Utils.maximumPlayers) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Current game is full. Please try again later!");
        }
    }
}
