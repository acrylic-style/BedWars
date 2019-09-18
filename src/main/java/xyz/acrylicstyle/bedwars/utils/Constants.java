package xyz.acrylicstyle.bedwars.utils;

import xyz.acrylicstyle.bedwars.gameevents.*;
import xyz.acrylicstyle.bedwars.generators.*;

import java.util.ArrayList;
import java.util.List;

public final class Constants {
    public static final List<Generator> generators = new ArrayList<>();
    public static final List<GameEvent> events = new ArrayList<>();

    static {
        generators.add(new IronGenerator());
        generators.add(new GoldGenerator());
        generators.add(new DiamondGenerator());
        generators.add(new EmeraldGenerator());

        events.add(new DiamondLevel2()); // 6 minutes
        events.add(new EmeraldLevel2()); // 10 minutes
        events.add(new DiamondLevel3()); // 15 minutes
        events.add(new EmeraldLevel3()); // 20 minutes
        events.add(new DiamondLevel4()); // 25 minutes
        events.add(new EmeraldLevel4()); // 30 minutes
        events.add(new DiamondLevel5()); // 35 minutes
        events.add(new EmeraldLevel5()); // 40 minutes
        events.add(new DiamondLevel6()); // 45 minutes
        events.add(new EmeraldLevel6()); // 48 minutes
        events.add(new BedDestruction()); // 50 minutes
        events.add(new SuddenDeath()); // 55 minutes
        events.add(new GameEnd()); // 60 minutes
        events.add(new KickAll()); // 61 minutes
    }
}
