package xyz.acrylicstyle.bedwars.utils;

import org.bukkit.ChatColor;

public enum Team {
    RED(ChatColor.RED + "Red"),
    BLUE(ChatColor.BLUE + "Blue"),
    YELLOW(ChatColor.YELLOW + "Yellow"),
    GREEN(ChatColor.GREEN + "Green"),
    WHITE(ChatColor.WHITE + "White"),
    BLACK(ChatColor.BLACK + "Black"),
    PINK(ChatColor.LIGHT_PURPLE + "Pink"),
    AQUA(ChatColor.AQUA + "Aqua");

    private String teamName;

    private Team(String teamName){
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return this.teamName;
    }
}
