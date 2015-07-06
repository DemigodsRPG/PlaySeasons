package com.playseasons.model;

import com.demigodsrpg.util.LocationUtil;
import com.demigodsrpg.util.datasection.AbstractPersistentModel;
import com.demigodsrpg.util.datasection.DataSection;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class LockedBlockModel extends AbstractPersistentModel<String> {

    // -- DATA -- //

    String location;
    String owner;
    boolean locked;

    // -- CONSTRUCTORS -- //

    public LockedBlockModel(Location location, String owner) {
        this.location = LocationUtil.stringFromLocation(location);
        this.owner = owner;
        locked = true;
    }

    public LockedBlockModel(String location, DataSection data) {
        this.location = location;
        owner = data.getString("owner");
        locked = data.getBoolean("locked");
    }

    // -- GETTERS -- //

    public Location getLocation() {
        return LocationUtil.locationFromString(location);
    }

    public String getOwner() {
        return owner;
    }

    public boolean isLocked() {
        return locked;
    }

    @Override
    public String getPersistentId() {
        return location;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("owner", owner);
        map.put("locked", locked);
        return map;
    }

    // -- MUTATORS -- //

    public boolean setLocked(boolean locked) {
        this.locked = locked;
        return locked;
    }
}
