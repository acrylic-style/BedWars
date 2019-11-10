package xyz.acrylicstyle.bedwars.gamemod;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

public class Strength2 implements Modifier {
    @Override
    public String getName() {
        return "+2 Strength";
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("Grants +2 strength.");
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.PAPER);
    }

    @Override
    public void up() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100000, 1, false));
        });
    }

    @Override
    public void down() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        });
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
        return new Strength();
    }
}
