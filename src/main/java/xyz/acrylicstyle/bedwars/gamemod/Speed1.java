package xyz.acrylicstyle.bedwars.gamemod;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.acrylicstyle.bedwars.utils.Utils;

import java.util.Collections;
import java.util.List;

public class Speed1 implements Modifier {
    @Override
    public String getName() {
        return "Speed I";
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("Applies Speed I.");
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.PAPER);
    }

    @Override
    public void up() {
        Utils.speed = 1;
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 0, false));
        });
    }

    @Override
    public void down() {
        Utils.speed = 0;
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.removePotionEffect(PotionEffectType.SPEED);
        });
    }

    @Override
    public boolean defaultStatus() {
        return false;
    }

    @Override
    public boolean isGroup() { return false; }

    @Override
    public Modifier groupOf() { return new Speed(); }
}
