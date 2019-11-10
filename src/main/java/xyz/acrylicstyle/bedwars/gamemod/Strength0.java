package xyz.acrylicstyle.bedwars.gamemod;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.acrylicstyle.bedwars.utils.Utils;

import java.util.Collections;
import java.util.List;

public class Strength0 implements Modifier {
    @Override
    public String getName() {
        return "+0 Strength";
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("Applies +0 strength Normal mode.");
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.PAPER);
    }

    @Override
    public void up() {
        Utils.strength = 0;
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        });
    }

    @Override
    public void down() {
        Utils.strength = 0;
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        });
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
        return new Strength();
    }
}
