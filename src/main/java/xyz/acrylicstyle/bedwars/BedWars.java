package xyz.acrylicstyle.bedwars;

import org.bukkit.*;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import xyz.acrylicstyle.bedwars.inventories.ItemShop;
import xyz.acrylicstyle.bedwars.tasks.GameTask;
import xyz.acrylicstyle.bedwars.tasks.LobbyTask;
import xyz.acrylicstyle.bedwars.utils.Collection;
import xyz.acrylicstyle.bedwars.utils.PlayerStatus;
import xyz.acrylicstyle.bedwars.utils.Team;
import xyz.acrylicstyle.bedwars.utils.Utils;
import xyz.acrylicstyle.tomeito_core.providers.ConfigProvider;
import xyz.acrylicstyle.tomeito_core.utils.Log;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    private static ItemShop itemShop = null;

    @Override
    public void onEnable() {
        itemShop = new ItemShop();
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(itemShop, this);
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
        e.getPlayer().getInventory().clear();
        e.getPlayer().getInventory().setArmorContents(new ItemStack[]{new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)});
        e.getPlayer().getActivePotionEffects().forEach(effect -> e.getPlayer().removePotionEffect(effect.getType()));
        Scoreboard board = manager.getNewScoreboard();
        e.getPlayer().setScoreboard(board);
        final Objective objective = board.registerNewObjective("scoreboard", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(""+ChatColor.YELLOW + ChatColor.BOLD + "BED WARS");
        scoreboards.put(e.getPlayer().getUniqueId(), board);
        if (startedLobbyTask) return;
        LobbyTask lobbyTask = new LobbyTask();
        Utils.setLobbyTask(lobbyTask);
        lobbyTask.runTaskTimer(this, 0, 20);
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        if (Bukkit.getOnlinePlayers().size() >= Utils.maximumPlayers) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Current game is full. Please try again later!");
            return;
        }
        if (GameTask.playedTime > 0) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Game is already started. Please try again later!");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        playerPlacedBlocks.add(e.getBlockPlaced().getLocation());
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        if (!playerPlacedBlocks.contains(e.getBlock().getLocation()) && e.getBlock().getType() != Material.BED_BLOCK) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "You can only break a block that placed by player.");
        }
        if (e.getBlock().getType() == Material.BED_BLOCK) {
            Team team = Utils.getConfigUtils().getTeamFromLocation(e.getBlock().getLocation());
            if (team == null) throw new NullPointerException("Unknown bed location: " + e.getBlock().getLocation().toString());
            BedWars.team.values(team).foreachKeys((uuid, i) -> Bukkit.getPlayer(uuid).sendTitle("" + ChatColor.RED + ChatColor.BLUE + "BED DESTROYED!", "You will no longer respawn!"));
            Team theirTeam = BedWars.team.get(e.getPlayer().getUniqueId());
            world.getPlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 100, 1));
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("" + ChatColor.WHITE + ChatColor.BOLD + "BED DESTRUCTION > " + team.color + Utils.capitalize(team.name()) + " Bed " + ChatColor.GRAY + "was traded with milk by " + theirTeam.color + e.getPlayer().getName() + ChatColor.GRAY + "!");
            Bukkit.broadcastMessage("");
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
        ItemStack item = e.getItem().getItemStack();
        ItemMeta meta = item.getItemMeta();
        meta.spigot().setUnbreakable(false);
        meta.removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        e.getItem().setItemStack(item);
        if (type == Material.GOLD_INGOT || type == Material.IRON_INGOT || type == Material.BRICK) {
            Team team = BedWars.team.get(e.getPlayer().getUniqueId());
            Location resourceSpawn = Utils.getConfigUtils().getGeneratorLocation(team.name());
            BedWars.team.values(team).removeReturnCollection(e.getPlayer().getUniqueId()).forEach((uuid, team1) -> {
                if (Bukkit.getPlayer(uuid).getLocation().distanceSquared(resourceSpawn) <= 4) {
                    Bukkit.getPlayer(uuid).getInventory().addItem(new ItemStack(type));
                }
            });
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        e.setRespawnLocation(Utils.getConfigUtils().getTeamSpawnPoint(BedWars.team.get(e.getPlayer().getUniqueId())));
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.getInventory().getType() == InventoryType.MERCHANT && e.getInventory().getHolder() instanceof Villager) {
            e.setCancelled(true);
            if (((Villager) e.getInventory().getHolder()).getCustomName().equalsIgnoreCase("" + ChatColor.YELLOW + ChatColor.BOLD + "ITEM SHOP")) {
                itemShop.openInventory(Bukkit.getPlayer(e.getPlayer().getUniqueId()));
            }
        }
    }
}
