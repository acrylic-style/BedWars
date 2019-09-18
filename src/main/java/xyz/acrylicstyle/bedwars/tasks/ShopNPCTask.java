package xyz.acrylicstyle.bedwars.tasks;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.utils.Team;
import xyz.acrylicstyle.bedwars.utils.Utils;

import java.util.Arrays;

public class ShopNPCTask extends BukkitRunnable {
    @Override
    public void run() {
        Arrays.asList(Team.values()).forEach(team -> {
            Villager itemShop = (Villager) BedWars.world.spawnEntity(Utils.getConfigUtils().getItemShopNPCLocation(team), EntityType.VILLAGER);
            itemShop.setCustomName("" + ChatColor.YELLOW + ChatColor.BOLD + "ITEM SHOP");
            itemShop.setProfession(Villager.Profession.BLACKSMITH);
            itemShop.setMaxHealth(Integer.MAX_VALUE);
            itemShop.setHealth(Integer.MAX_VALUE);
            itemShop.setCanPickupItems(false);
            Villager upgrade = (Villager) BedWars.world.spawnEntity(Utils.getConfigUtils().getUpgradeNPCLocation(team), EntityType.VILLAGER);
            upgrade.setCustomName("" + ChatColor.YELLOW + ChatColor.BOLD + "TEAM UPGRADES");
            upgrade.setProfession(Villager.Profession.PRIEST);
            upgrade.setMaxHealth(Integer.MAX_VALUE);
            upgrade.setHealth(Integer.MAX_VALUE);
            upgrade.setCanPickupItems(false);
        });
    }
}