package xyz.acrylicstyle.bedwars.utils;


import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
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
}
