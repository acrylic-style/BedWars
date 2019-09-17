package xyz.acrylicstyle.bedwars.tasks;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.bedwars.utils.Generator;
import xyz.acrylicstyle.bedwars.utils.GeneratorPlaces;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class ResourceGeneratorTask extends BukkitRunnable {
    private Generator generator;

    ResourceGeneratorTask(Generator generator) {
        this.generator = generator;
    }

    @Override
    public void run() {
        if (this.generator.getGeneratorPlace() == GeneratorPlaces.TEAM_BASE) {
            BedWars.aliveTeam.forEach(team -> {
                Location location = Utils.getConfigUtils().getGeneratorLocation(team.name().toLowerCase());
                location.getWorld().dropItem(location, this.generator.getResource());
            });
        } else if (this.generator.getGeneratorPlace() == GeneratorPlaces.SEMI_MIDDLE) {
            Utils.getConfigUtils().getSemiMiddleGenerators().forEach(location -> location.getWorld().dropItem(location, this.generator.getResource()));
        } else if (this.generator.getGeneratorPlace() == GeneratorPlaces.MIDDLE) {
            Utils.getConfigUtils().getMiddleGenerators().forEach(location -> location.getWorld().dropItem(location, this.generator.getResource()));
        }
    }
}
