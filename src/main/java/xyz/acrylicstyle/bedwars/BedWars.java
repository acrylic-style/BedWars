package xyz.acrylicstyle.bedwars;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import xyz.acrylicstyle.bedwars.tasks.LobbyTask;
import xyz.acrylicstyle.bedwars.utils.*;
import xyz.acrylicstyle.bedwars.utils.Collection;
import xyz.acrylicstyle.tomeito_core.providers.ConfigProvider;
import xyz.acrylicstyle.tomeito_core.utils.Log;

import java.util.*;

import static xyz.acrylicstyle.bedwars.utils.Utils.getInstance;

public class BedWars extends JavaPlugin implements Listener {
    public static ConfigProvider config = null;
    public static String mapName = null;
    public static ConfigProvider map = null;
    public static String worldName = null;
    public static World world = null;
    public static ScoreboardManager manager = null;
    public static Collection<UUID, Scoreboard> scoreboards = new Collection<>();
    public static Collection<UUID, PlayerStatus> status = new Collection<>();
    public static Collection<UUID, Team> team = new Collection<>();
    public static Set<Team> aliveTeam = new HashSet<>();
    public static boolean startedLobbyTask = false;
    private static Set<Location> playerPlacedBlocks = new HashSet<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
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
        status.put(e.getPlayer().getUniqueId(), PlayerStatus.BEFORE_GAME);
        e.getPlayer().setGameMode(GameMode.ADVENTURE);
        e.getPlayer().setMaxHealth(20);
        Scoreboard board = manager.getNewScoreboard();
        e.getPlayer().setScoreboard(board);
        final Objective objective = board.registerNewObjective("scoreboard", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(""+ChatColor.GREEN + ChatColor.BOLD + "BED WARS");
        scoreboards.put(e.getPlayer().getUniqueId(), board);
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

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        playerPlacedBlocks.add(e.getBlockPlaced().getLocation());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        if (playerPlacedBlocks.contains(e.getBlock().getLocation())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "You can only break a block that placed by player.");
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent e) {
        e.setCancelled(false);
        e.blockList().forEach(block -> {
            if (playerPlacedBlocks.contains(block.getLocation())) block.breakNaturally();
        });
    }

    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent e) {
        Material type = e.getItem().getItemStack().getType();
        if (type == Material.GOLD_INGOT || type == Material.IRON_INGOT || type == Material.BRICK) {
            Team team = BedWars.team.get(e.getPlayer().getUniqueId());
            Location resourceSpawn = Utils.getConfigUtils().getGeneratorLocation(team.name());
            BedWars.team.values(team).removeReturnCollection(e.getPlayer().getUniqueId()).forEach((uuid, team1) -> {
                if (Bukkit.getPlayer(uuid).getLocation().distance(resourceSpawn) <= 4) {
                    Bukkit.getPlayer(uuid).getInventory().addItem(new ItemStack(type));
                }
            });
        }
    }
}
