package xyz.acrylicstyle.bedwars.gameevents;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.acrylicstyle.bedwars.generators.DiamondGenerator;
import xyz.acrylicstyle.bedwars.utils.GameEvent;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class DiamondLevel3 implements GameEvent {
    public void run() {
        DiamondGenerator.setTime(15);
        Bukkit.broadcastMessage(ChatColor.AQUA + "Diamond Generators " + ChatColor.YELLOW + " have been upgraded to Tier " + ChatColor.RED + "III");
        Utils.getConfigUtils().getSemiMiddleGenerators().forEach(location -> {
            Hologram hologram = Utils.getHologram(location.toString());
            hologram.insertTextLine(0, ChatColor.YELLOW + "Tier " + ChatColor.RED + "III");
        });
    }

    public int getTime() {
        return 60*15; // 15 minutes
    }

    @Override
    public String getName() {
        return "Diamond III";
    }
}
