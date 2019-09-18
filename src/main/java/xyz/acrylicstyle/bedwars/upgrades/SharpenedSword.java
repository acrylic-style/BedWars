package xyz.acrylicstyle.bedwars.upgrades;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.utils.Team;
import xyz.acrylicstyle.bedwars.utils.Utils;

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
                    // Bukkit.getPlayer(uuid).sendMessage(ChatColor.GREEN + "Someone has bought a upgrade(and triggered event), " + SharpenedSword.this.getName());
                });
            }
        }.runTaskTimer(Utils.getInstance(), 0, 40);
    }
}
