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
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.utils.*;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemShop implements InventoryHolder, Listener {
    private final Collection<ShopCategory, Inventory> inventories = new Collection<>();

    public ItemShop() {
        inventories.add(ShopCategory.QUICK_BUY, Bukkit.createInventory(this, 9*6, "Item Shop"));
        inventories.add(ShopCategory.BLOCKS, Bukkit.createInventory(this, 9*6, "Item Shop"));
        inventories.add(ShopCategory.MELEE, Bukkit.createInventory(this, 9*6, "Item Shop"));
        inventories.add(ShopCategory.ARMOR, Bukkit.createInventory(this, 9*6, "Item Shop"));
        inventories.add(ShopCategory.TOOLS, Bukkit.createInventory(this, 9*6, "Item Shop"));
        inventories.add(ShopCategory.RANGED, Bukkit.createInventory(this, 9*6, "Item Shop"));
        inventories.add(ShopCategory.POTIONS, Bukkit.createInventory(this, 9*6, "Item Shop"));
        inventories.add(ShopCategory.UTILITY, Bukkit.createInventory(this, 9*6, "Item Shop"));
        initializeItems();
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventories.get(ShopCategory.QUICK_BUY);
    }

    private void initializeCategoryItems() {
        inventories.foreach((inv,i) -> inv.setItem(0, categoryItem(Material.NETHER_STAR, ChatColor.GREEN + "Quick Buy")));
        inventories.foreach((inv,i) -> inv.setItem(1, categoryItem(Material.STAINED_CLAY, ChatColor.GREEN + "Blocks")));
        inventories.foreach((inv,i) -> inv.setItem(2, categoryItem(Material.GOLD_SWORD, ChatColor.GREEN + "Melee")));
        inventories.foreach((inv,i) -> inv.setItem(3, categoryItem(Material.CHAINMAIL_BOOTS, ChatColor.GREEN + "Armor")));
        inventories.foreach((inv,i) -> inv.setItem(4, categoryItem(Material.STONE_PICKAXE, ChatColor.GREEN + "Tools")));
        inventories.foreach((inv,i) -> inv.setItem(5, categoryItem(Material.BOW, ChatColor.GREEN + "Ranged")));
        inventories.foreach((inv,i) -> inv.setItem(6, categoryItem(Material.BREWING_STAND_ITEM, ChatColor.GREEN + "Potions")));
        inventories.foreach((inv,i) -> inv.setItem(7, categoryItem(Material.TNT, ChatColor.GREEN + "Utility")));
        inventories.forEach((c,inv) -> inv.setItem(9, categoryItem(Material.STAINED_GLASS_PANE, " ", "Quick Buy".equalsIgnoreCase(c.name) ? (byte) 5 : (byte) 0)));
        inventories.forEach((c,inv) -> inv.setItem(10, categoryItem(Material.STAINED_GLASS_PANE, " ", "Blocks".equalsIgnoreCase(c.name) ? (byte) 5 : (byte) 0)));
        inventories.forEach((c,inv) -> inv.setItem(11, categoryItem(Material.STAINED_GLASS_PANE, " ", "Melee".equalsIgnoreCase(c.name) ? (byte) 5 : (byte) 0)));
        inventories.forEach((c,inv) -> inv.setItem(12, categoryItem(Material.STAINED_GLASS_PANE, " ", "Armor".equalsIgnoreCase(c.name) ? (byte) 5 : (byte) 0)));
        inventories.forEach((c,inv) -> inv.setItem(13, categoryItem(Material.STAINED_GLASS_PANE, " ", "Tools".equalsIgnoreCase(c.name) ? (byte) 5 : (byte) 0)));
        inventories.forEach((c,inv) -> inv.setItem(14, categoryItem(Material.STAINED_GLASS_PANE, " ", "Ranged".equalsIgnoreCase(c.name) ? (byte) 5 : (byte) 0)));
        inventories.forEach((c,inv) -> inv.setItem(15, categoryItem(Material.STAINED_GLASS_PANE, " ", "Potions".equalsIgnoreCase(c.name) ? (byte) 5 : (byte) 0)));
        inventories.forEach((c,inv) -> inv.setItem(16, categoryItem(Material.STAINED_GLASS_PANE, " ", "Utility".equalsIgnoreCase(c.name) ? (byte) 5 : (byte) 0)));
    }

    private void initializeQuickBuyItems() {
        Inventory quickBuy = inventories.get(ShopCategory.QUICK_BUY);
        quickBuy.setItem(19, setLore(new ItemStack(Material.WOOL, 16)));
        quickBuy.setItem(28, setLore(new ItemStack(Material.WOOD, 16)));
        quickBuy.setItem(37, setLore(new ItemStack(Material.GLASS, 4)));
        quickBuy.setItem(46, setLore(new ItemStack(Material.ENDER_STONE, 16)));
        quickBuy.setItem(20, setLore(Utils.unbreakable(Material.STONE_SWORD)));
        quickBuy.setItem(29, setLore(Utils.unbreakable(Material.IRON_SWORD)));
        quickBuy.setItem(38, setLore(Utils.unbreakable(Material.DIAMOND_SWORD)));
        quickBuy.setItem(47, setLore(Utils.enchantTool(Material.STICK, Enchantment.KNOCKBACK, 2)));
        quickBuy.setItem(21, setLore(Utils.unbreakable(Material.CHAINMAIL_BOOTS)));
        quickBuy.setItem(30, setLore(Utils.unbreakable(Material.IRON_BOOTS)));
        quickBuy.setItem(39, setLore(Utils.unbreakable(Material.DIAMOND_BOOTS)));
        quickBuy.setItem(22, setLore(Utils.enchantTool(Material.WOOD_PICKAXE)));
        quickBuy.setItem(31, setLore(Utils.enchantTool(Material.IRON_PICKAXE)));
        quickBuy.setItem(40, setLore(Utils.enchantTool(Material.WOOD_AXE)));
        quickBuy.setItem(49, setLore(Utils.enchantTool(Material.IRON_AXE)));
        quickBuy.setItem(23, setLore(new ItemStack(Material.ARROW, 8)));
        quickBuy.setItem(32, setLore(Utils.unbreakable(Material.BOW)));
        quickBuy.setItem(41, setLore(Utils.enchantTool(Material.BOW, Enchantment.ARROW_DAMAGE, 4)));
        quickBuy.setItem(50, setLore(Utils.enchantTool(Material.BOW, Enchantment.ARROW_KNOCKBACK, 2)));
        quickBuy.setItem(25, setLore(new ItemStack(Material.TNT)));
        quickBuy.setItem(34, setLore(new ItemStack(Material.WATER_BUCKET)));
        quickBuy.setItem(43, setLore(new ItemStack(Material.MILK_BUCKET)));
        quickBuy.setItem(52, setLore(new ItemStack(Material.COOKED_BEEF)));

        Constants.shopItems_Blocks.foreachKeys((item, index) -> {
            inventories.get(ShopCategory.BLOCKS).setItem(index+18, setLore(item)); // 18 is the offset, because <= 17 is category zone
        });
        Constants.shopItems_Melee.foreachKeys((item, index) -> {
            inventories.get(ShopCategory.MELEE).setItem(index+18, setLore(item)); // 18 is the offset, because <= 17 is category zone
        });
        Constants.shopItems_Armor.foreachKeys((item, index) -> {
            inventories.get(ShopCategory.ARMOR).setItem(index+18, setLore(item)); // 18 is the offset, because <= 17 is category zone
        });
        Constants.shopItems_Tools.foreachKeys((item, index) -> {
            inventories.get(ShopCategory.TOOLS).setItem(index+18, setLore(item)); // 18 is the offset, because <= 17 is category zone
        });
        Constants.shopItems_Ranged.foreachKeys((item, index) -> {
            inventories.get(ShopCategory.RANGED).setItem(index+18, setLore(item)); // 18 is the offset, because <= 17 is category zone
        });
        //Constants.shopItems_Potions.foreachKeys((item, index) -> {
        //    inventories.get(ShopCategory.POTIONS).setItem(index+18, setLore(item)); // 18 is the offset, because <= 17 is category zone
        //});
        Constants.shopItems_Utility.foreachKeys((item, index) -> {
            inventories.get(ShopCategory.UTILITY).setItem(index+18, setLore(item)); // 18 is the offset, because <= 17 is category zone
        });
    }

    private ItemStack setLore(ItemStack item) {
        ItemStack cost = Constants.shopItems_everything.get(item);
        if (cost == null) throw new NullPointerException("Couldn't find cost data for item: " + item);
        ItemMeta meta = item.getItemMeta();
        String[] a = { ChatColor.YELLOW + "Cost: " + ChatColor.GOLD + cost.getAmount() + " " + Utils.getFriendlyName(cost) };
        meta.setLore(Arrays.asList(a));
        item.setItemMeta(meta);
        return item;
    }

    private void initializeItems() {
        initializeCategoryItems();
        initializeQuickBuyItems();
    }

    private ItemStack categoryItem(Material material, String name) {
        return categoryItem(material, name, (byte) 0);
    }

    private ItemStack categoryItem(Material material, String name, byte data) {
        ItemStack item = new ItemStack(material, 1, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        String[] a = { ChatColor.YELLOW + "Click to view!" };
        meta.setLore(Arrays.asList(a));
        item.setItemMeta(meta);
        return item;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory().getHolder() != this) {
            return;
        }
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
        if (e.getSlot() == 0) {
            p.closeInventory();
            p.openInventory(inventories.get(ShopCategory.QUICK_BUY));
            p.updateInventory();
        } else if (e.getSlot() == 1) {
            p.closeInventory();
            p.openInventory(inventories.get(ShopCategory.BLOCKS));
            p.updateInventory();
        } else if (e.getSlot() == 2) {
            p.closeInventory();
            p.openInventory(inventories.get(ShopCategory.MELEE));
            p.updateInventory();
        } else if (e.getSlot() == 3) {
            p.closeInventory();
            p.openInventory(inventories.get(ShopCategory.ARMOR));
            p.updateInventory();
        } else if (e.getSlot() == 4) {
            p.closeInventory();
            p.openInventory(inventories.get(ShopCategory.TOOLS));
            p.updateInventory();
        } else if (e.getSlot() == 5) {
            p.closeInventory();
            p.openInventory(inventories.get(ShopCategory.RANGED));
            p.updateInventory();
        } else if (e.getSlot() == 6) {
            p.closeInventory();
            p.openInventory(inventories.get(ShopCategory.POTIONS));
            p.updateInventory();
        } else if (e.getSlot() == 7) {
            p.closeInventory();
            p.openInventory(inventories.get(ShopCategory.UTILITY));
            p.updateInventory();
        }
        if (clickedItem == null || clickedItem.getType() == Material.AIR || e.getSlot() <= 17) return;
        ItemMeta meta = clickedItem.getItemMeta();
        meta.setLore(null);
        clickedItem.setItemMeta(meta);
        ItemStack cost = Constants.shopItems_everything.get(clickedItem);
        if (cost == null) {
            p.sendMessage(ChatColor.RED + "You've tried to purchase undefined item, it'll be reported to our developers.");
            ItemStack required = Constants.shopItems_everything.get(new ItemStack(clickedItem.getType(), clickedItem.getAmount()));
            throw new NullPointerException("Undefined item data: " + clickedItem.getData() + ", " + clickedItem + "\nExpected(probably): " + required + ", Data: " + (required != null ? required.getData() : null));
        }
        if (!p.getInventory().containsAtLeast(cost, cost.getAmount())) {
            p.sendMessage(ChatColor.RED + "You don't have enough items!");
            return;
        }
        p.getInventory().removeItem(cost);
        Team team = BedWars.team.get(p.getUniqueId());
        String name = team.name().toUpperCase();
        if (name.equalsIgnoreCase("AQUA")) name = "LIGHT_BLUE";
        if (clickedItem.getType() == Material.WOOL) clickedItem.setDurability(DyeColor.valueOf(name).getWoolData());
        p.getInventory().addItem(clickedItem);
        clickedItem.setDurability((byte) 0);
        clickedItem = setLore(clickedItem);
        p.sendMessage(ChatColor.GREEN + "You purchased " + ChatColor.GOLD + Utils.getFriendlyName(clickedItem));
    }
}
