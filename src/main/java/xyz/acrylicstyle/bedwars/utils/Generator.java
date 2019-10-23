package xyz.acrylicstyle.bedwars.utils;

import org.bukkit.inventory.ItemStack;

public interface Generator {
    GeneratorPlaces getGeneratorPlace();
    ItemStack getResource();
    double getGenerateTime();
    Generator clone();
}
