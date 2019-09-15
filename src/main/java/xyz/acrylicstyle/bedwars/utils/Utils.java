package xyz.acrylicstyle.bedwars.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.tasks.GameTask;
import xyz.acrylicstyle.bedwars.tasks.LobbyTask;
import xyz.acrylicstyle.tomeito_core.providers.ConfigProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.Consumer;

public final class Utils {
    private Utils() {}

    private static GameTask _gameTask = null;
    private static LobbyTask _lobbyTask = null;

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
        BedWars.world.setGameRuleValue("doMobSpawning", "true");
        BedWars.world.setGameRuleValue("keepInventory", "true");
        BedWars.world.setFullTime(6000);
        BedWars.manager = Bukkit.getScoreboardManager();
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
}
