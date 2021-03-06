package xyz.acrylicstyle.bedwars.upgrades;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.acrylicstyle.bedwars.utils.Team;
import xyz.acrylicstyle.bedwars.utils.Trap;
import xyz.acrylicstyle.bedwars.utils.Traps;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class BlindTrap implements TrapUpgrade, OneTimeUpgrade<Team> {
    @Override
    public ItemStack getCost() {
        return new ItemStack(Material.DIAMOND, 2);
    }

    @Override
    public String getName() {
        return "Blind Trap (10 seconds blind)";
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.EYE_OF_ENDER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + getName());
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void run(Team team) {
        Utils.traps.get(team).add(new Trap(Traps.BLIND, team));
    }
}
