package xyz.acrylicstyle.bedwars.gameevents;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.acrylicstyle.bedwars.generators.EmeraldGenerator;
import xyz.acrylicstyle.bedwars.utils.GameEvent;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class EmeraldLevel4 implements GameEvent {
    public void run() {
        EmeraldGenerator.setTime(35);
        Bukkit.broadcastMessage(ChatColor.GREEN + "Emerald Generators " + ChatColor.YELLOW + " have been upgraded to Tier " + ChatColor.RED + "IV");
        Utils.getConfigUtils().getMiddleGenerators().forEach(location -> {
            Hologram hologram = Utils.getHologram(location.toString());
            hologram.insertTextLine(0, ChatColor.YELLOW + "Tier " + ChatColor.RED + "IV");
        });
    }

    public int getTime() {
        return 60*30; // 30 minutes
    }

    @Override
    public String getName() {
        return "Emerald IV";
    }
}
