package xyz.acrylicstyle.bedwars.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.acrylicstyle.bedwars.tasks.LobbyTask;

public class ForceStart implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        LobbyTask.setCountdown(10);
        sender.sendMessage(ChatColor.GREEN + "Game is starting in 10 seconds!");
        return true;
    }
}
