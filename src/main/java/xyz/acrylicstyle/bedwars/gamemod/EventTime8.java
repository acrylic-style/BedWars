package xyz.acrylicstyle.bedwars.gamemod;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.acrylicstyle.bedwars.utils.Utils;

import java.util.Collections;
import java.util.List;

public class EventTime8 implements Modifier {
    @Override
    public String getName() {
        return "8x Faster Event Time";
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("Help me, it's too fast for me!");
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.PAPER);
    }

    @Override
    public void up() {
        Utils.eventTime = 8;
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
