package xyz.acrylicstyle.bedwars.utils;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import util.Collection;
import util.CollectionList;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.inventories.GameModifiers;
import xyz.acrylicstyle.bedwars.tasks.GameTask;
import xyz.acrylicstyle.bedwars.tasks.LobbyTask;
import xyz.acrylicstyle.tomeito_core.providers.ConfigProvider;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Consumer;

public final class Utils {
    private Utils() {}

    private static GameTask _gameTask = null;
    private static LobbyTask _lobbyTask = null;
    private static ConfigUtils configUtils = null;
    private static Collection<String, Hologram> holograms = new Collection<>();
    public static Collection<Team, CollectionList<Trap>> traps = new Collection<>();

    public static int maximumPlayers = 16;
    public static int minimumPlayers = 4;
    public static int teamSize = 2; // doubles
    public static boolean ended = false;
    public static float eventTime = 1; // 1x faster
    public static boolean blockProtection = true;
    public static int respawnTime = 5;
    public static boolean crafting = false;
    public static boolean maxTeamUpgrades = false;
    public static GameModifiers gameModifiers = new GameModifiers();

    private final static char heavy_X = '\u2718';
    private final static char check = '\u2714';

    public static BedWars getInstance() {
        return BedWars.getPlugin(BedWars.class);
    }

    public static void setGameTask(GameTask task) {
        Utils._gameTask = task;
    }

    public static void setLobbyTask(LobbyTask task) {
        Utils._lobbyTask = task;
    }

    public static GameTask getGameTask() {
        return Utils._gameTask;
    }

    public static LobbyTask getLobbyTask() {
        return Utils._lobbyTask;
    }

    private static void initConfigUtils() throws IOException, InvalidConfigurationException {
        Utils.configUtils = new ConfigUtils("./plugins/BedWars/maps/" + BedWars.mapName + ".yml");
    }

    public static ConfigUtils getConfigUtils() {
        if (Utils.configUtils == null) throw new NullPointerException("ConfigUtils isn't initialized yet!");
        return Utils.configUtils;
    }

    public static String secondsToTime(int seconds) {
        int minutes = (int) Math.floor((float) seconds / 60F);
        String sec = Integer.toString(seconds % 60);
        return minutes + ":" + (sec.length() == 1 ? "0" + sec : sec);
    }

    public static void reset() throws IOException, InvalidConfigurationException {
        BedWars.config = new ConfigProvider("./plugins/BedWars/config.yml");
        BedWars.mapName = BedWars.config.getString("map");
        if (BedWars.mapName == null)
            throw new NullPointerException("A map field(string) in config.yml is required.");
        BedWars.map = new ConfigProvider("./plugins/BedWars/maps/" + BedWars.mapName + ".yml");
        BedWars.worldName = BedWars.map.getString("world");
        if (BedWars.worldName == null)
            throw new NullPointerException("A world field(string) in maps/" + BedWars.mapName + ".yml is required.");
        BedWars.world = Bukkit.getWorld(BedWars.worldName);
        if (BedWars.world == null)
            throw new NullPointerException("World is null. (Probably invalid world name were provided)");
        BedWars.world.setPVP(true);
        BedWars.world.setMonsterSpawnLimit(0);
        BedWars.world.setGameRuleValue("doMobSpawning", "false");
        BedWars.world.setGameRuleValue("keepInventory", "true"); // for delete all items when death
        BedWars.world.setGameRuleValue("mobGriefing", "true");
        BedWars.world.setFullTime(6000);
        BedWars.manager = Bukkit.getScoreboardManager();
        Utils.maximumPlayers = BedWars.config.getInt("maximumPlayers", 16);
        Utils.minimumPlayers = BedWars.config.getInt("minimumPlayers", 4);
        Utils.teamSize = BedWars.map.getInt("teamSize", 2);
        Utils.eventTime = BedWars.map.getInt("eventTime", 1);
        Utils.blockProtection = BedWars.map.getBoolean("blockProtection", true);
        Utils.crafting = BedWars.map.getBoolean("crafting", false);
        for (Team team : Team.values()) {
            traps.add(team, new CollectionList<>());
        }
        GameTask.playedTime = 0;
        Utils.initConfigUtils();
        BedWars.scoreboards = new Collection<>();
        if (Bukkit.getOnlinePlayers().size() > 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setGameMode(GameMode.ADVENTURE);
                BedWars.scoreboards.put(player.getUniqueId(), BedWars.manager.getNewScoreboard());
            }
        }
    }

    /**
     * @param consumer Consumer without any args. <b>It won't pass any arguments.</b>
     */
    public static void run(Consumer<?> consumer) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Utils.getInstance(), () -> consumer.accept(null), 1);
    }

    public static <T extends CommandSender> boolean run(ThrowableRunnable consumer, T player, Errors error) {
        final boolean[] type = {true};
        try {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Utils.getInstance(), () -> {
                try {
                    consumer.run();
                } catch (Throwable e) {
                    e.printStackTrace();
                    Utils.sendError(player, error);
                    type[0] = false;
                }
            }, 1);
        } catch (Throwable e) {
            e.printStackTrace();
            Utils.sendError(player, Errors.SCHEDULER_ERROR);
            type[0] = false;
        }
        return type[0];
    }

    private static <T extends CommandSender> void sendError(T player, Errors error) {
        player.sendMessage(ChatColor.RED + "Couldn't run this command! Please try again later. (" + error.toString() + ")");
    }

    private interface ThrowableRunnable {
        void run() throws Exception;
    }

    private static Collection<UUID, Collection<Integer, String>> scores = new Collection<>();

    /**
     * @param name A name to register / remove score.
     * @param score Score for set score. null to remove the score.
     * @param objective Objective for set score
     */
    public static void setScoreReplace(String name, Integer score, Objective objective, UUID uuid) {
        final String name2 = name + "        ";
        if (score == null) {
            objective.getScoreboard().resetScores(name2);
            return;
        }
        if (scores.get(uuid) == null) scores.put(uuid, new Collection<>());
        if (scores.get(uuid).get(score) != null) {
            if (scores.get(uuid).get(score).equalsIgnoreCase(name2)) return; // return if name is same as last score entry
            objective.getScoreboard().resetScores(scores.get(uuid).get(score));
        }
        Score scoreObj = objective.getScore(name2);
        scoreObj.setScore(score);
        Collection<Integer, String> collection = scores.get(uuid);
        collection.put(score, name2);
        scores.put(uuid, collection);
    }

    public static Collection<UUID, Collection<Integer, String>> getScores() {
        return scores.clone();
    }

    public static Collection<UUID, Collection<Integer, String>> setScores(UUID uuid, Collection<Integer, String> collection) {
        scores.add(uuid, collection);
        return scores.clone();
    }

    public static Collection<UUID, Collection<Integer, String>> removeScores(UUID uuid) {
        scores.remove(uuid);
        return scores.clone();
    }

    /**
     * @param name A name to register / remove score.
     * @param score Score for set score. null to remove the score.
     */
    public static void setScoreReplace(String name, Integer score) {
        Bukkit.getOnlinePlayers().forEach(player -> Utils.setScoreReplace(name, score, Utils.getObjective(player.getUniqueId()), player.getUniqueId()));
    }

    public static void teamSB(Team team, int score, Objective objective, UUID uuid) {
        String you = ChatColor.GRAY + " YOU";
        boolean inTeam = BedWars.team.get(uuid) == team;
        if (BedWars.aliveTeam.contains(team)) {
            Utils.setScoreReplace(team + ": " + ChatColor.GREEN + Utils.check + (inTeam ? you : ""), score, objective, uuid);
        } else {
            int players = BedWars.team.filter(t -> t.equals(team)).filterKeys(uuid2 -> BedWars.status.get(uuid2) == PlayerStatus.ALIVE).size();
            if (players <= 0) {
                Utils.setScoreReplace(team + ": " + ChatColor.RED + Utils.heavy_X + (inTeam ? you : ""), score, objective, uuid);
            } else {
                Utils.setScoreReplace(team + ": " + ChatColor.GREEN + players + (inTeam ? you : ""), score, objective, uuid);
            }
        }
    }

    public static Objective getObjective(UUID uuid) {
        Scoreboard scoreboard = BedWars.scoreboards.get(uuid);
        return scoreboard.getObjective(DisplaySlot.SIDEBAR);
    }

    public static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public static String getFriendlyName(ItemStack itemStack) {
        String name = itemStack.getType().toString().replaceAll("_", " ").toLowerCase();
        name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
        return name;
    }

    /**
     * Grants unbreakable tag and add efficiency enchant to the specified material and return ItemStack.
     */
    public static ItemStack enchantTool(Material material) {
        return getItemStack(material, Enchantment.DIG_SPEED, 1, false);
    }

    /**
     * Grants unbreakable tag and add specified enchant to the specified material and return ItemStack.
     */
    static ItemStack enchantTool(Material material, Collection<Enchantment, Integer> enchantments) {
        ItemStack item = unbreakable(material);
        ItemMeta meta = item.getItemMeta();
        enchantments.forEach((enchant, level) -> meta.addEnchant(enchant, level, true));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Grants unbreakable tag and add specified enchant to the specified material and return ItemStack.
     */
    public static ItemStack enchantTool(Material material, Enchantment enchant, Integer level) {
        return getItemStack(material, enchant, level, true);
    }

    /**
     * Grants unbreakable tag and add specified enchant to the specified material and return ItemStack.
     */
    public static ItemStack enchantTool(ItemStack item, Enchantment enchant, Integer level) {
        ItemMeta meta = item.getItemMeta();
        if (level != null) meta.addEnchant(enchant, level, true);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack getItemStack(Material material, Enchantment enchant, Integer level, boolean ignoreLevelRestriction) {
        ItemStack item = unbreakable(material);
        ItemMeta meta = item.getItemMeta();
        if (level != null && enchant != null) meta.addEnchant(enchant, level, ignoreLevelRestriction);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack unbreakable(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.spigot().setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        return item;
    }

    private static Color teamToColor(Team team) {
        switch (team) {
            case RED:
                return Color.RED;
            case BLUE:
                return Color.BLUE;
            case YELLOW:
                return Color.YELLOW;
            case GREEN:
                return Color.GREEN;
            case PINK:
                return Color.fromRGB(255,0,255);
            case AQUA:
                return Color.AQUA;
            case BLACK:
                return Color.BLACK;
            case WHITE:
                return Color.WHITE;
            default:
                return Color.PURPLE;
        }
    }

    public static ItemStack getColoredLeatherArmor(Material material, Team team) {
        return getColoredLeatherArmor(material, teamToColor(team));
    }

    private static ItemStack getColoredLeatherArmor(Material material, Color color) {
        ItemStack item = unbreakable(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Create hologram but won't add to the list.
     * @param location Hologram location
     * @return new hologram object
     */
    public static Hologram createHologram(Location location) {
        return HologramsAPI.createHologram(Utils.getInstance(), location);
    }

    /**
     * Create hologram and add to the list.
     * @param id ID that you want to create
     * @param location Hologram location
     * @return New hologram object
     */
    public static Hologram addHologram(String id, Location location) {
        Hologram hologram = Utils.createHologram(location);
        holograms.add(id, hologram);
        return hologram;
    }

    /**
     * Gets hologram by id.
     * @param id Hologram name specified by #addHologram
     * @return Hologram but null if not found
     */
    public static Hologram getHologram(String id) {
        return holograms.get(id);
    }

    /**
     * Gets hologram by id but if not found, it'll create one with new key.
     * @param id Hologram name specified by #addHologram
     * @param location Default location value.
     * @return Hologram, but new hologram if not found
     */
    public static Hologram getHologram(String id, Location location) {
        return holograms.getOrDefault(id, addHologram(id, location));
    }

    public static ItemStack getPotionItemStack(PotionType type, int level, int duration, String displayName) {
        Potion potion = new Potion(type, 1, false, true);
        try {
            Field field = potion.getClass().getField("level");
            field.setAccessible(true);
            field.setInt(potion, level);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {} // impossible
        ItemStack itemstack = potion.toItemStack(1);
        PotionMeta meta = (PotionMeta) itemstack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.addCustomEffect(new PotionEffect(type.getEffectType(), duration*20, level), true);
        itemstack.setItemMeta(meta);
        return itemstack;
    }

    @SuppressWarnings("deprecation")
    public static void endGame() {
        Utils.ended = true;
        StringBuilder players = new StringBuilder();
        BedWars.aliveTeam.forEach(team -> {
            BedWars.team.filter(t -> t == team).foreachKeys((uuid, index) -> {
                if (players.length() >= 1) players.append(ChatColor.GRAY + ", ");
                Player player = Bukkit.getPlayer(uuid);
                player.sendTitle("" + ChatColor.GOLD + ChatColor.BOLD + "VICTORY!", "");
                players.append(ChatColor.GRAY + player.getPlayerListName());
            });
        });
        CollectionList<Team> teams = BedWars.team.valuesList();
        CollectionList<Team> teams2 = teams.clone();
        teams2.removeAll(teams2);
        teams.forEach(t -> {
            if (!teams2.contains(t)) teams2.add(t);
        });
        if (teams2.size() <= 0) {
            Bukkit.broadcastMessage("" + ChatColor.GREEN + ChatColor.BOLD + "----------------------------------------");
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(Utils.textAtCenter(ChatColor.BOLD + "Bedwars", 40));
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(Utils.textAtCenter(ChatColor.GRAY + "Nobody won at this game!", 40));
            Bukkit.broadcastMessage("");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(Utils.textAtCenter(ChatColor.YELLOW + "Your kills: " + ChatColor.RED + BedWars.kills.getOrDefault(player.getUniqueId(), 0), 40));
                player.sendMessage(Utils.textAtCenter(ChatColor.YELLOW + "Your final kills: " + ChatColor.RED + BedWars.finalKills.getOrDefault(player.getUniqueId(), 0), 40));
            }
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("" + ChatColor.GREEN + ChatColor.BOLD + "----------------------------------------");
            new BukkitRunnable() {
                public void run() {
                    Bukkit.shutdown();
                }
            }.runTaskLater(Utils.getInstance(), 30*20);
            return;
        }
        Team team = teams2.first();
        Bukkit.broadcastMessage("" + ChatColor.GREEN + ChatColor.BOLD + "----------------------------------------");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(Utils.textAtCenter(ChatColor.BOLD + "Bedwars", 40));
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(Utils.textAtCenter(team.color + Utils.capitalize(team.name()) + ChatColor.GRAY + " - " + players.toString(), 40));
        Bukkit.broadcastMessage("");
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(Utils.textAtCenter(ChatColor.YELLOW + "Your kills: " + ChatColor.RED + BedWars.kills.getOrDefault(player.getUniqueId(), 0), 40));
            player.sendMessage(Utils.textAtCenter(ChatColor.YELLOW + "Your final kills: " + ChatColor.RED + BedWars.finalKills.getOrDefault(player.getUniqueId(), 0), 40));
        }
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("" + ChatColor.GREEN + ChatColor.BOLD + "----------------------------------------");
        new BukkitRunnable() {
            public void run() {
                Bukkit.shutdown();
            }
        }.runTaskLater(Utils.getInstance(), 30*20);
    }

    public static String repeatString(String s, int count) { // if you're at java 11, this method is useless.
        StringBuilder s1 = new StringBuilder();
        for (int i = 0; i < count; i++) {
            s1.append(s);
        }
        return s1.toString();
    }

    public static String textAtCenter(String text, int length) {
        return repeatString(" ", (int) ((length-text.length())/2L)) + text; // if java 11, we could use String#repeat(count).
    }

    public static void destroyBed(Block block) {
        if (block.getType() == Material.BED_BLOCK || block.getType() == Material.BED) {
            block.breakNaturally();
            Utils.run(a -> {
                java.util.Collection<Item> items = block.getWorld().getEntitiesByClass(Item.class);
                items.forEach(item -> {
                    if (item.getItemStack().getType() == Material.BED) item.remove();
                });
            });
        }
    }

    public static ItemStack getModifierItem() {
        ItemStack item = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Game Modifiers");
        meta.setLore(Collections.singletonList("Enable all game modifiers and let's cheat!"));
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        item.setItemMeta(meta);
        return item;
    }
}
