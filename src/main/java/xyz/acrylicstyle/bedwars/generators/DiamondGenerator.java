package xyz.acrylicstyle.bedwars.generators;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.acrylicstyle.bedwars.utils.Generator;
import xyz.acrylicstyle.bedwars.utils.GeneratorPlaces;

public class DiamondGenerator implements Generator {
    private static int time = 30;

    @Override
    public GeneratorPlaces getGeneratorPlace() {
        return GeneratorPlaces.SEMI_MIDDLE;
    }

    @Override
    public ItemStack getResource() {
        ItemStack item = new ItemStack(Material.DIAMOND);
        ItemMeta meta = item.getItemMeta();
        meta.spigot().setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public double getGenerateTime() {
        return time;
    }

    public static void setTime(int time) {
        DiamondGenerator.time = time;
    }
}
