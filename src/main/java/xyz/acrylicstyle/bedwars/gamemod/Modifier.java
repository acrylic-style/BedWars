package xyz.acrylicstyle.bedwars.gamemod;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Modifier {
    String getName();
    List<String> getDescription();
    ItemStack getItem();
    void up();
    void down();
    boolean defaultStatus();
    boolean isGroup();
    Modifier groupOf();
}
