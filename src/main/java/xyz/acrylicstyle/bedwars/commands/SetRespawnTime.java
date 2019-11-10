package xyz.acrylicstyle.bedwars.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class SetRespawnTime implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Please specify one more argument! (x Respawn time)");
            return true;
        }
        int respawnTime;
        try {
            respawnTime = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Please specify a valid number!");
            return true;
        }
        Utils.respawnTime = respawnTime;
        sender.sendMessage(ChatColor.GREEN + "Respawn time is now " + args[0] + " seconds!");
        return true;
    }
}
