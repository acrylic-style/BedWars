package xyz.acrylicstyle.bedwars.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import util.Collection;
import xyz.acrylicstyle.bedwars.generators.GoldGenerator;
import xyz.acrylicstyle.bedwars.generators.IronGenerator;
import xyz.acrylicstyle.bedwars.utils.*;

import java.util.Arrays;

public class GeneratorTask extends BukkitRunnable {
    @Override
    public void run() {
        Constants.generators.forEach(generator -> {
            if (generator instanceof GoldGenerator || generator instanceof IronGenerator) {
                Arrays.asList(Team.values()).forEach(team -> {
                    Generator newGenerator = generator.clone(); // you can clone as many as you want
                    ResourceGeneratorTask task = new ResourceGeneratorTask(newGenerator);
                    if (newGenerator instanceof GoldGenerator) ((GoldGenerator) newGenerator).setTeam(team);
                    if (newGenerator instanceof IronGenerator) ((IronGenerator) newGenerator).setTeam(team);
                    task.setTeam(team);
                    task.runTaskLater(Utils.getInstance(), (long) newGenerator.getGenerateTime() * 20);
                    if (Constants.resourceGeneratorTask.get(team) == null)
                        Constants.resourceGeneratorTask.put(team, new Collection<>());
                    Collection<Generator, ResourceGeneratorTask> collection = Constants.resourceGeneratorTask.get(team);
                    collection.put(newGenerator, task);
                    Constants.resourceGeneratorTask.put(team, collection);
                });
            } else {
                ResourceGeneratorTask task = new ResourceGeneratorTask(generator);
                task.runTaskTimer(Utils.getInstance(), 0, (long) (generator.getGenerateTime() * 20));
            }
        });
    }
}
