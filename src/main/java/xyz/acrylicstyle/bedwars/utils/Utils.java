package xyz.acrylicstyle.bedwars.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.tasks.GameTask;
import xyz.acrylicstyle.bedwars.tasks.LobbyTask;
import xyz.acrylicstyle.tomeito_core.providers.ConfigProvider;

import java.io.IOException;
import java.util.function.Consumer;

import static xyz.acrylicstyle.bedwars.utils.Team.YELLOW;

public final class Utils {
    private Utils() {}

    private static GameTask _gameTask = null;
    private static LobbyTask _lobbyTask = null;

    public final static int maximumPlayers = 16;
    public static int minimumPlayers = 4;
    public static int teamSize = 2; // doubles

    public final static char heavy_X = '\u2718';
    public final static char heavy_check = '\u2714';

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
        Utils.minimumPlayers = BedWars.config.getInt("minimumPlayers", 4);
        Utils.teamSize = BedWars.map.getInt("teamSize", 2);
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

    /**
     * @param name A name to register / remove score.
     * @param score Score for set score. null to remove the score.
     * @param objective Objective for set score
     */
    public static void setScore(String name, Integer score, Objective objective) {
        if (score == null) {
            objective.getScoreboard().resetScores(name);
            return;
        }
        Score scoreObj = objective.getScore(name);
        scoreObj.setScore(score);
    }

    private static Collection<Integer, String> scores = new Collection<>();

    /**
     * @param name A name to register / remove score.
     * @param score Score for set score. null to remove the score.
     * @param objective Objective for set score
     */
    public static void setScoreReplace(String name, Integer score, Objective objective) {
        final String name2 = "    " + name + "        ";
        if (score == null) {
            objective.getScoreboard().resetScores(name2);
            return;
        }
        if (scores.get(score) != null) {
            if (scores.get(score).equalsIgnoreCase(name)) return; // return if name is same as last score entry
            objective.getScoreboard().resetScores(scores.get(score));
        }
        Score scoreObj = objective.getScore(name2);
        scoreObj.setScore(score);
        scores.put(score, name2);
    }

    /**
     * @param scoreNameMap Integer: Score number for set score, null to remove the score. | String: A name to register / remove score.
     * @param objective Objective for set score
     */
    public static void setScore(Collection<Integer, String> scoreNameMap, Objective objective) {
        scoreNameMap.forEach((score, name) -> setScore(name, score, objective));
    }

    /**
     * @param scoreNameMap Integer: Score number for set score, null to remove the score. | String: A name to register / remove score.
     * @param objective Objective for set score
     */
    public static void setScoreReplace(Collection<Integer, String> scoreNameMap, Objective objective) {
        scoreNameMap.forEach((score, name) -> setScoreReplace(name, score, objective));
    }

    public static void teamSB(Team team, int score, Objective objective) {
        if (BedWars.aliveTeam.contains(team)) {
            Utils.setScore(team + ": " + ChatColor.GREEN + Utils.heavy_check, score, objective);
        } else {
            int players = BedWars.team.filter(t -> t.equals(team)).filterKeys(uuid -> BedWars.status.get(uuid) == PlayerStatus.ALIVE).size();
            if (players <= 0) {
                Utils.setScore(team + ": " + ChatColor.RED + Utils.heavy_X, score, objective);
            } else {
                Utils.setScore(team + ": " + ChatColor.GREEN + players, score, objective);
            }
        }
    }
}
