package xyz.acrylicstyle.bedwars.gamemod;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.acrylicstyle.bedwars.utils.Utils;

import java.util.Collections;
import java.util.List;

public class OnePunchOneKill implements Modifier {
    @Override
    public String getName() {
        return "One Punch, One Kill";
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("You can 1-hit player and kill someone, but also you can be killed with 1-hit.");
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.DIAMOND_BLOCK);
    }

    @Override
    public void up() {
        Utils.strength = 127;
    }

    @Override
    public void down() {}

    @Override
    public boolean defaultStatus() {
        return false;
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public Modifier groupOf() {
        return new Strength();
    }
}
