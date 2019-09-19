package xyz.acrylicstyle.bedwars.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import xyz.acrylicstyle.bedwars.gameevents.*;
import xyz.acrylicstyle.bedwars.generators.DiamondGenerator;
import xyz.acrylicstyle.bedwars.generators.EmeraldGenerator;
import xyz.acrylicstyle.bedwars.generators.GoldGenerator;
import xyz.acrylicstyle.bedwars.generators.IronGenerator;
import xyz.acrylicstyle.bedwars.upgrades.ReinforcedArmor;
import xyz.acrylicstyle.bedwars.upgrades.SharpenedSword;
import xyz.acrylicstyle.bedwars.upgrades.Upgrade;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Constants {
    public static final List<Generator> generators = new ArrayList<>();
    public static final List<GameEvent> events = new ArrayList<>();
    public static final CollectionList<Upgrade> upgrades = new CollectionList<>();
    public static final Collection<ItemStack, ItemStack> shopItems_everything = new Collection<>();
    public static final Collection<ItemStack, ItemStack> shopItems_Blocks = new Collection<>();
    public static final Collection<ItemStack, ItemStack> shopItems_Melee = new Collection<>();
    public static final Collection<ItemStack, ItemStack> shopItems_Armor = new Collection<>();
    public static final Collection<ItemStack, ItemStack> shopItems_Tools = new Collection<>();
    public static final Collection<ItemStack, ItemStack> shopItems_Ranged = new Collection<>();
    public static final Collection<ItemStack, ItemStack> shopItems_Potions = new Collection<>();
    public static final Collection<ItemStack, ItemStack> shopItems_Utility = new Collection<>();
    public static final Collection<UUID, PlayerArmor> wearingArmor = new Collection<>();
    public static final boolean keepInventory = false;

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
        shopItems_Tools.put(Utils.unbreakable(Material.SHEARS), new ItemStack(Material.IRON_INGOT, 20));

        shopItems_Ranged.put(new ItemStack(Material.ARROW, 8), new ItemStack(Material.GOLD_INGOT, 2));
        shopItems_Ranged.put(Utils.unbreakable(Material.BOW), new ItemStack(Material.GOLD_INGOT, 12));
        shopItems_Ranged.put(Utils.enchantTool(Material.BOW, Enchantment.ARROW_DAMAGE, 4), new ItemStack(Material.EMERALD, 4));
        shopItems_Ranged.put(Utils.enchantTool(Material.BOW, Enchantment.ARROW_KNOCKBACK, 2), new ItemStack(Material.EMERALD, 10));
        shopItems_Ranged.put(Utils.enchantTool(Material.BOW, Enchantment.ARROW_KNOCKBACK, 10), new ItemStack(Material.EMERALD, 64));

        // shopItems_Potions.put(itemStack, itemStack); // ???

        shopItems_Utility.put(new ItemStack(Material.TNT), new ItemStack(Material.GOLD_INGOT, 8));
        shopItems_Utility.put(new ItemStack(Material.WATER_BUCKET), new ItemStack(Material.GOLD_INGOT, 4));
        shopItems_Utility.put(new ItemStack(Material.MILK_BUCKET), new ItemStack(Material.GOLD_INGOT, 6));
        shopItems_Utility.put(new ItemStack(Material.COOKED_BEEF), new ItemStack(Material.IRON_INGOT, 1));

        shopItems_everything.addAll(shopItems_Blocks);
        shopItems_everything.addAll(shopItems_Melee);
        shopItems_everything.addAll(shopItems_Armor);
        shopItems_everything.addAll(shopItems_Tools);
        shopItems_everything.addAll(shopItems_Potions);
        shopItems_everything.addAll(shopItems_Ranged);
        shopItems_everything.addAll(shopItems_Utility);

        upgrades.add(new SharpenedSword());
        upgrades.add(new ReinforcedArmor());
    }
}
