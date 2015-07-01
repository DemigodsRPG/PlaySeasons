package com.playseasons.dungeon;

import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;

public interface DungeonMob extends Listener {
    String getName();

    EntityType getType();

    double getMaxHealth();

    boolean isBoss();

    int getStrength();

    default void registerRunnables() {
    }
}
