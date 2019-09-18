package xyz.acrylicstyle.bedwars.utils;

import xyz.acrylicstyle.bedwars.generators.*;

import java.util.ArrayList;
import java.util.List;

public final class Constants {
    public static final List<Generator> generators = new ArrayList<>();

    static {
        generators.add(new IronGenerator());
        generators.add(new GoldGenerator());
        generators.add(new DiamondGenerator());
        generators.add(new EmeraldGenerator());
    }
}
