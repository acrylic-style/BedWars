package xyz.acrylicstyle.bedwars.gamemod;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.acrylicstyle.bedwars.upgrades.MoltenForge;
import xyz.acrylicstyle.bedwars.upgrades.ReinforcedArmor;
import xyz.acrylicstyle.bedwars.upgrades.SharpenedSword;
import xyz.acrylicstyle.bedwars.utils.Team;
import xyz.acrylicstyle.bedwars.utils.Utils;

import java.util.Arrays;
import java.util.List;

public class MaxTeamUpgrades implements Modifier {
    @Override
    public String getName() {
        return "Max Team Upgrades";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("Maxes your team upgrades :)", ChatColor.RED + "Note: You can't disable this option after enabled this.");
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.DIAMOND);
    }

    @Override
    public void up() {
        for (Team team : Team.values()) {
            ReinforcedArmor.setTier(team, 4);
            MoltenForge.setTier(team, 5);
            SharpenedSword.runStatic(team);
        }
        Utils.maxTeamUpgrades = true;
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
        return null;
    }
}
