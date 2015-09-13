package com.playseasons.util;

import com.playseasons.impl.Setting;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@SuppressWarnings("ConstantConditions")
public class RegionUtil {
    private RegionUtil() {
    }

    public static boolean spawnContains(Location location) throws NullPointerException {
        ProtectedRegion spawn = WorldGuardPlugin.inst().getRegionManager(Bukkit.getWorlds().get(0)).
                getRegion(Setting.SPAWN_REGION);
        return spawn != null && spawn.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static boolean visitingContains(Location location) throws NullPointerException {
        ProtectedRegion visiting = WorldGuardPlugin.inst().getRegionManager(Bukkit.getWorlds().get(0)).
                getRegion(Setting.VISITOR_REGION);
        return visiting != null && visiting.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static Location spawnLocation() throws NullPointerException {
        ProtectedRegion spawn = WorldGuardPlugin.inst().getRegionManager(Bukkit.getWorlds().get(0)).
                getRegion(Setting.SPAWN_REGION);
        Vector sV = spawn.getFlag(DefaultFlag.SPAWN_LOC).getPosition();
        float sY = spawn.getFlag(DefaultFlag.SPAWN_LOC).getYaw();
        float sP = spawn.getFlag(DefaultFlag.SPAWN_LOC).getPitch();
        return new Location(Bukkit.getWorlds().get(0), sV.getX(), sV.getY(), sV.getZ(), sY, sP);
    }

    public static Location visitingLocation() throws NullPointerException {
        ProtectedRegion visiting = WorldGuardPlugin.inst().getRegionManager(Bukkit.getWorlds().get(0)).
                getRegion(Setting.VISITOR_REGION);
        Vector sV = visiting.getFlag(DefaultFlag.SPAWN_LOC).getPosition();
        float sY = visiting.getFlag(DefaultFlag.SPAWN_LOC).getYaw();
        float sP = visiting.getFlag(DefaultFlag.SPAWN_LOC).getPitch();
        return new Location(Bukkit.getWorlds().get(0), sV.getX(), sV.getY(), sV.getZ(), sY, sP);
    }
}
