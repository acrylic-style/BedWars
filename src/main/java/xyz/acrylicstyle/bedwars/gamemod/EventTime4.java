package xyz.acrylicstyle.bedwars.gamemod;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.acrylicstyle.bedwars.utils.Utils;

import java.util.Collections;
import java.util.List;

public class EventTime4 implements Modifier {
    @Override
    public String getName() {
        return "4x Faster Event Time";
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("Woah, it's fast!");
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.PAPER);
    }

    @Override
    public void up() {
        Utils.eventTime = 4;
    }

    @Override
    public void down() {}

    @Override
    public boolean defaultStatus() { return true; }

    @Override
    public boolean isGroup() { return false; }

    @Override
    public Modifier groupOf() {
        return new EventTime();
    }
}
