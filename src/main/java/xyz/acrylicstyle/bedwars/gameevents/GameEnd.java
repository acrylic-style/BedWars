package xyz.acrylicstyle.bedwars.gameevents;

import xyz.acrylicstyle.bedwars.utils.GameEvent;

public class GameEnd implements GameEvent {
    public void run() {
        // not defined
    }

    public int getTime() {
        return 60*60; // 60 minutes
    }

    @Override
    public String getName() {
        return "Game End";
    }
}
