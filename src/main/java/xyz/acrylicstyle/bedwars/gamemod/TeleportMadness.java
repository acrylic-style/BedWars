package xyz.acrylicstyle.bedwars.gamemod;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.acrylicstyle.bedwars.utils.Utils;

import java.util.Collections;
import java.util.List;

public class TeleportMadness implements Modifier {
    @Override
    public String getName() {
        return "Teleport Madness";
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("Gives you 64 ender pearls when you respawn and game starts.");
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.ENDER_PEARL);
    }

    @Override
    public void up() {
        Utils.teleportMadness = true;
    }

    @Override
    public void down() {
        Utils.teleportMadness = false;
    }

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
        return null;
    }
}
