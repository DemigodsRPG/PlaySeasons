package com.playseasons.dungeon.mob;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;

public interface DungeonMob extends Listener {
    String getName();

    EntityType getType();

    double getMaxHealth();

    boolean isBoss();

    int getStrength();

    default LivingEntity spawnRaw(Location location) {
        LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, getType());
        entity.setCustomName(getName());
        entity.setCustomNameVisible(true);
        entity.setMaxHealth(getMaxHealth());
        entity.setHealth(getMaxHealth());
        return entity;
    }

    default void registerRunnables() {
    }
}
