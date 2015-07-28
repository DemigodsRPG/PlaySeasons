package com.playseasons;

import org.bukkit.configuration.Configuration;

public class Setting {
    public static final String SAVE_PATH = PlaySeasonsPlugin.SAVE_PATH;
    public static final boolean USE_PSQL = getConfig().getBoolean("save.psql.use", true);
    public static final String PSQL_CONNECTION = getConfig().getString("save.psql.connection", "postgresql://localhost:5432/seasons?user=seasons&password=seasons");
    public static final boolean SAVE_PRETTY = getConfig().getBoolean("save.pretty", false);
    public static final String VISITOR_REGION = getConfig().getString("region.visiting", "visiting");
    public static final String SPAWN_REGION = getConfig().getString("region.spawn", "spawn");

    public static Configuration getConfig() {
        return PlaySeasonsPlugin.PLUGIN.getConfig();
    }
}
