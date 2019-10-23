package xyz.acrylicstyle.bedwars.generators;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.acrylicstyle.bedwars.upgrades.MoltenForge;
import xyz.acrylicstyle.bedwars.utils.Generator;
import xyz.acrylicstyle.bedwars.utils.GeneratorPlaces;
import xyz.acrylicstyle.bedwars.utils.Team;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class GoldGenerator implements Generator {
    private Team team;

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public GeneratorPlaces getGeneratorPlace() {
        return GeneratorPlaces.TEAM_BASE;
    }

    @Override
    public ItemStack getResource() {
        ItemStack item = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta = item.getItemMeta();
        meta.spigot().setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public double getGenerateTime() {
        return (6/(Utils.teamSize/4F)) / MoltenForge.getDivider(team);
    }

    @Override
    public GoldGenerator clone() {
        GoldGenerator generator = new GoldGenerator();
        generator.setTeam(this.team);
        return generator;
    }
}
