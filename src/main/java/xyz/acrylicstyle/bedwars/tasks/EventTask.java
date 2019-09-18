package xyz.acrylicstyle.bedwars.tasks;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.acrylicstyle.bedwars.utils.CollectionList;
import xyz.acrylicstyle.bedwars.utils.Constants;
import xyz.acrylicstyle.bedwars.utils.GameEvent;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class EventTask extends BukkitRunnable {
    private CollectionList<GameEvent> events = new CollectionList<>();

    @Override
    public void run() {
        GameEvent nextEvent = events.first();
        if (GameTask.playedTime > nextEvent.getTime()) {
            events.remove(nextEvent);
            nextEvent = events.first();
        }
        Utils.setScoreReplace(nextEvent.getName() + " in " + ChatColor.GREEN + Utils.secondsToTime(nextEvent.getTime()-GameTask.playedTime), 10);
    }

    void scheduleEvents() {
        Constants.events.forEach(GameEvent::schedule);
        events.addAll(Constants.events);
    }
}
