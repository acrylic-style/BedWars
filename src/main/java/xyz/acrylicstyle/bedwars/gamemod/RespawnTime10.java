package xyz.acrylicstyle.bedwars.gamemod;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.acrylicstyle.bedwars.utils.Utils;

import java.util.Collections;
import java.util.List;

public class RespawnTime10 implements Modifier {
    @Override
    public String getName() {
        return "10 seconds";
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("Set respawn time to 10 seconds.");
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.PAPER);
    }

    @Override
    public void up() {
        Utils.respawnTime = 10;
    }

    @Override
    public void down() {}

    @Override
    public boolean defaultStatus() { return false; }

    @Override
    public boolean isGroup() { return false; }

    @Override
    public Modifier groupOf() { return new RespawnTime(); }
}
