package xyz.acrylicstyle.bedwars.gamemod;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class EventTime implements Modifier {
    @Override
    public String getName() {
        return "Event Time";
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("Modify time for how fast events will happen.");
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.WATCH);
    }

    @Override
    public void up() {}

    @Override
    public void down() {}

    @Override
    public boolean defaultStatus() { return false; }

    @Override
    public boolean isGroup() { return true; }

    @Override
    public Modifier groupOf() {
        return null;
    }
}
