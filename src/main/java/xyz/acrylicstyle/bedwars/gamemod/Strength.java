package xyz.acrylicstyle.bedwars.gamemod;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class Strength implements Modifier {
    @Override
    public String getName() {
        return "Strength";
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("Add strength to sword!");
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.DIAMOND_SWORD);
    }

    @Override
    public void up() {}

    @Override
    public void down() {}

    @Override
    public boolean defaultStatus() { return false; }

    @Override
    public boolean isGroup() {
        return true;
    }

    @Override
    public Modifier groupOf() {
        return null;
    }
}
