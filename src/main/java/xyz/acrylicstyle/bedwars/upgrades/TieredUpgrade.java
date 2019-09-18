package xyz.acrylicstyle.bedwars.upgrades;

import org.bukkit.inventory.ItemStack;
import xyz.acrylicstyle.bedwars.utils.Team;
import xyz.acrylicstyle.bedwars.utils.Utils;

public interface TieredUpgrade extends Upgrade {
    int getTier();
    int maxTier();
    default void upgrade(Team team) {
        Utils.run(a -> this.run(team));
    }
    ItemStack getCost(int tier);
}
