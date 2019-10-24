package xyz.acrylicstyle.bedwars.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.acrylicstyle.bedwars.BedWars;

public class SetSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must run this command from in-game.");
            return true;
        }
        Player player = (Player) sender;
        Location location = player.getLocation();
        BedWars.map.set("world", location.getWorld().getName());
        BedWars.map.set("spawn.x", location.getX());
        BedWars.map.set("spawn.y", location.getY());
        BedWars.map.set("spawn.z", location.getZ());
        sender.sendMessage(ChatColor.GREEN + "Spawn point has been set to:");
        sender.sendMessage(ChatColor.YELLOW + "World: " + ChatColor.RED + location.getWorld().getName());
        sender.sendMessage(ChatColor.YELLOW + "X pos: " + ChatColor.RED + location.getX());
        sender.sendMessage(ChatColor.YELLOW + "Y pos: " + ChatColor.RED + location.getY());
        sender.sendMessage(ChatColor.YELLOW + "Z pos: " + ChatColor.RED + location.getZ());
        return true;
    }
}
