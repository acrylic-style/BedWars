package xyz.acrylicstyle.bedwars.upgrades;

import org.bukkit.inventory.ItemStack;

public interface OneTimeUpgrade<T> extends Upgrade<T> {
    ItemStack getCost();
}
