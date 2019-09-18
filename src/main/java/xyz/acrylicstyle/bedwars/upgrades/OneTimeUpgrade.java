package xyz.acrylicstyle.bedwars.upgrades;

import org.bukkit.inventory.ItemStack;

public interface OneTimeUpgrade extends Runnable, Upgrade {
    ItemStack getCost();
}
