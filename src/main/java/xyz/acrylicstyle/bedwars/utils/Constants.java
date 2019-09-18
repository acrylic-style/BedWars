package xyz.acrylicstyle.bedwars.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.acrylicstyle.bedwars.gameevents.*;
import xyz.acrylicstyle.bedwars.generators.*;

import java.util.ArrayList;
import java.util.List;

public final class Constants {
    public static final List<Generator> generators = new ArrayList<>();
    public static final List<GameEvent> events = new ArrayList<>();
    public static final Collection<ItemStack, ItemStack> shopItems = new Collection<>();

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

        shopItems.put(new ItemStack(Material.WOOL, 16), new ItemStack(Material.IRON_INGOT, 4));
        shopItems.put(new ItemStack(Material.WOOD, 16), new ItemStack(Material.GOLD_INGOT, 4));
        shopItems.put(new ItemStack(Material.GLASS, 4), new ItemStack(Material.IRON_INGOT, 12));
        shopItems.put(new ItemStack(Material.ENDER_STONE, 16), new ItemStack(Material.IRON_INGOT, 24));
    }
}
