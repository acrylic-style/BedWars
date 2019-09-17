package xyz.acrylicstyle.bedwars.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import xyz.acrylicstyle.bedwars.utils.Constants;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class GeneratorTask extends BukkitRunnable {
    @Override
    public void run() {
        Constants.generators.forEach(generator -> {
            ResourceGeneratorTask task = new ResourceGeneratorTask(generator);
            task.runTaskTimer(Utils.getInstance(), 0, generator.getGenerateTime()*20);
        });
    }
}
