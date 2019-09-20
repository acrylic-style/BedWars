package xyz.acrylicstyle.bedwars.utils;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.tasks.GameTask;
import xyz.acrylicstyle.bedwars.tasks.LobbyTask;
import xyz.acrylicstyle.tomeito_core.providers.ConfigProvider;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Consumer;

public final class Utils {
    private Utils() {}

    private static GameTask _gameTask = null;
    private static LobbyTask _lobbyTask = null;
    private static ConfigUtils configUtils = null;

    public static int maximumPlayers = 16;
    public static int minimumPlayers = 4;
    public static int teamSize = 2; // doubles

    private final static char heavy_X = '\u2718';
    private final static char heavy_check = '\u2714';

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
            Utils.setScoreReplace(team + ": " + ChatColor.GREEN + Utils.heavy_check + (inTeam ? you : ""), score, objective, uuid);
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
        return s.substring(0, 1).toUpperCase() + s.substring(1);
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
        meta.addEnchant(enchant, level, true);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack getItemStack(Material material, Enchantment enchant, Integer level, boolean ignoreLevelRestriction) {
        ItemStack item = unbreakable(material);
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(enchant, level, ignoreLevelRestriction);
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
}
