package xyz.acrylicstyle.bedwars.utils;

public class Trap {
    private Traps trap;
    private Team team;

    public Trap(Traps trap, Team team) {
        this.trap = trap;
        this.team = team;
    }

    public Traps getTrap() { return this.trap; }
    public Team getTeam() { return this.team; }
}
