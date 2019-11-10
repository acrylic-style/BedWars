package xyz.acrylicstyle.bedwars.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import util.Collection;
import xyz.acrylicstyle.bedwars.gamemod.Modifier;
import xyz.acrylicstyle.bedwars.utils.Constants;
import xyz.acrylicstyle.bedwars.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GameModifiersGroup implements InventoryHolder, Listener {
    private Inventory inventory;
    private Modifier modifier;
    private Collection<Integer, Modifier> modifiers = new Collection<>();
    private Collection<Modifier, Boolean> statuses = new Collection<>();
    private Collection<Modifier, ItemStack> items = new Collection<>();

    public GameModifiersGroup(Modifier modifier) {
        this.modifier = modifier;
        inventory = Bukkit.createInventory(this, 9*4, "Game Modifiers - " + modifier.getName());
        initializeItems();
    }

    private void initializeItems() {
        AtomicInteger index = new AtomicInteger();
        Constants.modifiers.filter(m -> m.groupOf() != null && m.groupOf().getClass().isInstance(modifier)).forEach(modifier -> {
            ItemStack item = modifier.getItem();
            ItemMeta meta = item.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
            List<String> lore = new ArrayList<>();
            meta.setDisplayName(ChatColor.GREEN + ChatColor.translateAlternateColorCodes('&', modifier.getName()));
            modifier.getDescription().forEach(d -> lore.add(ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', d)));
            meta.setLore(lore);
            meta.spigot().setUnbreakable(true);
            if (modifier.defaultStatus()) meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
            item.setItemMeta(meta);
            this.modifiers.add(index.get()*2, modifier);
            this.statuses.add(modifier, modifier.defaultStatus());
            this.items.add(modifier, item);
            inventory.setItem(index.getAndIncrement()*2, item);
        });
        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta meta = back.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Back");
        back.setItemMeta(meta);
        inventory.setItem(31, back);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory().getHolder() != this) return;
        if (e.getCurrentItem() == null) return;
        e.setCancelled(true);
        if (e.getRawSlot() == 31) {
            e.getWhoClicked().openInventory(Utils.gameModifiers.getInventory());
            return;
        }
        Modifier modifier = modifiers.get(e.getRawSlot());
        if (modifier == null) return;
        modifiers.foreach((mod, i) -> {
            mod.down();
            ItemStack item = this.items.get(modifier);
            ItemMeta meta = item.getItemMeta();
            meta.removeEnchant(Enchantment.PROTECTION_ENVIRONMENTAL);
            meta.setDisplayName(meta.getDisplayName().replaceFirst(" " + ChatColor.GREEN + ChatColor.BOLD + "SELECTED", ""));
            e.getWhoClicked().sendMessage("slot: " + modifiers.values(modifier).firstKey());
            item.setItemMeta(meta);
            inventory.setItem(modifiers.values(modifier).firstKey(), item);
        });
        modifier.up();
        ItemStack item = this.items.get(modifier);
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        meta.setDisplayName(meta.getDisplayName() + " " + ChatColor.GREEN + ChatColor.BOLD + "SELECTED");
        item.setItemMeta(meta);
        inventory.setItem(e.getRawSlot(), item);
        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_PLING, 100, 2);
    }
}
