package xyz.acrylicstyle.bedwars.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class ToggleBlockProtection implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Utils.blockProtection = !Utils.blockProtection;
        sender.sendMessage(ChatColor.GREEN + "Block protection is now " + (Utils.blockProtection ? "Enabled!" : "Disabled!"));
        return true;
    }
}
