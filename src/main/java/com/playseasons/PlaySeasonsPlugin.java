package com.playseasons;

import com.playseasons.util.LibraryHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class PlaySeasonsPlugin extends JavaPlugin {

    // -- LIBRARY HANDLER -- //

    private static LibraryHandler LIBRARIES;

    // -- BUKKIT ENABLE/DISABLE METHODS -- //

    @Override
    public void onEnable() {
        // Config
        getConfig().options().copyDefaults(true);
        saveConfig();

        // Get and load the libraries
        LIBRARIES = new LibraryHandler(this);

        // Censored Libs
        try {
            Class.forName("com.censoredsoftware.library.schematic.Selection");
            getLogger().info("CensoredLib is bundled.");
        } catch (Exception oops) {
            getLogger().info("CensoredLib is not bundled.");
            LIBRARIES.addMavenLibrary(Depends.DG_MG_REPO, Depends.COM_CS, Depends.CS_SCHEMATIC, Depends.CS_VER);
            LIBRARIES.addMavenLibrary(Depends.DG_MG_REPO, Depends.COM_CS, Depends.CS_UTIL, Depends.CS_VER);
            LIBRARIES.addMavenLibrary(Depends.DG_MG_REPO, Depends.COM_CS, Depends.CS_BUKKIT_UTIL, Depends.CS_VER);
        }

        // Demigods RPG Libs
        try {
            Class.forName("com.demigodsrpg.data.DGData");
            getLogger().info("DG utility modules are bundled.");
        } catch (Exception oops) {
            getLogger().info("DG utility modules are not bundled.");
            LIBRARIES.addMavenLibrary(Depends.DG_MG_REPO, Depends.COM_DG, Depends.DG_UTIL, Depends.DG_UTIL_VER);
            LIBRARIES.addMavenLibrary(Depends.DG_MG_REPO, Depends.COM_DG, Depends.FAMILIES, Depends.FAMILES_VER);
        }

        // PostgreSQL & Iciql Libs
        if (getConfig().getBoolean("psql.use", false)) {
            LIBRARIES.addMavenLibrary(Depends.GITBLIT_REPO, Depends.COM_ICIQL, Depends.ICIQL, Depends.ICIQL_VER);
            LIBRARIES.addMavenLibrary(LibraryHandler.MAVEN_CENTRAL, Depends.ORG_PSQL, Depends.PSQL, Depends.PSQL_VER);
        }
    }

    @Override
    public void onDisable() {
        // TODO
    }
}
