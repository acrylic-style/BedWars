package xyz.acrylicstyle.bedwars;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import util.Collection;
import util.CollectionList;
import util.CollectionSync;
import util.CollectionStrictSync;
import xyz.acrylicstyle.bedwars.commands.*;
import xyz.acrylicstyle.bedwars.inventories.*;
import xyz.acrylicstyle.bedwars.tasks.GameTask;
import xyz.acrylicstyle.bedwars.tasks.LobbyTask;
import xyz.acrylicstyle.bedwars.upgrades.ReinforcedArmor;
import xyz.acrylicstyle.bedwars.utils.*;
import xyz.acrylicstyle.tomeito_core.providers.ConfigProvider;
import xyz.acrylicstyle.tomeito_core.utils.Log;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static xyz.acrylicstyle.bedwars.utils.Utils.*;

public class BedWars extends JavaPlugin implements Listener {
    public static ConfigProvider config = null;
    public static String mapName = null;
    public static ConfigProvider map = null;
    public static String worldName = null;
    public static World world = null;
    public static ScoreboardManager manager = null;
    public static Collection<UUID, Scoreboard> scoreboards = new Collection<>();
    public static CollectionSync<UUID, PlayerStatus> status = new CollectionSync<>();
    public static Collection<UUID, Team> team = new Collection<>();
    public static CollectionStrictSync<UUID, Integer> kills = new CollectionStrictSync<>();
    public static CollectionStrictSync<UUID, Integer> finalKills = new CollectionStrictSync<>();
    public static Set<Team> aliveTeam = new HashSet<>();
    public static boolean startedLobbyTask = false;
    private static Set<Location> playerPlacedBlocks = new HashSet<>();
    private static ItemShop itemShop = null;
    private static TeamUpgrades teamUpgrades = null;
    private final int restrictedRange = 7;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        for (Team team : Team.values()) {
            traps.add(team, new CollectionList<>());
        }
        itemShop = new ItemShop();
        teamUpgrades = new TeamUpgrades();
        Bukkit.getPluginManager().registerEvents(itemShop, this);
        Bukkit.getPluginManager().registerEvents(teamUpgrades, this);
        Bukkit.getPluginManager().registerEvents(Utils.gameModifiers, this);
        Bukkit.getPluginCommand("forcestart").setExecutor(new ForceStart());
        Bukkit.getPluginCommand("forcestop").setExecutor(new EndGame());
        Bukkit.getPluginCommand("setspawn").setExecutor(new SetSpawn());
        Bukkit.getPluginCommand("modifiers").setExecutor(new Modifiers());
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        String name = e.getPlayer().getDisplayName().equalsIgnoreCase("") ? e.getPlayer().getName() : e.getPlayer().getDisplayName();
        if (GameTask.playedTime > 0) {
            e.setJoinMessage(e.getPlayer().getDisplayName() + ChatColor.YELLOW + " reconnected.");
            e.getPlayer().damage(LocalDateTime.now().getYear()); // let player die and respawn
            return;
        }
        e.setJoinMessage(ChatColor.GRAY + name + ChatColor.YELLOW + " has joined (" + ChatColor.AQUA + Bukkit.getOnlinePlayers().size() + ChatColor.YELLOW + "/" + ChatColor.AQUA + maximumPlayers + ChatColor.YELLOW + ")!");
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
        Utils.setScoreReplace("Map: " + ChatColor.GREEN + BedWars.map.getString("name", "???"), 4, objective, e.getPlayer().getUniqueId());
        Utils.setScoreReplace("Players: " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size() + "/" + maximumPlayers, 3, objective, e.getPlayer().getUniqueId());
        Utils.setScoreReplace(" ", 2, objective, e.getPlayer().getUniqueId());
        Utils.setScoreReplace(ChatColor.YELLOW + BedWars.config.getString("domain", "www.acrylicstyle.xyz"), -1, objective, e.getPlayer().getUniqueId());
        if (Bukkit.getOnlinePlayers().size() < Utils.minimumPlayers) Utils.setScoreReplace(ChatColor.WHITE + "Waiting...", 1, objective, e.getPlayer().getUniqueId());
        Utils.setScoreReplace("", 0, objective, e.getPlayer().getUniqueId());
        scoreboards.put(e.getPlayer().getUniqueId(), board);
        Location spawnPoint = new Location(world, map.getDouble("spawn.x", 0), map.getDouble("spawn.y", 60), map.getDouble("spawn.z", 0));
        e.getPlayer().teleport(spawnPoint);
        if (e.getPlayer().isOp()) e.getPlayer().getInventory().addItem(Utils.getModifierItem());
        if (startedLobbyTask) return; // ---------- event ends here if lobbytask is already started ----------
        LobbyTask lobbyTask = new LobbyTask();
        Utils.setLobbyTask(lobbyTask);
        lobbyTask.runTaskTimer(this, 0, 20);
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        if (Bukkit.getOnlinePlayers().size() >= Utils.maximumPlayers) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Sorry! Current game is full, Please try again later!");
            return;
        }
        if (GameTask.playedTime > 0 && !team.containsKey(e.getPlayer().getUniqueId())) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Game is already started. Please try again later!");
        }
    }

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onPlayerMove(PlayerMoveEvent e) {
        Team closestTeam = Utils.getConfigUtils().getClosestTeam(e.getPlayer().getLocation());
        if (closestTeam == BedWars.team.get(e.getPlayer().getUniqueId())) return;
        final int restrictedRange2 = restrictedRange+10; // 17 blocks from spawn
        Location loc = Utils.getConfigUtils().getTeamSpawnPoint(closestTeam);
        Location loc2 = e.getPlayer().getLocation();
        double x = loc.getX();
        double x2 = loc2.getX();
        double y = loc.getY();
        double y2 = loc2.getY();
        double z = loc.getZ();
        double z2 = loc2.getZ();
        if (x-x2 >= -restrictedRange2 && x-x2 <= restrictedRange2 && y-y2 >= -restrictedRange2 && y-y2 <= restrictedRange2 && z-z2 >= -restrictedRange2 && z-z2 <= restrictedRange2) {
            if (Utils.traps.get(closestTeam).size() >= 1) {
                BedWars.team.values(closestTeam).foreachKeys((uuid, index) -> {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null) {
                        player.sendTitle("" + ChatColor.RED + ChatColor.BOLD + "TRAP TRIGGERED!", ChatColor.RED + "Trap has been set to off!");
                        player.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Trap has been set to off! " + ChatColor.GRAY + "(Available traps: " + (Utils.traps.get(closestTeam).size()-1) + ")");
                    }
                });
                CollectionList<Trap> traps = Utils.traps.get(closestTeam);
                Trap trap = traps.first();
                traps.remove(0);
                Utils.traps.add(closestTeam, traps);
                if (trap.getTrap() == Traps.MINING_FATIGUE) {
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10*20, 0, false));
                } else if (trap.getTrap() == Traps.BLIND) {
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10*20, 0, false));
                }
            }
        }
        if (y2 <= 0) e.getPlayer().damage(2019);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Team closestTeam = Utils.getConfigUtils().getClosestTeam(e.getPlayer().getLocation());
        Location loc = Utils.getConfigUtils().getTeamSpawnPoint(closestTeam);
        Location loc2 = e.getBlock().getLocation();
        double x = loc.getX();
        double x2 = loc2.getX();
        double y = loc.getY();
        double y2 = loc2.getY();
        double z = loc.getZ();
        double z2 = loc2.getZ();
        if (x-x2 >= -restrictedRange && x-x2 <= restrictedRange && y-y2 >= -restrictedRange && y-y2 <= restrictedRange && z-z2 >= -restrictedRange && z-z2 <= restrictedRange) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "You can't place block here!");
            return;
        }
        if (e.getBlockPlaced().getLocation().getBlockY() >= 150) {
            e.getPlayer().sendMessage(ChatColor.RED + "Build height limit reached!");
            e.setBuild(false);
            e.setCancelled(true);
            return;
        }
        playerPlacedBlocks.add(e.getBlockPlaced().getLocation());
        if (e.getBlockPlaced().getType() == Material.TNT) {
            e.getBlockPlaced().setType(Material.AIR);
            TNTPrimed tnt = e.getBlockPlaced().getWorld().spawn(e.getBlockPlaced().getLocation().add(0.5, 0, 0.5), TNTPrimed.class);
            tnt.setFuseTicks(80);
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        Team closestTeam = Utils.getConfigUtils().getClosestTeam(e.getPlayer().getLocation());
        Location loc = Utils.getConfigUtils().getTeamSpawnPoint(closestTeam);
        Location loc2 = e.getBlock().getLocation();
        double x = loc.getX();
        double x2 = loc2.getX();
        double y = loc.getY();
        double y2 = loc2.getY();
        double z = loc.getZ();
        double z2 = loc2.getZ();
        if (x-x2 >= -restrictedRange && x-x2 <= restrictedRange && y-y2 >= -restrictedRange && y-y2 <= restrictedRange && z-z2 >= -restrictedRange && z-z2 <= restrictedRange) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "You can't break block here!");
            return;
        }
        if (!playerPlacedBlocks.contains(e.getBlock().getLocation()) && e.getBlock().getType() != Material.BED_BLOCK && Utils.blockProtection) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "You can only break a block that placed by player.");
            return;
        }
        if (e.getBlock().getType() == Material.BED_BLOCK) {
            Team team = Utils.getConfigUtils().getTeamFromLocation(e.getBlock().getLocation());
            if (!aliveTeam.contains(team)) {
                Utils.destroyBed(e.getBlock());
                return;
            }
            Team theirTeam = BedWars.team.get(e.getPlayer().getUniqueId());
            if (team == theirTeam) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "You can't destroy your own bed!");
                return;
            }
            if (team == null) throw new NullPointerException("Unknown bed location: " + e.getBlock().getLocation().toString());
            BedWars.team.values(team).foreachKeys((uuid, i) -> Bukkit.getPlayer(uuid).sendTitle("" + ChatColor.RED + ChatColor.BOLD + "BED DESTROYED!", "You will no longer respawn!"));
            world.getPlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 100, 0.9F));
            aliveTeam.remove(team);
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("" + ChatColor.WHITE + ChatColor.BOLD + "BED DESTRUCTION > " + team.color + Utils.capitalize(team.name()) + " Bed " + ChatColor.GRAY + "was traded with milk by " + theirTeam.color + e.getPlayer().getName() + ChatColor.GRAY + "!");
            Bukkit.broadcastMessage("");
            Utils.destroyBed(e.getBlock());
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent e) {
        e.setCancelled(true);
        Team closestTeam = Utils.getConfigUtils().getClosestTeam(e.getBlock().getLocation());
        Location loc = Utils.getConfigUtils().getTeamSpawnPoint(closestTeam);
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        e.blockList().forEach(block -> {
            Location loc2 = block.getLocation();
            double x2 = loc2.getX();
            double y2 = loc2.getY();
            double z2 = loc2.getZ();
            if (!(x-x2 >= -restrictedRange && x-x2 <= restrictedRange && y-y2 >= -restrictedRange && y-y2 <= restrictedRange && z-z2 >= -restrictedRange && z-z2 <= restrictedRange)) {
                if (block.getType() != Material.GLASS && block.getType() != Material.TNT && !Utils.blockProtection) block.breakNaturally();
                if (block.getType() != Material.GLASS && block.getType() != Material.TNT && playerPlacedBlocks.contains(block.getLocation())) block.breakNaturally();
            }
        });
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        e.setCancelled(true);
        Team closestTeam = Utils.getConfigUtils().getClosestTeam(e.getLocation());
        Location loc = Utils.getConfigUtils().getTeamSpawnPoint(closestTeam);
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        e.blockList().forEach(block -> {
            Location loc2 = block.getLocation();
            double x2 = loc2.getX();
            double y2 = loc2.getY();
            double z2 = loc2.getZ();
            if (!(x-x2 >= -restrictedRange && x-x2 <= restrictedRange && y-y2 >= -restrictedRange && y-y2 <= restrictedRange && z-z2 >= -restrictedRange && z-z2 <= restrictedRange)) {
                if (block.getType() != Material.GLASS && block.getType() != Material.TNT && !Utils.blockProtection) block.breakNaturally();
                if (block.getType() != Material.GLASS && block.getType() != Material.TNT && playerPlacedBlocks.contains(block.getLocation())) block.breakNaturally();
            }
        });
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
        ((Player) e.getEntity()).setFoodLevel(20);
    }

    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent e) {
        Material type = e.getItem().getItemStack().getType();
        ItemStack item = e.getItem().getItemStack();
        ItemMeta meta = item.getItemMeta();
        if ((type == Material.GOLD_INGOT || type == Material.IRON_INGOT || type == Material.BRICK) && meta.spigot().isUnbreakable()) {
            meta.spigot().setUnbreakable(false);
            meta.removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            item.setItemMeta(meta);
            e.getItem().setItemStack(item);
            Team team = BedWars.team.get(e.getPlayer().getUniqueId());
            Location resourceSpawn = Utils.getConfigUtils().getGeneratorLocation(team.name().toLowerCase());
            BedWars.team.values(team).removeReturnCollection(e.getPlayer().getUniqueId()).forEach((uuid, team1) -> {
                if (Bukkit.getPlayer(uuid).getLocation().distance(resourceSpawn) <= 4) {
                    Bukkit.getPlayer(uuid).getInventory().addItem(new ItemStack(type));
                }
            });
        } else if ((type == Material.DIAMOND || type == Material.EMERALD) && meta.spigot().isUnbreakable()) {
            meta.spigot().setUnbreakable(false);
            meta.removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            item.setItemMeta(meta);
            e.getItem().setItemStack(item);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Team victimTeam = team.get(e.getEntity().getUniqueId());
        Player killer = e.getEntity().getKiller();
        e.setKeepInventory(true);
        final String finalKillMessage = aliveTeam.contains(victimTeam) ? "" : "" + ChatColor.AQUA + ChatColor.BOLD + " FINAL KILL!";
        final boolean voidKill = e.getDeathMessage().contains(" void ");
        if (killer == null) {
            if (voidKill) e.setDeathMessage(victimTeam.color + e.getEntity().getName() + ChatColor.GRAY + " fell into the void." + finalKillMessage); else e.setDeathMessage(victimTeam.color + e.getEntity().getName() + ChatColor.GRAY + " died." + finalKillMessage);
        } else {
            if (killer.getUniqueId().equals(e.getEntity().getUniqueId())) {
                e.setDeathMessage(victimTeam.color + e.getEntity().getName() + ChatColor.GRAY + " took their own life." + finalKillMessage);
                Utils.run(l -> e.getEntity().spigot().respawn());
                return;
            }
            if (finalKillMessage.length() <= 0) kills.add(killer.getUniqueId(), kills.getOrDefault(killer.getUniqueId(), 0)+1);
            if (finalKillMessage.length() >= 1) finalKills.add(killer.getUniqueId(), finalKills.getOrDefault(killer.getUniqueId(), 0)+1);
            if (Utils.teams < 6) Utils.setScoreReplace(ChatColor.YELLOW + "Kills: " + ChatColor.AQUA + kills.get(killer.getUniqueId()), 3, getObjective(killer.getUniqueId()), killer.getUniqueId());
            if (Utils.teams < 7) Utils.setScoreReplace(ChatColor.YELLOW + "Final Kills: " + ChatColor.AQUA + finalKills.get(killer.getUniqueId()), 2, getObjective(killer.getUniqueId()), killer.getUniqueId());
            Team killerTeam = team.get(killer.getUniqueId());
            if (voidKill) e.setDeathMessage(victimTeam.color + e.getEntity().getName() + ChatColor.GRAY + " was knocked into the void by " + (killerTeam == null ? "" : killerTeam.color) + killer.getName() + ChatColor.GRAY + "." + finalKillMessage);
                else e.setDeathMessage(victimTeam.color + e.getEntity().getName() + ChatColor.GRAY + " was killed by " + (killerTeam == null ? "" : killerTeam.color) + killer.getName() + ChatColor.GRAY + "." + finalKillMessage);
            int diamonds = Utils.countItems(Utils.all(e.getEntity().getInventory().getContents(), Material.DIAMOND));
            int emeralds = Utils.countItems(Utils.all(e.getEntity().getInventory().getContents(), Material.EMERALD));
            int irons = Utils.countItems(Utils.all(e.getEntity().getInventory().getContents(), Material.IRON_INGOT));
            int golds = Utils.countItems(Utils.all(e.getEntity().getInventory().getContents(), Material.GOLD_INGOT));
            e.getEntity().getInventory().clear();
            if (diamonds >= 1) {
                killer.sendMessage(ChatColor.AQUA + "+" + diamonds + " Diamond");
                killer.getInventory().addItem(new ItemStack(Material.DIAMOND, diamonds));
            }
            if (emeralds >= 1) {
                killer.sendMessage(ChatColor.GREEN + "+" + emeralds + " Emerald");
                killer.getInventory().addItem(new ItemStack(Material.EMERALD, emeralds));
            }
            if (golds >= 1) {
                killer.sendMessage(ChatColor.GOLD + "+" + golds + " Gold");
                killer.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, golds));
            }
            if (irons >= 1) {
                killer.sendMessage(ChatColor.WHITE + "+" + irons + " Iron");
                killer.getInventory().addItem(new ItemStack(Material.IRON_INGOT, irons));
            }
        }
        Utils.run(l -> e.getEntity().spigot().respawn());
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Utils.run(a -> {
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            e.getPlayer().getInventory().clear();
        });
        if (aliveTeam.contains(team.get(e.getPlayer().getUniqueId()))) {
            AtomicInteger integer = new AtomicInteger(Utils.respawnTime);
            new BukkitRunnable() {
                @SuppressWarnings("deprecation")
                public void run() {
                    if (integer.get() <= 0) {
                        if (Utils.speed != 0) e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, Utils.speed-1, false));
                        if (Utils.onePunchOneKill) e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100000, 127, false));
                        if (Utils.strength != 0) e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100000, Utils.strength-1, false));
                        e.getPlayer().setGameMode(GameMode.SURVIVAL);
                        e.getPlayer().teleport(Utils.getConfigUtils().getTeamSpawnPoint(BedWars.team.get(e.getPlayer().getUniqueId())));
                        if (!Constants.keepInventory) {
                            Player player = e.getPlayer();
                            ItemStack boots;
                            ItemStack leggings;
                            PlayerArmor armor = Constants.wearingArmor.getOrDefault(player.getUniqueId(), PlayerArmor.LEATHER);
                            int tier = ReinforcedArmor.getTierStatic(team.get(e.getPlayer().getUniqueId()));
                            if (armor == PlayerArmor.LEATHER) {
                                boots = Utils.enchantTool(Utils.getColoredLeatherArmor(Material.LEATHER_BOOTS, BedWars.team.get(player.getUniqueId())), Enchantment.PROTECTION_ENVIRONMENTAL, tier == 0 ? null : tier);
                                leggings = Utils.enchantTool(Utils.getColoredLeatherArmor(Material.LEATHER_LEGGINGS, BedWars.team.get(player.getUniqueId())), Enchantment.PROTECTION_ENVIRONMENTAL, tier == 0 ? null : tier);
                            } else if (armor == PlayerArmor.CHAIN) {
                                boots = Utils.enchantTool(Material.CHAINMAIL_BOOTS, Enchantment.PROTECTION_ENVIRONMENTAL, tier == 0 ? null : tier);
                                leggings = Utils.enchantTool(Material.CHAINMAIL_LEGGINGS, Enchantment.PROTECTION_ENVIRONMENTAL, tier == 0 ? null : tier);
                            } else if (armor == PlayerArmor.IRON) {
                                boots = Utils.enchantTool(Material.IRON_BOOTS, Enchantment.PROTECTION_ENVIRONMENTAL, tier == 0 ? null : tier);
                                leggings = Utils.enchantTool(Material.IRON_LEGGINGS, Enchantment.PROTECTION_ENVIRONMENTAL, tier == 0 ? null : tier);
                            } else if (armor == PlayerArmor.DIAMOND) {
                                boots = Utils.enchantTool(Material.DIAMOND_BOOTS, Enchantment.PROTECTION_ENVIRONMENTAL, tier == 0 ? null : tier);
                                leggings = Utils.enchantTool(Material.DIAMOND_LEGGINGS, Enchantment.PROTECTION_ENVIRONMENTAL, tier == 0 ? null : tier);
                            } else { // impossible
                                boots = Utils.enchantTool(Material.LEATHER_BOOTS, Enchantment.PROTECTION_ENVIRONMENTAL, tier == 0 ? null : tier);
                                leggings = Utils.enchantTool(Material.LEATHER_LEGGINGS, Enchantment.PROTECTION_ENVIRONMENTAL, tier == 0 ? null : tier);
                            }
                            player.getInventory().setBoots(boots);
                            player.getInventory().setLeggings(leggings);
                            player.getInventory().setChestplate(Utils.getColoredLeatherArmor(Material.LEATHER_CHESTPLATE, BedWars.team.get(player.getUniqueId())));
                            player.getInventory().setHelmet(Utils.enchantTool(Utils.getColoredLeatherArmor(Material.LEATHER_HELMET, BedWars.team.get(player.getUniqueId())), Enchantment.WATER_WORKER, 1));
                            player.getInventory().addItem(Utils.unbreakable(Material.WOOD_SWORD));
                            if (Utils.teleportMadness) player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 16), new ItemStack(Material.ENDER_PEARL, 16), new ItemStack(Material.ENDER_PEARL, 16), new ItemStack(Material.ENDER_PEARL, 16));
                        }
                        this.cancel();
                    } else {
                        int number = integer.getAndDecrement();
                        String subtitle = ChatColor.YELLOW + "You will respawn in " + ChatColor.RED + number + ChatColor.YELLOW + " seconds!";
                        e.getPlayer().sendTitle(ChatColor.RED + "YOU DIED!", subtitle);
                        e.getPlayer().sendMessage(subtitle);
                    }
                }
            }.runTaskTimer(this, 0, 20);
        } else {
            Team team = BedWars.team.remove(e.getPlayer().getUniqueId());
            e.getPlayer().sendTitle(ChatColor.RED + "YOU DIED!", "");
            e.getPlayer().sendMessage(ChatColor.RED + "You have been eliminated!");
            if (BedWars.team.values(Team.AQUA).size() <= 0) {
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage("" + ChatColor.WHITE + ChatColor.BOLD + "TEAM ELIMINATED > " + team.color + Utils.capitalize(team.name()) + " Team " + ChatColor.RED + "has been eliminated!");
                Bukkit.broadcastMessage("");
            }
        }
        Set<Team> teams = new HashSet<>(team.values());
        if (teams.size() <= 1) Utils.run(l -> Utils.endGame());
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
            Team team = BedWars.team.get(e.getPlayer().getUniqueId());
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
            Team team = BedWars.team.get(e.getPlayer().getUniqueId());
            e.getPlayer().openInventory(teamUpgrades.prepare(team).getInventory());
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || Utils.ended) {
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
        || event.getItemDrop().getItemStack().getType() == Material.WOOD_SWORD
        || event.getItemDrop().getItemStack().equals(Utils.getModifierItem())) event.setCancelled(true);
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent e) {
        e.setMaxPlayers(maximumPlayers);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getItem() != null && e.getItem().equals(Utils.getModifierItem())) {
            e.setCancelled(true);
            e.getPlayer().openInventory(Utils.gameModifiers.getInventory());
            return;
        }
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getItem() == null) return;
            if (e.getItem().getType() == Material.FIREBALL) {
                e.getPlayer().getInventory().removeItem(new ItemStack(Material.FIREBALL, 1));
                e.setCancelled(true);
                Fireball fireball = e.getPlayer().launchProjectile(Fireball.class);
                fireball.setVelocity(fireball.getVelocity().multiply(8));
                fireball.setYield(2);
                fireball.setFireTicks(32000);
            }
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.BED_BLOCK
            || e.getClickedBlock().getType() == Material.BED) if (!e.getPlayer().isSneaking()) e.setCancelled(true);
            if (e.getClickedBlock().getType() == Material.WORKBENCH && !Utils.crafting) e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Team team = BedWars.team.get(e.getPlayer().getUniqueId());
        String teamName = team != null ? team.color + "[" + team.name() + "] " + ChatColor.GRAY : ChatColor.GRAY + "[SPECTATOR] ";
        Player player = e.getPlayer();
        player.setPlayerListName(BedWars.team.get(player.getUniqueId()).color + player.getName());
        if (team != null) e.setMessage(teamName + e.getPlayer().getDisplayName() + ChatColor.WHITE + ": " + ChatColor.GRAY + e.getMessage());
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent e) {
        if (!Utils.crafting) e.getInventory().setResult(new ItemStack(Material.AIR));
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent e) {
        if (e.getItem().getItemMeta() instanceof PotionMeta) {
            ((PotionMeta) e.getItem().getItemMeta()).getCustomEffects().forEach(potionEffect -> {
                if (potionEffect.getType() == PotionEffectType.INVISIBILITY) {
                    for (Player player : Bukkit.getOnlinePlayers())
                        player.hidePlayer(e.getPlayer());
                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            for (Player player : Bukkit.getOnlinePlayers())
                                player.showPlayer(e.getPlayer());
                        }
                    };
                    timer.schedule(timerTask, potionEffect.getDuration() / 20 * 1000);
                }
            });
        } else if (e.getItem().getType() == Material.MILK_BUCKET) {
            for (Player player : Bukkit.getOnlinePlayers())
                player.showPlayer(e.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (GameTask.playedTime > 0) {
            e.setQuitMessage(e.getPlayer().getPlayerListName() + ChatColor.YELLOW + " disconnected");
        } else {
            String name = e.getPlayer().getDisplayName().equalsIgnoreCase("") ? e.getPlayer().getName() : e.getPlayer().getDisplayName();
            e.setQuitMessage(ChatColor.GRAY + name + ChatColor.YELLOW + " has left!");
        }
        Utils.removeScores(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (Utils.ended) e.setCancelled(true);
    }
}
