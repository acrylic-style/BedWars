package xyz.acrylicstyle.bedwars.upgrades;

import org.bukkit.inventory.ItemStack;
import xyz.acrylicstyle.bedwars.utils.Team;

public interface Upgrade {
    String getName();
    ItemStack getItem();
    void run(Team team);
}
