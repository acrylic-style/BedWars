package xyz.acrylicstyle.bedwars.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.acrylicstyle.bedwars.utils.Utils;

public class EndGame implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Utils.endGame();
        return true;
    }
}
