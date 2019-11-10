package xyz.acrylicstyle.bedwars.gamemod;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.acrylicstyle.bedwars.utils.Utils;

import java.util.Collections;
import java.util.List;

public class BlockProtection implements Modifier {
    @Override
    public String getName() {
        return "Block Protection";
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("You can enable/disable block protection.");
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.GRASS);
    }

    @Override
    public void up() {
        Utils.blockProtection = true;
    }

    @Override
    public void down() {
        Utils.blockProtection = false;
    }

    @Override
    public boolean defaultStatus() {
        return true;
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public Modifier groupOf() {
        return null;
    }
}
