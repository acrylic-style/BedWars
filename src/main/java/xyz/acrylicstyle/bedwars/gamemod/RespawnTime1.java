package xyz.acrylicstyle.bedwars.gamemod;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.acrylicstyle.bedwars.utils.Utils;

import java.util.Collections;
import java.util.List;

public class RespawnTime1 implements Modifier {
    @Override
    public String getName() {
        return "1 second";
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("Set respawn time to 1 second.");
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.PAPER);
    }

    @Override
    public void up() {
        Utils.respawnTime = 1;
    }

    @Override
    public void down() {} // wont be called

    @Override
    public boolean defaultStatus() { return false; }

    @Override
    public boolean isGroup() { return false; }

    @Override
    public Modifier groupOf() { return new RespawnTime(); }
}
