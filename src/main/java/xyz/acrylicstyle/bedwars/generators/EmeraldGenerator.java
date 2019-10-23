package xyz.acrylicstyle.bedwars.generators;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.acrylicstyle.bedwars.utils.Generator;
import xyz.acrylicstyle.bedwars.utils.GeneratorPlaces;

public class EmeraldGenerator implements Generator {
    public static int time = 70;

    @Override
    public GeneratorPlaces getGeneratorPlace() {
        return GeneratorPlaces.MIDDLE;
    }

    @Override
    public ItemStack getResource() {
        ItemStack item = new ItemStack(Material.EMERALD);
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
        EmeraldGenerator.time = time;
    }

    @Override
    public EmeraldGenerator clone() {
        EmeraldGenerator generator = new EmeraldGenerator();
        generator.setTime(EmeraldGenerator.time);
        return generator;
    }
}
