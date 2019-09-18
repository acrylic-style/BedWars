package xyz.acrylicstyle.bedwars.utils;

import org.bukkit.Bukkit;

public interface GameEvent extends Runnable {
    /**
     * @return Time that happens the event in seconds.
     */
    int getTime();

    /**
     * @return The name of this event, or what will happen etc.
     */
    String getName();
}

final class LockHolder {
    static final Object lock = new Object();
}
