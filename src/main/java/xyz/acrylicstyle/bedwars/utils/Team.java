package xyz.acrylicstyle.bedwars.utils;

import org.bukkit.ChatColor;

public enum Team {
    RED(ChatColor.RED + "R" + ChatColor.WHITE + " Red", ChatColor.RED),
    BLUE(ChatColor.BLUE + "B" + ChatColor.WHITE + " Blue", ChatColor.BLUE),
    YELLOW(ChatColor.YELLOW + "Y" + ChatColor.WHITE + " Yellow", ChatColor.YELLOW),
    GREEN(ChatColor.GREEN + "G" + ChatColor.WHITE + " Green", ChatColor.GREEN),
    WHITE(ChatColor.WHITE + "W" + ChatColor.WHITE + " White", ChatColor.WHITE),
    BLACK(ChatColor.BLACK + "B" + ChatColor.WHITE + " Black", ChatColor.BLACK),
    PINK(ChatColor.LIGHT_PURPLE + "P" + ChatColor.WHITE + " Purple", ChatColor.LIGHT_PURPLE),
    AQUA(ChatColor.AQUA + "A" + ChatColor.WHITE + " Aqua", ChatColor.AQUA);

    private String teamName;
    public ChatColor color;

    Team(String teamName, ChatColor color){
        this.teamName = teamName;
        this.color = color;
    }

    @Override
    public String toString() {
        return this.teamName;
    }
}
