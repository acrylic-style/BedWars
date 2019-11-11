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

import java.util.UUID;

public class SharpenedSword implements OneTimeUpgrade<Team> {
    private static UUID uuid = null;

    @Override
    public String getName() { return "Sharpened Sword"; }

    @Override
    public ItemStack getCost() { return new ItemStack(Material.DIAMOND, 8); }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + getName());
        item.setItemMeta(meta);
        return item;
    }

    public static void runStatic(Team team) {
        new BukkitRunnable() {
            public void run() {
                BedWars.team.values(team).foreachKeys((uuid, i) -> {
                    if (uuid != null) {
                        SharpenedSword.uuid = uuid;
                        Bukkit.getPlayer(uuid).getInventory().all(Material.WOOD_SWORD).forEach(SharpenedSword::func);
                        Bukkit.getPlayer(uuid).getInventory().all(Material.STONE_SWORD).forEach(SharpenedSword::func);
                        Bukkit.getPlayer(uuid).getInventory().all(Material.IRON_SWORD).forEach(SharpenedSword::func);
                        Bukkit.getPlayer(uuid).getInventory().all(Material.GOLD_SWORD).forEach(SharpenedSword::func);
                        Bukkit.getPlayer(uuid).getInventory().all(Material.DIAMOND_SWORD).forEach(SharpenedSword::func);
                    }
                });
            }
        }.runTaskTimer(Utils.getInstance(), 0, 40);
    }

    @Override
    public void run(Team team) {
        runStatic(team);
    }

    private static void func(Integer slot, ItemStack item) {
        if (!item.getItemMeta().hasEnchant(Enchantment.DAMAGE_ALL)) Bukkit.getPlayer(uuid).getInventory().setItem(slot, Utils.enchantTool(item.getType(), Enchantment.DAMAGE_ALL, 1));
    }
}
