package xyz.acrylicstyle.bedwars.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.acrylicstyle.bedwars.tasks.GameTask;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class Modifiers implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("no u");
            return true;
        }
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You are no longer OP.");
            return true;
        }
        if (GameTask.playedTime > 60) {
            sender.sendMessage(ChatColor.RED + "Game is already started!");
            return true;
        }
        ((Player) sender).openInventory(Utils.gameModifiers.getInventory());
        return true;
    }
}
