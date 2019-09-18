package xyz.acrylicstyle.bedwars.generators;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.acrylicstyle.bedwars.utils.Generator;
import xyz.acrylicstyle.bedwars.utils.GeneratorPlaces;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class GoldGenerator implements Generator {
    @Override
    public GeneratorPlaces getGeneratorPlace() {
        return GeneratorPlaces.TEAM_BASE;
    }

    @Override
    public ItemStack getResource() {
        ItemStack item = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta = item.getItemMeta();
        meta.spigot().setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public double getGenerateTime() {
        return 3/(Utils.teamSize/4F);
    }
}
