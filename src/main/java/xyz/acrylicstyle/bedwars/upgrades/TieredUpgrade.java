package xyz.acrylicstyle.bedwars.upgrades;

import org.bukkit.inventory.ItemStack;

public interface TieredUpgrade extends Upgrade {
    int getTier();
    int maxTier();
    void upgrade();
    ItemStack getCost(int tier);
}
