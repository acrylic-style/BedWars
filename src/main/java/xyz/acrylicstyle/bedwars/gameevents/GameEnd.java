package xyz.acrylicstyle.bedwars.gameevents;

import xyz.acrylicstyle.bedwars.utils.GameEvent;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class GameEnd implements GameEvent {
    public void run() {
        Utils.endGame();
    }

    public int getTime() {
        return (int) (60*60/ Utils.eventTime); // 60 minutes
    }

    @Override
    public String getName() {
        return "Game End";
    }
}
