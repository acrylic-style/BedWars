package xyz.acrylicstyle.bedwars.upgrades;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import util.CollectionStrictSync;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.utils.Team;
import xyz.acrylicstyle.bedwars.utils.Utils;

import java.util.Arrays;
import java.util.List;

public class ReinforcedArmor implements TieredUpgrade<Team> {
    private static CollectionStrictSync<Team, Integer> tier = new CollectionStrictSync<>();

    public static int getTierStatic(Team team) { return tier.getOrDefault(team, 0); }

    @Override
    public void upgrade(Team team) {
        tier.put(team, getTier(team)+1);
    }

    @Override
    public int getTier(Team team) {
        if (team == null) throw new NullPointerException("team is null - please nerf.");
        return tier.getOrDefault(team, 0);
    }

    @Override
    public int maxTier() { return 4; }

    @Override
    public ItemStack getCost(int tier) {
        switch (tier) {
            case 1:
                return new ItemStack(Material.DIAMOND, 4);
            case 2:
                return new ItemStack(Material.DIAMOND, 8);
            case 3:
                return new ItemStack(Material.DIAMOND, 16);
            case 4:
                return new ItemStack(Material.DIAMOND, 32);
            default:
                return new ItemStack(Material.DIAMOND, 64*64*64);
        }
    }

    @Override
    public String getName() { return "Reinforced Armor"; }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.IRON_CHESTPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + getName());
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void run(Team team) {
        new BukkitRunnable() {
            public void run() {
                BedWars.team.values(team).foreachKeys(((uuid, i) -> {
                    List<ItemStack> items = Arrays.asList(Bukkit.getPlayer(uuid).getInventory().getArmorContents());
                    items.forEach(item -> item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, getTier(team)));
                }));
            }
        }.runTaskTimer(Utils.getInstance(), 0, 40);
    }
}
