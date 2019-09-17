package xyz.acrylicstyle.bedwars.generators;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.acrylicstyle.bedwars.utils.Generator;
import xyz.acrylicstyle.bedwars.utils.GeneratorPlaces;

public class IronGenerator implements Generator {
    @Override
    public GeneratorPlaces getGeneratorPlace() {
        return GeneratorPlaces.TEAM_BASE;
    }

    @Override
    public ItemStack getResource() {
        ItemStack item = new ItemStack(Material.IRON_INGOT);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.spigot().setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public int getGenerateTime() {
        return 1;
    }
}
