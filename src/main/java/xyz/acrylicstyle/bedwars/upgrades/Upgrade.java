package xyz.acrylicstyle.bedwars.upgrades;

import org.bukkit.inventory.ItemStack;

public interface Upgrade<T> {
    String getName();
    ItemStack getItem();
    void run(T t);
}
