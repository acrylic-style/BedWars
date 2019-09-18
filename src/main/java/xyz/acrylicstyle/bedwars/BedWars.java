package xyz.acrylicstyle.bedwars;

import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
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
import xyz.acrylicstyle.bedwars.inventories.TeamUpgrades;
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
import java.util.concurrent.atomic.AtomicInteger;

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
    private static TeamUpgrades teamUpgrades = null;

    @Override
    public void onEnable() {
        itemShop = new ItemShop();
        teamUpgrades = new TeamUpgrades();
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
            e.getBlock().breakNaturally();
            Utils.run(a -> {
                java.util.Collection<Item> items = e.getBlock().getWorld().getEntitiesByClass(Item.class);
                items.forEach(item -> {
                    if (item.getItemStack().getType() == Material.BED) item.remove();
                });
            });
            Team team = Utils.getConfigUtils().getTeamFromLocation(e.getBlock().getLocation());
            if (team == null) throw new NullPointerException("Unknown bed location: " + e.getBlock().getLocation().toString());
            BedWars.team.values(team).foreachKeys((uuid, i) -> Bukkit.getPlayer(uuid).sendTitle("" + ChatColor.RED + ChatColor.BOLD + "BED DESTROYED!", "You will no longer respawn!"));
            Team theirTeam = BedWars.team.get(e.getPlayer().getUniqueId());
            world.getPlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 100, 1));
            aliveTeam.remove(team);
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("" + ChatColor.WHITE + ChatColor.BOLD + "BED DESTRUCTION > " + team.color + Utils.capitalize(team.name()) + " Bed " + ChatColor.GRAY + "was traded with milk by " + theirTeam.color + e.getPlayer().getName() + ChatColor.GRAY + "!");
            Bukkit.broadcastMessage("");
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent e) {
        e.setCancelled(false);
        e.blockList().forEach(block -> {
            if (block.getType() != Material.GLASS && playerPlacedBlocks.contains(block.getLocation())) block.breakNaturally();
        });
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        e.setCancelled(false);
        e.blockList().forEach(block -> {
            if (block.getType() != Material.GLASS && playerPlacedBlocks.contains(block.getLocation())) block.breakNaturally();
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
    public void onPlayerDeath(PlayerDeathEvent e) {
        Team victimTeam = team.get(e.getEntity().getUniqueId());
        Player killer = e.getEntity().getKiller();
        if (killer == null) {
            e.setDeathMessage(victimTeam.color + e.getEntity().getName() + ChatColor.GRAY + " died.");
        } else {
            Team killerTeam = team.get(killer.getUniqueId());
            e.setDeathMessage(victimTeam.color + e.getEntity().getName() + ChatColor.GRAY + " was killed by " + (killerTeam == null ? "" : killerTeam.color) + killer.getName() + ChatColor.GRAY + ".");
        }
        Utils.run(l -> e.getEntity().spigot().respawn());
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Utils.run(a -> e.getPlayer().setGameMode(GameMode.SPECTATOR));
        if (aliveTeam.contains(team.get(e.getPlayer().getUniqueId()))) {
            AtomicInteger integer = new AtomicInteger(5);
            new BukkitRunnable() {
                @SuppressWarnings("deprecation")
                public void run() {
                    if (integer.get() <= 0) {
                        e.getPlayer().setGameMode(GameMode.SURVIVAL);
                        e.getPlayer().teleport(Utils.getConfigUtils().getTeamSpawnPoint(BedWars.team.get(e.getPlayer().getUniqueId())));
                        this.cancel();
                        return;
                    }
                    int number = integer.getAndDecrement();
                    String subtitle = ChatColor.YELLOW + "You will respawn in " + ChatColor.RED + number + ChatColor.YELLOW + " seconds!";
                    e.getPlayer().sendTitle(ChatColor.RED + "YOU DIED!", subtitle);
                    e.getPlayer().sendMessage(subtitle);
                }
            }.runTaskTimer(this, 0, 20);
        } else {
            Team team = BedWars.team.remove(e.getPlayer().getUniqueId());
            e.getPlayer().sendTitle(ChatColor.RED + "YOU DIED!", "");
            e.getPlayer().sendMessage(ChatColor.RED + "You've been eliminated!");
            if (BedWars.team.values(Team.AQUA).size() <= 0) {
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage("" + ChatColor.WHITE + ChatColor.BOLD + "TEAM ELIMINATED > " + team.color + Utils.capitalize(team.name()) + "Team " + ChatColor.RED + "has been eliminated!");
                Bukkit.broadcastMessage("");
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.getInventory().getType() == InventoryType.MERCHANT && e.getInventory().getHolder() instanceof Villager) {
            e.setCancelled(true);
            if (((Villager) e.getInventory().getHolder()).getCustomName().equalsIgnoreCase("" + ChatColor.YELLOW + ChatColor.BOLD + "ITEM SHOP")) {
                e.getPlayer().openInventory(itemShop.getInventory());
            } else if (((Villager) e.getInventory().getHolder()).getCustomName().equalsIgnoreCase("" + ChatColor.YELLOW + ChatColor.BOLD + "TEAM UPGRADES")) {
                Team team = Team.valueOf(((Villager) e.getInventory().getHolder()).getMetadata("team").get(0).asString());
                e.getPlayer().openInventory(teamUpgrades.prepare(team).getInventory());
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof Villager)) return;
        e.setCancelled(true);
        if (e.getRightClicked().getCustomName().equalsIgnoreCase("" + ChatColor.YELLOW + ChatColor.BOLD + "ITEM SHOP")) {
            e.getPlayer().openInventory(itemShop.getInventory());
        } else if (e.getRightClicked().getCustomName().equalsIgnoreCase("" + ChatColor.YELLOW + ChatColor.BOLD + "TEAM UPGRADES")) {
            Team team = Team.valueOf(e.getRightClicked().getMetadata("team").get(0).asString());
            e.getPlayer().openInventory(teamUpgrades.prepare(team).getInventory());
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
        if (!(e.getRightClicked() instanceof Villager)) return;
        e.setCancelled(true);
        if (e.getRightClicked().getCustomName().equalsIgnoreCase("" + ChatColor.YELLOW + ChatColor.BOLD + "ITEM SHOP")) {
            e.getPlayer().openInventory(itemShop.getInventory());
        } else if (e.getRightClicked().getCustomName().equalsIgnoreCase("" + ChatColor.YELLOW + ChatColor.BOLD + "TEAM UPGRADES")) {
            Team team = Team.valueOf(e.getRightClicked().getMetadata("team").get(0).asString());
            e.getPlayer().openInventory(teamUpgrades.prepare(team).getInventory());
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            e.setCancelled(true);
            return;
        }
        if (!(e.getDamager() instanceof Player)) return;
        Player damager = (Player) e.getDamager();
        Player player = (Player) e.getEntity();
        if (team.get(damager.getUniqueId()) == team.get(player.getUniqueId())) e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getSlotType() == InventoryType.SlotType.ARMOR && e.getWhoClicked().getGameMode() != GameMode.CREATIVE) e.setCancelled(true);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() == Material.LEATHER_CHESTPLATE
        || event.getItemDrop().getItemStack().getType() == Material.LEATHER_HELMET
        || event.getItemDrop().getItemStack().getType() == Material.WOOD_SWORD) event.setCancelled(true);
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent e) {
        if (GameTask.playedTime > 0) {
            e.setMaxPlayers(0);
        }
    }
}
