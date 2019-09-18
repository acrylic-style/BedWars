package xyz.acrylicstyle.bedwars.inventories;

import com.avaje.ebean.validation.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.upgrades.OneTimeUpgrade;
import xyz.acrylicstyle.bedwars.upgrades.TieredUpgrade;
import xyz.acrylicstyle.bedwars.upgrades.Upgrade;
import xyz.acrylicstyle.bedwars.utils.*;

import java.util.ArrayList;
import java.util.Arrays;

public class TeamUpgrades implements InventoryHolder, Listener {
    private Collection<Material, Upgrade> upgrades = new Collection<>();
    private final Collection<Team, Inventory> inventories = new Collection<>();
    private Team team = null;

    public TeamUpgrades() {
        Arrays.asList(Team.values()).forEach(team -> {
            inventories.add(team, Bukkit.createInventory(this, 9*6, "Team Upgrades"));
        });
        initializeItems();
    }

    public TeamUpgrades prepare(Team team) {
        this.team = team;
        return this;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        if (this.team == null) throw new NullPointerException("getInventory must be called after prepare(Team).");
        Inventory inv = inventories.get(this.team);
        this.team = null;
        return inv;
    }

    private ItemStack setLore(Upgrade upgrade) {
        if (upgrade instanceof TieredUpgrade) return setLore((TieredUpgrade) upgrade);
        else if (upgrade instanceof OneTimeUpgrade) return setLore((OneTimeUpgrade) upgrade);
        else return null;
    }

    private ItemStack setLore(OneTimeUpgrade upgrade) {
        ItemStack item = upgrade.getItem();
        ItemStack cost = upgrade.getCost();
        ItemMeta meta = cost.getItemMeta();
        String[] a = { ChatColor.YELLOW + "Cost: " + ChatColor.AQUA + cost.getAmount() + " " + Utils.getFriendlyName(cost) };
        meta.setLore(Arrays.asList(a));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack setLore(TieredUpgrade upgrade) {
        ItemStack item = upgrade.getItem();
        ItemStack cost = upgrade.getCost(upgrade.getTier()+1);
        ItemMeta meta = item.getItemMeta();
        final String[] a;
        if (cost == null) {
            a = new String[]{ChatColor.GREEN + "UNLOCKED"};
        } else {
            a = new String[]{ChatColor.YELLOW + "Tier " + (upgrade.getTier() + 1) + " Cost: " + ChatColor.AQUA + cost.getAmount() + " " + Utils.getFriendlyName(cost)};
        }
        meta.setLore(Arrays.asList(a));
        item.setItemMeta(meta);
        return item;
    }

    private void initializeItems() {
        Constants.upgrades.forEach(upgrade -> {
            upgrades.add(upgrade.getItem().getType(), upgrade);
            inventories.foreach((inv,i) -> inv.setItem(i, setLore(upgrade)));
        });
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory().getHolder() != this) return;
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        ItemMeta meta = clickedItem.getItemMeta();
        meta.setLore(new ArrayList<>());
        clickedItem.setItemMeta(meta);
        ItemStack cost = Constants.shopItems_everything.get(clickedItem);
        if (cost == null) {
            p.sendMessage(ChatColor.RED + "You've tried to purchase undefined item, it'll be reported to our developers.");
            ItemStack required = Constants.shopItems_everything.get(new ItemStack(clickedItem.getType()));
            throw new NullPointerException("Undefined item data: " + clickedItem.getData() + ", " + clickedItem + "\nExpected(probably): " + required + ", Data: " + (required != null ? required.getData() : null));
        }
        if (!p.getInventory().containsAtLeast(cost, cost.getAmount())) {
            p.sendMessage(ChatColor.RED + "You don't have enough items!");
            return;
        }
        p.getInventory().removeItem(cost);
        Team team = BedWars.team.get(p.getUniqueId());
        String name = team.name().toUpperCase();
        clickedItem = setLore(upgrades.get(clickedItem.getType()));
        p.sendMessage(ChatColor.GREEN + "You purchased " + ChatColor.GOLD + Utils.getFriendlyName(clickedItem));
    }
}
