package xyz.acrylicstyle.bedwars.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class ToggleCrafting implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Utils.crafting = !Utils.crafting;
        sender.sendMessage(ChatColor.GREEN + "Now you are " + (Utils.crafting ? "able" : "not able") + " to craft!");
        return true;
    }
}
