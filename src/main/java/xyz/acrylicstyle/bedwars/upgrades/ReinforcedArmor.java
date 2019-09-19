package xyz.acrylicstyle.bedwars.upgrades;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.utils.Team;
import xyz.acrylicstyle.bedwars.utils.Utils;

import java.util.Arrays;
import java.util.List;

public class ReinforcedArmor implements TieredUpgrade {
    private int tier = 0;

    public void upgrade() {
        this.tier++;
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    @Override
    public int maxTier() {
        return 4;
    }

    @Override
    public ItemStack getCost(int tier) {
        switch (tier) {
            case 1:
                return new ItemStack(Material.DIAMOND, 4);
            case 2:
                return new ItemStack(Material.DIAMOND, 8);
            case 3:
                return new ItemStack(Material.DIAMOND, 16);
            case 4:
                return new ItemStack(Material.DIAMOND, 32);
            default:
                return new ItemStack(Material.DIAMOND, 64*64*64);
        }
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public ItemStack getItem() {
        return null;
    }

    @Override
    public void run(Team team) {
        BedWars.team.values(team).foreachKeys(((uuid, i) -> {
            List<ItemStack> items = Arrays.asList(Bukkit.getPlayer(uuid).getInventory().getContents());
            items.forEach(item -> {
                if (item != null)
                    if (item.getType() == Material.IRON_LEGGINGS
                            || item.getType() == Material.IRON_BOOTS
                            || item.getType() == Material.LEATHER_BOOTS
                            || item.getType() == Material.LEATHER_LEGGINGS
                            || item.getType() == Material.CHAINMAIL_LEGGINGS
                            || item.getType() == Material.CHAINMAIL_BOOTS
                            || item.getType() == Material.DIAMOND_LEGGINGS
                            || item.getType() == Material.DIAMOND_BOOTS) Bukkit.getPlayer(uuid).getInventory().setItem(items.indexOf(item), Utils.enchantTool(item.getType(), Enchantment.PROTECTION_FALL, getTier()));
            });
        }));
    }
}
