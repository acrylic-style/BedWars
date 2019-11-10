package xyz.acrylicstyle.bedwars.gamemod;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.acrylicstyle.bedwars.utils.Utils;

import java.util.Collections;
import java.util.List;

public class Crafting implements Modifier {
    @Override
    public String getName() { return "Crafting"; }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("If this has enabled, players will be able to craft anything.");
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.WORKBENCH);
    }

    @Override
    public void up() { Utils.crafting = true; }

    @Override
    public void down() { Utils.crafting = false; }

    @Override
    public boolean defaultStatus() { return false; }

    @Override
    public boolean isGroup() { return false; }

    @Override
    public Modifier groupOf() { return null; }
}
