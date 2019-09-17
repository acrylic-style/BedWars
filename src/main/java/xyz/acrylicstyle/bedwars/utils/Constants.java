package xyz.acrylicstyle.bedwars.utils;

import xyz.acrylicstyle.bedwars.generators.IronGenerator;

import java.util.ArrayList;
import java.util.List;

public final class Constants {
    public static final List<Generator> generators = new ArrayList<>();

    static {
        generators.add(new IronGenerator());
    }
}
