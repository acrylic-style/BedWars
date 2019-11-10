package xyz.acrylicstyle.bedwars.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.acrylicstyle.bedwars.utils.Generator;
import xyz.acrylicstyle.bedwars.utils.GeneratorPlaces;
import xyz.acrylicstyle.bedwars.utils.Team;
import xyz.acrylicstyle.bedwars.utils.Utils;

/**
 * Represents a class that generates resource and drop it to the world.
 */
public class ResourceGeneratorTask extends BukkitRunnable {
    public Generator generator;
    private Team team;

    ResourceGeneratorTask(Generator generator) {
        this.generator = generator;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void run() {
        Bukkit.getScheduler().runTaskLater(Utils.getInstance(), ResourceGeneratorTask.this, (long) (ResourceGeneratorTask.this.generator.getGenerateTime()*20));
        if (this.generator.getGeneratorPlace() == GeneratorPlaces.TEAM_BASE) {
            Location location = Utils.getConfigUtils().getGeneratorLocation(team.name().toLowerCase());
            Bukkit.getScheduler().runTask(Utils.getInstance(), () -> location.getWorld().dropItem(location, ResourceGeneratorTask.this.generator.getResource()));
        } else if (this.generator.getGeneratorPlace() == GeneratorPlaces.SEMI_MIDDLE) {
            Utils.getConfigUtils().getSemiMiddleGenerators().forEach(location -> location.getWorld().dropItem(location, this.generator.getResource()));
        } else if (this.generator.getGeneratorPlace() == GeneratorPlaces.MIDDLE) {
            Utils.getConfigUtils().getMiddleGenerators().forEach(location -> location.getWorld().dropItem(location, this.generator.getResource()));
        }
    }
}
