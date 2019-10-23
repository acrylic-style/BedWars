package xyz.acrylicstyle.bedwars.upgrades;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import util.CollectionStrictSync;
import xyz.acrylicstyle.bedwars.utils.Team;

public class MoltenForge implements TieredUpgrade<Team> {
    private static CollectionStrictSync<Team, Integer> divider = new CollectionStrictSync<>();

    public static int getDivider(Team team) {
        Validate.notNull(team, "Team cannot be null");
        return divider.getOrDefault(team, 1);
    }

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
            case 5:
                return new ItemStack(Material.DIAMOND, 24);
            case 6:
                return new ItemStack(Material.DIAMOND, 32);
            case 7:
                return new ItemStack(Material.DIAMOND, 48);
            case 8:
                return new ItemStack(Material.DIAMOND, 64);
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
