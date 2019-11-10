package xyz.acrylicstyle.bedwars.utils;


import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import util.Collection;
import util.CollectionList;
import xyz.acrylicstyle.bedwars.BedWars;
import xyz.acrylicstyle.tomeito_core.providers.ConfigProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigUtils extends ConfigProvider {
    ConfigUtils(String path) throws IOException, InvalidConfigurationException {
        super(path);
    }

    public Location getGeneratorLocation(String team) {
        //Map<String, Object> section = ConfigProvider.getConfigSectionValue(this.config.get("teams." + team + ".generators"), true);
        double x = this.getDouble("teams." + team + ".generator.x");
        double y = this.getDouble("teams." + team + ".generator.y");
        double z = this.getDouble("teams." + team + ".generator.z");
        return new Location(BedWars.world, x, y, z);
    }

    public List<Location> getMiddleGenerators() {
        List<String> stringLocationList = this.getStringList("teams.middle.generators");
        return getLocations(stringLocationList);
    }

    public List<Location> getSemiMiddleGenerators() {
        List<String> stringLocationList = this.getStringList("teams.semiMiddle.generators");
        return getLocations(stringLocationList);
    }

    private List<Location> getLocations(List<String> stringLocationList) {
        List<Location> locationList = new ArrayList<>();
        stringLocationList.forEach(loc -> {
            String[] locations = loc.split(",");
            double x = Double.parseDouble(locations[0]);
            double y = Double.parseDouble(locations[1]);
            double z = Double.parseDouble(locations[2]);
            locationList.add(new Location(BedWars.world, x, y, z));
        });
        return locationList;
    }

    public Team getTeamFromLocation(Location location) {
        Map<String, Object> locations = ConfigProvider.getConfigSectionValue(this.get("beds.locationTeam"), true);
        String locationStr = location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
        Object obj = locations.get(locationStr);
        if (obj == null) return null;
        return Team.valueOf(obj.toString());
    }

    public Location getTeamSpawnPoint(Team team) {
        double x = this.getDouble("teams." + team.name().toLowerCase() + ".spawn.x");
        double y = this.getDouble("teams." + team.name().toLowerCase() + ".spawn.y");
        double z = this.getDouble("teams." + team.name().toLowerCase() + ".spawn.z");
        return new Location(BedWars.world, x, y, z);
    }

    public Collection<Team, Location> getSpawnPointsAndTeam() {
        Collection<Team, Location> spawnPoints = new Collection<>();
        for (Team team : Team.values()) {
            double x = this.getDouble("teams." + team.name().toLowerCase() + ".spawn.x");
            double y = this.getDouble("teams." + team.name().toLowerCase() + ".spawn.y");
            double z = this.getDouble("teams." + team.name().toLowerCase() + ".spawn.z");
            spawnPoints.add(team, new Location(BedWars.world, x, y, z));
        }
        return spawnPoints;
    }

    public CollectionList<Location> getSpawnPoints() {
        return getSpawnPointsAndTeam().valuesList();
    }

    public Team getClosestTeam(Location location) {
        Location closest = null;
        for (Location loc : Utils.getConfigUtils().getSpawnPoints()) {
            if (closest == null) {
                closest = loc;
            } else if (loc.distanceSquared(location) < closest.distanceSquared(location)) {
                closest = loc;
            }
        }
        assert closest != null; // impossible if all configuration is correct
        return Utils.getConfigUtils().getSpawnPointsAndTeam().values(new Location(BedWars.world, closest.getX(), closest.getY(), closest.getZ())).firstKey();
    }

    public Location getItemShopNPCLocation(Team team) {
        double x = this.getDouble("teams." + team.name().toLowerCase() + ".itemShop.x");
        double y = this.getDouble("teams." + team.name().toLowerCase() + ".itemShop.y");
        double z = this.getDouble("teams." + team.name().toLowerCase() + ".itemShop.z");
        return new Location(BedWars.world, x, y, z);
    }

    public Location getUpgradeNPCLocation(Team team) {
        double x = this.getDouble("teams." + team.name().toLowerCase() + ".upgrade.x");
        double y = this.getDouble("teams." + team.name().toLowerCase() + ".upgrade.y");
        double z = this.getDouble("teams." + team.name().toLowerCase() + ".upgrade.z");
        return new Location(BedWars.world, x, y, z);
    }

    public Location getTeamEnderDragonSpawnPoint(Team team) {
        Location location = this.getTeamSpawnPoint(team);
        location.setY(location.getY()+15);
        return location;
    }
}
