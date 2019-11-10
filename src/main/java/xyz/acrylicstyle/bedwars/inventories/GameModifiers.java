package xyz.acrylicstyle.bedwars.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

public class GameModifiers implements InventoryHolder, Listener {
    private Inventory inventory;
    private Collection<Modifier, GameModifiersGroup> groups = new Collection<>();
    private Collection<Integer, Modifier> modifiers = new Collection<>();
    private Collection<Modifier, ItemStack> items = new Collection<>();
    private Collection<Modifier, Boolean> statuses = new Collection<>();

    public GameModifiers() {
        inventory = Bukkit.createInventory(this, 9*6, "Game Modifiers");
        initializeItems();
    }

    private void initializeItems() {
        AtomicInteger index = new AtomicInteger();
        Constants.modifiers.foreach((modifier, i) -> {
            if (modifier.groupOf() == null) {
                ItemStack item = modifier.getItem();
                ItemMeta meta = item.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
                List<String> lore = new ArrayList<>();
                meta.setDisplayName(ChatColor.GREEN + ChatColor.translateAlternateColorCodes('&', modifier.getName()));
                modifier.getDescription().forEach(d -> lore.add(ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', d)));
                meta.setLore(lore);
                meta.spigot().setUnbreakable(true);
                item.setItemMeta(meta);
                statuses.add(modifier, modifier.defaultStatus());
                items.add(modifier, item);
                modifiers.add(index.get()*2, modifier);
                inventory.setItem(index.getAndIncrement()*2, item);
            }
            if (modifier.isGroup()) {
                GameModifiersGroup gmg = new GameModifiersGroup(modifier);
                groups.add(modifier, gmg);
                Bukkit.getPluginManager().registerEvents(gmg, Utils.getInstance());
            }
        });
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
        Modifier modifier = modifiers.get(e.getRawSlot());
        e.getWhoClicked().sendMessage("Slot: " + e.getRawSlot());
        if (modifier == null) return;
        boolean status = statuses.get(modifier);
        if (status) {
            modifier.down();
            ItemStack item = this.items.get(modifier);
            ItemMeta meta = item.getItemMeta();
            meta.removeEnchant(Enchantment.PROTECTION_ENVIRONMENTAL);
            item.setItemMeta(meta);
            inventory.setItem(e.getRawSlot(), item);
            statuses.add(modifier, false);
        } else {
            modifier.up();
            ItemStack item = this.items.get(modifier);
            ItemMeta meta = item.getItemMeta();
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
            item.setItemMeta(meta);
            inventory.setItem(e.getRawSlot(), item);
            statuses.add(modifier, true);
        }
        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_PLING, 100, 2);
    }
}
