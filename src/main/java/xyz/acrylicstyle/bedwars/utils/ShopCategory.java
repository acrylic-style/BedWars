package xyz.acrylicstyle.bedwars.utils;

import org.bukkit.Material;

public enum ShopCategory {
    QUICK_BUY("Quick Buy", Material.NETHER_STAR),
    BLOCKS("Blocks", Material.STAINED_CLAY),
    MELEE("Melee", Material.GOLD_SWORD),
    ARMOR("Armor", Material.CHAINMAIL_BOOTS),
    TOOLS("Tools", Material.STONE_PICKAXE),
    RANGED("Ranged", Material.BOW),
    POTIONS("Potions", Material.BREWING_STAND_ITEM),
    UTILITY("Utility", Material.TNT);

    public String name;
    public Material material;

    ShopCategory(String name, Material material) {
        this.name = name;
        this.material = material;
    }
}
