package xyz.acrylicstyle.bedwars.upgrades;

import org.bukkit.inventory.ItemStack;

public interface OneTimeUpgrade extends Upgrade {
    ItemStack getCost();
}
