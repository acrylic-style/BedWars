package xyz.acrylicstyle.bedwars.upgrades;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.acrylicstyle.bedwars.utils.Team;
import xyz.acrylicstyle.bedwars.utils.Trap;
import xyz.acrylicstyle.bedwars.utils.Traps;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class MiningFatigueTrap implements TrapUpgrade, OneTimeUpgrade<Team> {
    @Override
    public ItemStack getCost() {
        return new ItemStack(Material.DIAMOND, 2);
    }

    @Override
    public String getName() {
        return "Mining Fatigue Trap (10 seconds mining fatigue)";
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.IRON_PICKAXE);
    }

    @Override
    public void run(Team team) {
        Utils.traps.get(team).add(new Trap(Traps.MINING_FATIGUE, team));
    }
}
