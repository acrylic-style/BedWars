package xyz.acrylicstyle.bedwars.utils;

import org.bukkit.ChatColor;

public enum Team {
    RED(ChatColor.RED + "R" + ChatColor.WHITE + " Red"),
    BLUE(ChatColor.BLUE + "B" + ChatColor.WHITE + " Blue"),
    YELLOW(ChatColor.YELLOW + "Y" + ChatColor.WHITE + " Yellow"),
    GREEN(ChatColor.GREEN + "G" + ChatColor.WHITE + " Green"),
    WHITE(ChatColor.WHITE + "W" + ChatColor.WHITE + " White"),
    BLACK(ChatColor.BLACK + "B" + ChatColor.WHITE + " Black"),
    PINK(ChatColor.LIGHT_PURPLE + "P" + ChatColor.WHITE + " Purple"),
    AQUA(ChatColor.AQUA + "A" + ChatColor.WHITE + " Aqua");

    private String teamName;

    Team(String teamName){
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return this.teamName;
    }
}
