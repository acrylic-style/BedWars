package xyz.acrylicstyle.bedwars.inventories;

import com.avaje.ebean.validation.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.upgrades.OneTimeUpgrade;
import xyz.acrylicstyle.bedwars.upgrades.TieredUpgrade;
import xyz.acrylicstyle.bedwars.upgrades.Upgrade;
import xyz.acrylicstyle.bedwars.utils.Collection;
import xyz.acrylicstyle.bedwars.utils.Constants;
import xyz.acrylicstyle.bedwars.utils.Team;
import xyz.acrylicstyle.bedwars.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamUpgrades implements InventoryHolder, Listener {
    private Collection<Material, Upgrade> upgrades = new Collection<>();
    private List<Upgrade> unlockedUpgrades = new ArrayList<>();
    private final Collection<Team, Inventory> inventories = new Collection<>();
    private final Collection<Integer, ItemStack> noLoreItems = new Collection<>();
    private Team team = null;

    public TeamUpgrades() {
        Arrays.asList(Team.values()).forEach(team -> inventories.add(team, Bukkit.createInventory(this, 9*6, "Team Upgrades")));
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

    private ItemStack setLore(Upgrade upgrade, int slot) {
        if (upgrade instanceof TieredUpgrade) return setLore((TieredUpgrade) upgrade, slot);
        else if (upgrade instanceof OneTimeUpgrade) return setLore((OneTimeUpgrade) upgrade, slot);
        else return null;
    }

    private ItemStack setLore(OneTimeUpgrade upgrade, int slot) {
        ItemStack item = upgrade.getItem();
        noLoreItems.put(slot, item.clone());
        item = item.clone();
        ItemStack cost = upgrade.getCost();
        ItemMeta meta = item.getItemMeta();
        String[] a = { ChatColor.YELLOW + "Cost: " + ChatColor.AQUA + cost.getAmount() + " " + Utils.getFriendlyName(cost) };
        meta.setLore(Arrays.asList(a));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack setLore(TieredUpgrade upgrade, int slot) {
        ItemStack item = upgrade.getItem();
        noLoreItems.put(slot, item.clone());
        item = item.clone();
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
        Constants.upgrades.foreach((upgrade, i) -> {
            upgrades.add(upgrade.getItem().getType(), upgrade);
            inventories.foreach((inv,i2) -> inv.setItem(i, setLore(upgrade, i)));
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
        if (unlockedUpgrades.contains(upgrades.get(clickedItem.getType()))) {
            p.sendMessage(ChatColor.RED + "You've already unlocked this upgrade!");
            return;
        }
        ItemStack item = noLoreItems.get(e.getSlot());
        ItemStack cost = getCost(upgrades.get(item.getType()));
        if (cost == null) {
            p.sendMessage(ChatColor.RED + "This upgrade is already unlocked!");
            return;
        }
        if (!p.getInventory().containsAtLeast(cost, cost.getAmount())) {
            p.sendMessage(ChatColor.RED + "You don't have enough items!");
            return;
        }
        p.getInventory().removeItem(cost);
        Team team = BedWars.team.get(p.getUniqueId());
        if (upgrades.get(item.getType()) instanceof OneTimeUpgrade) {
            upgrades.get(item.getType()).run(team);
            unlockedUpgrades.add(upgrades.get(item.getType()));
        } else if (upgrades.get(item.getType()) instanceof TieredUpgrade) if (((TieredUpgrade) upgrades.get(item.getType())).getTier() == ((TieredUpgrade)upgrades.get(item.getType())).maxTier()) {
            ((TieredUpgrade) upgrades.get(item.getType())).upgrade();
            upgrades.get(item.getType()).run(team);
            unlockedUpgrades.add(upgrades.get(item.getType()));
        }
        p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
        p.sendMessage(ChatColor.GREEN + p.getName() + " purchased " + ChatColor.GOLD + upgrades.get(item.getType()).getName());
        initializeItems(); // re-add lore for items
    }

    private ItemStack getCost(Upgrade upgrade) {
        if (upgrade instanceof OneTimeUpgrade) return ((OneTimeUpgrade)upgrade).getCost();
        if (upgrade instanceof TieredUpgrade) return ((TieredUpgrade)upgrade).getCost(((TieredUpgrade)upgrade).getTier()+1);
        return null;
    }
}
