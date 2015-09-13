package com.playseasons.dungeon.structure;

import com.censoredsoftware.library.schematic.Point;
import com.censoredsoftware.library.schematic.Schematic;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;
import java.util.stream.Collectors;

public abstract class DungeonStructure extends Schematic {
    public DungeonStructure(String name, String designer, int groundRadius) {
        super(name, designer, groundRadius);
    }

    public abstract Location getImportantSpot();

    public void generate(Location reference) {
        generate(new Point(reference.getBlockX(), reference.getBlockY(), reference.getBlockZ(),
                new DungeonWorld(reference.getWorld())));
    }

    public List<Location> getLocations(Location reference) {
        return getLocations(new Point(reference.getBlockX(), reference.getBlockY(), reference.getBlockZ(),
                new DungeonWorld(reference.getWorld()))).stream().
                map(point -> new Location(Bukkit.getWorld(point.getWorld().getName()), point.getX(), point.getY(),
                        point.getZ())).collect(Collectors.toList());
    }
}
