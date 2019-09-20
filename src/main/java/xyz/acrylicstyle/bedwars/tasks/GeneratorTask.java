package xyz.acrylicstyle.bedwars.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import xyz.acrylicstyle.bedwars.generators.GoldGenerator;
import xyz.acrylicstyle.bedwars.generators.IronGenerator;
import xyz.acrylicstyle.bedwars.utils.*;

import java.util.Arrays;

public class GeneratorTask extends BukkitRunnable {
    @Override
    public void run() {
        Constants.generators.forEach(generator -> Arrays.asList(Team.values()).forEach(team -> {
            ResourceGeneratorTask task = new ResourceGeneratorTask(generator);
            if (generator instanceof GoldGenerator) ((GoldGenerator) generator).setTeam(team);
            if (generator instanceof IronGenerator) ((IronGenerator) generator).setTeam(team);
            task.setTeam(team);
            task.runTaskTimer(Utils.getInstance(), 0, (long) (generator.getGenerateTime() * 20));
            if (Constants.resourceGeneratorTask.get(team) == null) Constants.resourceGeneratorTask.put(team, new Collection<>());
            Collection<Generator, ResourceGeneratorTask> collection = Constants.resourceGeneratorTask.get(team);
            collection.put(generator, task);
            Constants.resourceGeneratorTask.put(team, collection);
        }));
    }
}
