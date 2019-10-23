package xyz.acrylicstyle.bedwars.inventories;

import com.avaje.ebean.validation.NotNull;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionType;
import util.Collection;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.utils.*;
import xyz.acrylicstyle.tomeito_core.utils.Log;

import java.util.Arrays;

public class ItemShop implements InventoryHolder, Listener {
    private final Collection<ShopCategory, Inventory> inventories = new Collection<>();
    private final Collection<ShopCategory, Collection<Integer, ItemStack>> noLoreItems = new Collection<>();

    public ItemShop() {
        inventories.add(ShopCategory.QUICK_BUY, Bukkit.createInventory(this, 9*6, "Item Shop - Quick Buy"));
        inventories.add(ShopCategory.BLOCKS, Bukkit.createInventory(this, 9*6, "Item Shop - Blocks"));
        inventories.add(ShopCategory.MELEE, Bukkit.createInventory(this, 9*6, "Item Shop - Melee"));
        inventories.add(ShopCategory.ARMOR, Bukkit.createInventory(this, 9*6, "Item Shop - Armor"));
        inventories.add(ShopCategory.TOOLS, Bukkit.createInventory(this, 9*6, "Item Shop - Tools"));
        inventories.add(ShopCategory.RANGED, Bukkit.createInventory(this, 9*6, "Item Shop - Ranged"));
        inventories.add(ShopCategory.POTIONS, Bukkit.createInventory(this, 9*6, "Item Shop - Potions"));
        inventories.add(ShopCategory.UTILITY, Bukkit.createInventory(this, 9*6, "Item Shop - Utility"));
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
        quickBuy.setItem(19, setLore(19, new ItemStack(Material.WOOL, 16), ShopCategory.QUICK_BUY));
        quickBuy.setItem(28, setLore(28, new ItemStack(Material.WOOD, 16), ShopCategory.QUICK_BUY));
        quickBuy.setItem(37, setLore(37, new ItemStack(Material.GLASS, 4), ShopCategory.QUICK_BUY));
        quickBuy.setItem(46, setLore(46, new ItemStack(Material.ENDER_STONE, 16), ShopCategory.QUICK_BUY));
        quickBuy.setItem(20, setLore(20,Utils.unbreakable(Material.STONE_SWORD), ShopCategory.QUICK_BUY));
        quickBuy.setItem(29, setLore(29, Utils.unbreakable(Material.IRON_SWORD), ShopCategory.QUICK_BUY));
        quickBuy.setItem(38, setLore(38, Utils.unbreakable(Material.DIAMOND_SWORD), ShopCategory.QUICK_BUY));
        quickBuy.setItem(47, setLore(47, Utils.enchantTool(Material.STICK, Enchantment.KNOCKBACK, 2), ShopCategory.QUICK_BUY));
        quickBuy.setItem(21, setLore(21, Utils.unbreakable(Material.CHAINMAIL_BOOTS), ShopCategory.QUICK_BUY));
        quickBuy.setItem(30, setLore(30, Utils.unbreakable(Material.IRON_BOOTS), ShopCategory.QUICK_BUY));
        quickBuy.setItem(39, setLore(39, Utils.unbreakable(Material.DIAMOND_BOOTS), ShopCategory.QUICK_BUY));
        quickBuy.setItem(22, setLore(22, Utils.enchantTool(Material.WOOD_PICKAXE), ShopCategory.QUICK_BUY));
        quickBuy.setItem(31, setLore(31, Utils.enchantTool(Material.IRON_PICKAXE), ShopCategory.QUICK_BUY));
        quickBuy.setItem(40, setLore(40, Utils.enchantTool(Material.WOOD_AXE), ShopCategory.QUICK_BUY));
        quickBuy.setItem(49, setLore(49, Utils.enchantTool(Material.IRON_AXE), ShopCategory.QUICK_BUY));
        quickBuy.setItem(23, setLore(23, new ItemStack(Material.ARROW, 8), ShopCategory.QUICK_BUY));
        quickBuy.setItem(32, setLore(32, Utils.unbreakable(Material.BOW), ShopCategory.QUICK_BUY));
        quickBuy.setItem(41, setLore(41, Utils.enchantTool(Material.BOW, Enchantment.ARROW_DAMAGE, 4), ShopCategory.QUICK_BUY));
        quickBuy.setItem(50, setLore(50, Utils.enchantTool(Material.BOW, Enchantment.ARROW_KNOCKBACK, 2), ShopCategory.QUICK_BUY));
        quickBuy.setItem(24, setLore(24, Utils.getPotionItemStack(PotionType.JUMP, 5, 45, ChatColor.AQUA + "Jump Potion V (45 seconds)"), ShopCategory.QUICK_BUY));
        quickBuy.setItem(33, setLore(33, Utils.getPotionItemStack(PotionType.SPEED, 2, 45, ChatColor.AQUA + "Speed Potion II (45 seconds)"), ShopCategory.QUICK_BUY));
        quickBuy.setItem(42, setLore(42, Utils.getPotionItemStack(PotionType.INVISIBILITY, 1, 30, ChatColor.AQUA + "Invisibility Potion (30 seconds)"), ShopCategory.QUICK_BUY));
        quickBuy.setItem(42, setLore(51, Utils.getPotionItemStack(PotionType.REGEN, 2, 10, ChatColor.AQUA + "Regen Potion II (10 seconds)"), ShopCategory.QUICK_BUY));
        quickBuy.setItem(25, setLore(25, new ItemStack(Material.TNT), ShopCategory.QUICK_BUY));
        quickBuy.setItem(34, setLore(34, new ItemStack(Material.WATER_BUCKET), ShopCategory.QUICK_BUY));
        quickBuy.setItem(43, setLore(43, new ItemStack(Material.MILK_BUCKET), ShopCategory.QUICK_BUY));
        quickBuy.setItem(52, setLore(52, new ItemStack(Material.GOLDEN_APPLE), ShopCategory.QUICK_BUY));

        Constants.shopItems_Blocks.foreachKeys((item, index) -> {
            inventories.get(ShopCategory.BLOCKS).setItem(index+18, setLore(index+18, item, ShopCategory.BLOCKS)); // 18 is the offset, because <= 17 is category zone
        });
        Constants.shopItems_Melee.foreachKeys((item, index) -> {
            inventories.get(ShopCategory.MELEE).setItem(index+18, setLore(index+18, item, ShopCategory.MELEE)); // 18 is the offset, because <= 17 is category zone
        });
        Constants.shopItems_Armor.foreachKeys((item, index) -> {
            inventories.get(ShopCategory.ARMOR).setItem(index+18, setLore(index+18, item, ShopCategory.ARMOR)); // 18 is the offset, because <= 17 is category zone
        });
        Constants.shopItems_Tools.foreachKeys((item, index) -> {
            inventories.get(ShopCategory.TOOLS).setItem(index+18, setLore(index+18, item, ShopCategory.TOOLS)); // 18 is the offset, because <= 17 is category zone
        });
        Constants.shopItems_Ranged.foreachKeys((item, index) -> {
            inventories.get(ShopCategory.RANGED).setItem(index+18, setLore(index+18, item, ShopCategory.RANGED)); // 18 is the offset, because <= 17 is category zone
        });
        Constants.shopItems_Potions.foreachKeys((item, index) -> {
            inventories.get(ShopCategory.POTIONS).setItem(index+18, setLore(index+18, item, ShopCategory.POTIONS)); // 18 is the offset, because <= 17 is category zone
        });
        Constants.shopItems_Utility.foreachKeys((item, index) -> {
            inventories.get(ShopCategory.UTILITY).setItem(index+18, setLore(index+18, item, ShopCategory.UTILITY)); // 18 is the offset, because <= 17 is category zone
        });
    }

    private ItemStack setLore(int slot, ItemStack item, ShopCategory shopCategory) {
        if (noLoreItems.get(shopCategory) == null) noLoreItems.put(shopCategory, new Collection<>());
        Collection<Integer, ItemStack> collection = noLoreItems.get(shopCategory);
        collection.put(slot, item.clone());
        noLoreItems.put(shopCategory, collection.clone());
        item = item.clone();
        ItemStack cost = Constants.shopItems_everything.get(item);
        if (cost == null) throw new NullPointerException("Couldn't find cost data for item: " + item);
        ItemMeta meta = item.getItemMeta();
        String name = Utils.getFriendlyName(cost);
        ChatColor color = null;
        if (name.equalsIgnoreCase("Iron ingot")) color = ChatColor.WHITE;
        if (name.equalsIgnoreCase("Gold ingot")) color = ChatColor.GOLD;
        if (name.equalsIgnoreCase("Diamond")) color = ChatColor.AQUA;
        if (name.equalsIgnoreCase("Emerald")) color = ChatColor.GREEN;
        if (color == null) color = ChatColor.GRAY;
        String[] a = { ChatColor.YELLOW + "Cost: " + color + cost.getAmount() + " " + name.replaceAll("ingot", "") };
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
        if (e.getClickedInventory().getHolder() != this) return;
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem().clone();
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
        ItemStack item = noLoreItems.get(inventories.values(e.getClickedInventory()).keysList().first()).get(e.getSlot()).clone(); // clone for avoid bug
        ItemStack cost = Constants.shopItems_everything.get(item);
        if (cost == null) {
            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 100, 0.5F);
            p.sendMessage(ChatColor.RED + "You've tried to purchase undefined item, it'll be reported to our developers.");
            Log.debug("Amount: " + item.getAmount());
            Log.debug("Data: " + item.getData());
            Log.debug("Lore: " + item.getItemMeta().getLore());
            Log.debug("Unbreakable: " + item.getItemMeta().spigot().isUnbreakable());
            Log.debug("ItemMeta: " + item.getItemMeta());
            throw new NullPointerException("Undefined item data: " + item);
        }
        if (!p.getInventory().containsAtLeast(cost, cost.getAmount())) {
            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 100, 1);
            p.sendMessage(ChatColor.RED + "You don't have enough items!");
            return;
        }
        if (item.getType() == Material.DIAMOND_BOOTS) {
            if (Constants.wearingArmor.get(e.getWhoClicked().getUniqueId()).ordinal() <= PlayerArmor.DIAMOND.ordinal()) {
                // never happens
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 100, 1);
                p.sendMessage(ChatColor.RED + "You already have greater armor!");
                return;
            }
            p.getInventory().removeItem(cost);
            Constants.wearingArmor.add(e.getWhoClicked().getUniqueId(), PlayerArmor.DIAMOND);
            e.getWhoClicked().getInventory().setBoots(Utils.unbreakable(Material.DIAMOND_BOOTS));
            e.getWhoClicked().getInventory().setLeggings(Utils.unbreakable(Material.DIAMOND_LEGGINGS));
        } else if (item.getType() == Material.IRON_BOOTS) {
            if (Constants.wearingArmor.get(e.getWhoClicked().getUniqueId()).ordinal() <= PlayerArmor.IRON.ordinal()) {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 100, 1);
                p.sendMessage(ChatColor.RED + "You already have greater armor!");
                return;
            }
            p.getInventory().removeItem(cost);
            Constants.wearingArmor.add(e.getWhoClicked().getUniqueId(), PlayerArmor.IRON);
            e.getWhoClicked().getInventory().setBoots(Utils.unbreakable(Material.IRON_BOOTS));
            e.getWhoClicked().getInventory().setLeggings(Utils.unbreakable(Material.IRON_LEGGINGS));
        } else if (item.getType() == Material.CHAINMAIL_BOOTS) {
            if (Constants.wearingArmor.get(e.getWhoClicked().getUniqueId()).ordinal() <= PlayerArmor.CHAIN.ordinal()) {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 100, 1);
                p.sendMessage(ChatColor.RED + "You already have greater armor!");
                return;
            }
            p.getInventory().removeItem(cost);
            Constants.wearingArmor.add(e.getWhoClicked().getUniqueId(), PlayerArmor.CHAIN);
            e.getWhoClicked().getInventory().setBoots(Utils.unbreakable(Material.CHAINMAIL_BOOTS));
            e.getWhoClicked().getInventory().setLeggings(Utils.unbreakable(Material.CHAINMAIL_LEGGINGS));
        } else {
            p.getInventory().removeItem(cost);
            Team team = BedWars.team.get(p.getUniqueId());
            String name = team.name().toUpperCase();
            if (name.equalsIgnoreCase("AQUA")) name = "LIGHT_BLUE";
            if (item.getType() == Material.WOOL) item.setDurability(DyeColor.valueOf(name).getWoolData());
            p.getInventory().addItem(item);
        }
        p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 2);
        p.sendMessage(ChatColor.GREEN + "You purchased " + ChatColor.GOLD + Utils.getFriendlyName(item));
    }
}
