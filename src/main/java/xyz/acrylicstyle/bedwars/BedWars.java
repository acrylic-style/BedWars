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

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static xyz.acrylicstyle.bedwars.utils.Utils.getInstance;
import static xyz.acrylicstyle.bedwars.utils.Utils.teamSize;

public class BedWars extends JavaPlugin implements Listener {
    private boolean enabled = false;
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

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        itemShop = new ItemShop();
        teamUpgrades = new TeamUpgrades();
        Bukkit.getPluginManager().registerEvents(itemShop, this);
        Bukkit.getPluginManager().registerEvents(teamUpgrades, this);
        Bukkit.getPluginCommand("forcestart").setExecutor(new ForceStart());
        Bukkit.getPluginCommand("forcestop").setExecutor(new EndGame());
        Bukkit.getPluginCommand("setspawn").setExecutor(new SetSpawn());
        Bukkit.getPluginCommand("seteventtime").setExecutor(new SetEventTime());
        Bukkit.getPluginCommand("toggleblockprotection").setExecutor(new ToggleBlockProtection());
        Bukkit.getPluginCommand("setrespawntime").setExecutor(new SetRespawnTime());
        Bukkit.getPluginCommand("togglecrafting").setExecutor(new ToggleCrafting());
        new BukkitRunnable() {
            public void run() {
                try {
                    Utils.reset();
                    enabled = true;
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
        e.setJoinMessage(ChatColor.GRAY + name + ChatColor.YELLOW + " has joined (" + ChatColor.AQUA + Bukkit.getOnlinePlayers().size() + ChatColor.YELLOW + "/" + ChatColor.AQUA + (teamSize*8) + ChatColor.YELLOW + ")!");
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
        Utils.setScoreReplace("Players: " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size() + "/" + (Utils.teamSize*8), 3, objective, e.getPlayer().getUniqueId());
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
        if (GameTask.playedTime > 0) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Game is already started. Please try again later!");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getBlockPlaced().getLocation().getBlockY() >= 100) {
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
            world.getPlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 100, 1));
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
        e.blockList().forEach(block -> {
            if (block.getType() != Material.GLASS && block.getType() != Material.TNT && Utils.blockProtection && playerPlacedBlocks.contains(block.getLocation())) block.breakNaturally();
        });
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        e.setCancelled(true);
        e.blockList().forEach(block -> {
            if (block.getType() != Material.GLASS && block.getType() != Material.TNT && Utils.blockProtection && playerPlacedBlocks.contains(block.getLocation())) block.breakNaturally();
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
        if (killer == null) {
            e.setDeathMessage(victimTeam.color + e.getEntity().getName() + ChatColor.GRAY + " died." + finalKillMessage);
        } else {
            if (killer.getUniqueId().equals(e.getEntity().getUniqueId())) {
                e.setDeathMessage(victimTeam.color + e.getEntity().getName() + ChatColor.GRAY + " took their own life." + finalKillMessage);
                Utils.run(l -> e.getEntity().spigot().respawn());
                return;
            }
            if (finalKillMessage.length() <= 0) kills.add(killer.getUniqueId(), kills.getOrDefault(killer.getUniqueId(), 0)+1);
            if (finalKillMessage.length() >= 1) finalKills.add(killer.getUniqueId(), kills.getOrDefault(killer.getUniqueId(), 0)+1);
            Team killerTeam = team.get(killer.getUniqueId());
            e.setDeathMessage(victimTeam.color + e.getEntity().getName() + ChatColor.GRAY + " was killed by " + (killerTeam == null ? "" : killerTeam.color) + killer.getName() + ChatColor.GRAY + "." + finalKillMessage);
            CollectionList<ItemStack> diamonds = CollectionList.fromValues(e.getEntity().getInventory().all(Material.DIAMOND));
            CollectionList<ItemStack> emeralds = CollectionList.fromValues(e.getEntity().getInventory().all(Material.EMERALD));
            CollectionList<ItemStack> golds = CollectionList.fromValues(e.getEntity().getInventory().all(Material.GOLD_INGOT));
            CollectionList<ItemStack> irons = CollectionList.fromValues(e.getEntity().getInventory().all(Material.IRON_INGOT));
            e.getEntity().getInventory().clear();
            if (diamonds.size() >= 1) {
                killer.sendMessage(ChatColor.AQUA + "+" + diamonds.size() + " Diamond");
                killer.getInventory().addItem(new ItemStack(Material.DIAMOND, diamonds.size()));
            }
            if (emeralds.size() >= 1) {
                killer.sendMessage(ChatColor.GREEN + "+" + emeralds.size() + " Emerald");
                killer.getInventory().addItem(new ItemStack(Material.EMERALD, emeralds.size()));
            }
            if (golds.size() >= 1) {
                killer.sendMessage(ChatColor.GOLD + "+" + golds.size() + " Gold");
                killer.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, golds.size()));
            }
            if (irons.size() >= 1) {
                killer.sendMessage(ChatColor.WHITE + "+" + irons.size() + " Iron");
                killer.getInventory().addItem(new ItemStack(Material.IRON_INGOT, irons.size()));
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
        if (GameTask.playedTime > 0) {
            e.setMaxPlayers(0);
        } else if (enabled) {
            e.setMaxPlayers(teamSize*8);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getItem() == null)
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
        if (team != null) e.setFormat(teamName + e.getPlayer().getDisplayName() + ChatColor.WHITE + ": " + ChatColor.GRAY + e.getMessage());
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
        String name = e.getPlayer().getDisplayName().equalsIgnoreCase("") ? e.getPlayer().getName() : e.getPlayer().getDisplayName();
        e.setQuitMessage(ChatColor.GRAY + name + ChatColor.YELLOW + " has left (" + ChatColor.AQUA + Bukkit.getOnlinePlayers().size() + ChatColor.YELLOW + "/" + ChatColor.AQUA + (teamSize*8) + ChatColor.YELLOW + ")!");
        Utils.removeScores(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (Utils.ended) e.setCancelled(true);
    }
}
