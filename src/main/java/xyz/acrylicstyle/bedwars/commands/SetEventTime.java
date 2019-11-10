package xyz.acrylicstyle.bedwars.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class SetEventTime implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Please specify one more argument! (+xx faster event time)");
            return true;
        }
        int eventTime;
        try {
            eventTime = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Please specify a valid number!");
            return true;
        }
        Utils.eventTime = eventTime;
        sender.sendMessage(ChatColor.GREEN + "Event time is now " + args[0] + "x faster");
        return true;
    }
}
