package com.playseasons.impl;

import org.bukkit.configuration.Configuration;

public class Setting {
    public static final boolean USING_PSQL = getConfig().getBoolean("save.psql.use", true);
    public static final String PSQL_CONNECTION = getConfig().getString("save.psql.connection",
            "postgresql://localhost:5432/seasons?user=seasons&password=seasons");
    public static final String VISITOR_REGION = getConfig().getString("region.visiting", "visiting");
    public static final String SPAWN_REGION = getConfig().getString("region.spawn", "spawn");

    public static Configuration getConfig() {
        return PlaySeasons.INST.getConfig();
    }
}
