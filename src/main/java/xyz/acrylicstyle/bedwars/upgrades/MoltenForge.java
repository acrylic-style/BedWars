package xyz.acrylicstyle.bedwars.upgrades;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import util.Collection;
import util.CollectionStrictSync;
import xyz.acrylicstyle.bedwars.generators.GoldGenerator;
import xyz.acrylicstyle.bedwars.generators.IronGenerator;
import xyz.acrylicstyle.bedwars.tasks.ResourceGeneratorTask;
import xyz.acrylicstyle.bedwars.utils.*;

public class MoltenForge implements TieredUpgrade<Team> {
    private static CollectionStrictSync<Team, Integer> divider = new CollectionStrictSync<>();

    public static Integer getDivider(Team team) { return divider.getOrDefault(team, 1); }

    @SuppressWarnings("unused")
    public static CollectionStrictSync<Team, Integer> getDivider() { return divider; }

    @Override
    public void upgrade(Team team) { divider.put(team, getTier(team)+1); }

    @Override
    public int getTier(Team team) { return divider.getOrDefault(team, 0); }

    @Override
    public int maxTier() { return 4; }

    @Override
    public ItemStack getCost(int tier) {
        switch (tier) {
            case 1:
                return new ItemStack(Material.DIAMOND, 4);
            case 2:
                return new ItemStack(Material.DIAMOND, 8);
            case 3:
                return new ItemStack(Material.DIAMOND, 12);
            case 4:
                return new ItemStack(Material.DIAMOND, 16);
            default:
                return new ItemStack(Material.DIAMOND, 64*64*64);
        }
    }

    @Override
    public String getName() { return "Molten Forge"; }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.FURNACE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + getName());
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void run(Team team) {}
}
