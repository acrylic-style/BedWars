package xyz.acrylicstyle.bedwars.upgrades;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.utils.Team;
import xyz.acrylicstyle.bedwars.utils.Utils;

import java.util.Arrays;
import java.util.List;

public class SharpenedSword implements OneTimeUpgrade {
    @Override
    public String getName() {
        return "Sharpened Sword";
    }

    @Override
    public ItemStack getCost() {
        return new ItemStack(Material.DIAMOND, 8);
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Sharpened Sword");
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void run(Team team) {
        new BukkitRunnable() {
            public void run() {
                BedWars.team.values(team).foreachKeys((uuid, i) -> {
                    List<ItemStack> items = Arrays.asList(Bukkit.getPlayer(uuid).getInventory().getContents());
                    items.forEach(item -> {
                        if (item != null)
                            if (item.getType() == Material.IRON_SWORD
                                || item.getType() == Material.GOLD_SWORD
                                || item.getType() == Material.WOOD_SWORD
                                || item.getType() == Material.DIAMOND_SWORD
                                || item.getType() == Material.STONE_SWORD) items.add(items.indexOf(item), Utils.enchantTool(item.getType(), Enchantment.DAMAGE_ALL, 1));
                    });
                    Bukkit.getPlayer(uuid).getInventory().setContents(items.toArray(new ItemStack[0]));
                });
            }
        }.runTaskTimer(Utils.getInstance(), 0, 40);
    }
}
