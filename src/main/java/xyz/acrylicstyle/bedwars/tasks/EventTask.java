package xyz.acrylicstyle.bedwars.tasks;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import util.CollectionList;
import xyz.acrylicstyle.bedwars.generators.DiamondGenerator;
import xyz.acrylicstyle.bedwars.generators.EmeraldGenerator;
import xyz.acrylicstyle.bedwars.utils.Constants;
import xyz.acrylicstyle.bedwars.utils.GameEvent;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class EventTask extends BukkitRunnable {
    private CollectionList<GameEvent> events = new CollectionList<>();
    private int dseconds = 0;
    private int eseconds = 0;
    private int lastdseconds = 0;
    private int lasteseconds = 0;

    @Override
    public void run() {
        GameEvent nextEvent = events.first();
        if (GameTask.playedTime > nextEvent.getTime()) {
            nextEvent.run();
            events.remove(nextEvent);
            nextEvent = events.first();
        }
        if (DiamondGenerator.time != lastdseconds) {
            lastdseconds = DiamondGenerator.time;
            dseconds = DiamondGenerator.time;
        }
        if (EmeraldGenerator.time != lasteseconds) {
            lasteseconds = EmeraldGenerator.time;
            eseconds = EmeraldGenerator.time;
        }
        Utils.setScoreReplace(nextEvent.getName() + " in " + ChatColor.GREEN + Utils.secondsToTime(nextEvent.getTime()-GameTask.playedTime), 10);
        Utils.getConfigUtils().getSemiMiddleGenerators().forEach(location -> {
            Hologram hologram = Utils.getHologram(location.toString());
            hologram.insertTextLine(2, ChatColor.YELLOW + "Spawns in " + ChatColor.RED + dseconds + ChatColor.YELLOW + " seconds");
        });
        Utils.getConfigUtils().getMiddleGenerators().forEach(location -> {
            Hologram hologram = Utils.getHologram(location.toString());
            hologram.insertTextLine(2, ChatColor.YELLOW + "Spawns in " + ChatColor.RED + eseconds + ChatColor.YELLOW + " seconds");
        });
        dseconds--;
        eseconds--;
    }

    void scheduleEvents() {
        events.addAll(Constants.events);
    }
}
