package xyz.acrylicstyle.bedwars.upgrades;

import org.bukkit.inventory.ItemStack;
import xyz.acrylicstyle.bedwars.utils.Utils;

public interface TieredUpgrade extends Upgrade {
    int getTier();
    default void upgrade() {
        Utils.run(a -> this.run());
    }
    ItemStack getCost(int tier);
}
