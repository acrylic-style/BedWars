package xyz.acrylicstyle.bedwars.upgrades;

import org.bukkit.inventory.ItemStack;

public interface TieredUpgrade<T> extends Upgrade<T> {
    int getTier(T t);
    int maxTier();
    void upgrade(T t);
    ItemStack getCost(int tier);
}
