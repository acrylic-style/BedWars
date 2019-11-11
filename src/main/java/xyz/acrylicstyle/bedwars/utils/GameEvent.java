package xyz.acrylicstyle.bedwars.utils;

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