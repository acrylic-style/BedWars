package xyz.acrylicstyle.bedwars.utils;

import xyz.acrylicstyle.bedwars.gameevents.DiamondLevel2;
import xyz.acrylicstyle.bedwars.gameevents.DiamondLevel3;
import xyz.acrylicstyle.bedwars.gameevents.EmeraldLevel2;
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
    }
}
