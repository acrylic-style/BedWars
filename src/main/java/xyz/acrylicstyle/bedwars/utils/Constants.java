package xyz.acrylicstyle.bedwars.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import xyz.acrylicstyle.bedwars.gameevents.*;
import xyz.acrylicstyle.bedwars.generators.*;

import java.util.ArrayList;
import java.util.List;

public final class Constants {
    public static final List<Generator> generators = new ArrayList<>();
    public static final List<GameEvent> events = new ArrayList<>();
    public static final Collection<ItemStack, ItemStack> shopItems_everything = new Collection<>();
    public static final Collection<ItemStack, ItemStack> shopItems_Blocks = new Collection<>();
    public static final Collection<ItemStack, ItemStack> shopItems_Melee = new Collection<>();
    public static final Collection<ItemStack, ItemStack> shopItems_Armor = new Collection<>();
    public static final Collection<ItemStack, ItemStack> shopItems_Tools = new Collection<>();

    static {
        generators.add(new IronGenerator());
        generators.add(new GoldGenerator());
        generators.add(new DiamondGenerator());
        generators.add(new EmeraldGenerator());

        events.add(new DiamondLevel2()); // 6 minutes
        events.add(new EmeraldLevel2()); // 10 minutes
        events.add(new DiamondLevel3()); // 15 minutes
        events.add(new EmeraldLevel3()); // 20 minutes
        events.add(new DiamondLevel4()); // 25 minutes
        events.add(new EmeraldLevel4()); // 30 minutes
        events.add(new DiamondLevel5()); // 35 minutes
        events.add(new EmeraldLevel5()); // 40 minutes
        events.add(new DiamondLevel6()); // 45 minutes
        events.add(new EmeraldLevel6()); // 48 minutes
        events.add(new BedDestruction()); // 50 minutes
        events.add(new SuddenDeath()); // 55 minutes
        events.add(new GameEnd()); // 60 minutes
        events.add(new KickAll()); // 61 minutes

        shopItems_Blocks.put(new ItemStack(Material.WOOL, 16), new ItemStack(Material.IRON_INGOT, 4));
        shopItems_Blocks.put(new ItemStack(Material.WOOD, 16), new ItemStack(Material.GOLD_INGOT, 4));
        shopItems_Blocks.put(new ItemStack(Material.GLASS, 4), new ItemStack(Material.IRON_INGOT, 12));
        shopItems_Blocks.put(new ItemStack(Material.ENDER_STONE, 16), new ItemStack(Material.IRON_INGOT, 24));

        shopItems_Melee.put(Utils.unbreakable(Material.STONE_SWORD), new ItemStack(Material.IRON_INGOT, 10));
        shopItems_Melee.put(Utils.unbreakable(Material.IRON_SWORD), new ItemStack(Material.GOLD_INGOT, 7));
        shopItems_Melee.put(Utils.unbreakable(Material.DIAMOND_SWORD), new ItemStack(Material.EMERALD, 4));
        shopItems_Melee.put(Utils.enchantTool(Material.STICK, Enchantment.KNOCKBACK, 2), new ItemStack(Material.GOLD_INGOT, 10));

        shopItems_Armor.put(Utils.unbreakable(Material.CHAINMAIL_BOOTS), new ItemStack(Material.IRON_INGOT, 40));
        shopItems_Armor.put(Utils.unbreakable(Material.IRON_BOOTS), new ItemStack(Material.GOLD_INGOT, 12));
        shopItems_Armor.put(Utils.unbreakable(Material.DIAMOND_BOOTS), new ItemStack(Material.EMERALD, 6));

        shopItems_Tools.put(Utils.enchantTool(Material.WOOD_PICKAXE), new ItemStack(Material.IRON_INGOT, 10));
        shopItems_Tools.put(Utils.enchantTool(Material.IRON_PICKAXE), new ItemStack(Material.IRON_INGOT, 20));
        shopItems_Tools.put(Utils.enchantTool(Material.GOLD_PICKAXE), new ItemStack(Material.GOLD_INGOT, 13));
        shopItems_Tools.put(Utils.enchantTool(Material.DIAMOND_PICKAXE), new ItemStack(Material.GOLD_INGOT, 19));
        shopItems_Tools.put(Utils.enchantTool(Material.WOOD_AXE), new ItemStack(Material.IRON_INGOT, 10));
        shopItems_Tools.put(Utils.enchantTool(Material.IRON_AXE), new ItemStack(Material.IRON_INGOT, 20));
        shopItems_Tools.put(Utils.enchantTool(Material.GOLD_AXE), new ItemStack(Material.GOLD_INGOT, 13));
        shopItems_Tools.put(Utils.enchantTool(Material.DIAMOND_AXE), new ItemStack(Material.GOLD_INGOT, 19));

        shopItems_everything.addAll(shopItems_Blocks);
        shopItems_everything.addAll(shopItems_Melee);
        shopItems_everything.addAll(shopItems_Armor);
        shopItems_everything.addAll(shopItems_Tools);
    }
}
