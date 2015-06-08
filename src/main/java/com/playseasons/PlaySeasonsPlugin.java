package com.playseasons;

import com.demigodsrpg.chitchat.Chitchat;
import com.demigodsrpg.chitchat.tag.DefaultPlayerTag;
import com.demigodsrpg.chitchat.tag.PlayerTag;
import com.playseasons.chitchat.ServerIdTag;
import com.playseasons.chitchat.TrustedTag;
import com.playseasons.util.LibraryHandler;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class PlaySeasonsPlugin extends JavaPlugin {

    // -- PLUGIN INSTANCE & SAVE PATH -- //

    static PlaySeasonsPlugin PLUGIN;
    static String SAVE_PATH;

    // -- CHITCHAT -- //

    private DefaultPlayerTag MOD;
    private DefaultPlayerTag MOD_PLUS;
    private DefaultPlayerTag ADMIN;
    private PlayerTag SERVER_TAG;
    private TrustedTag TRUSTED_TAG;

    // -- LIBRARY HANDLER -- //

    private static LibraryHandler LIBRARIES;

    // -- BUKKIT ENABLE/DISABLE METHODS -- //

    @Override
    public void onEnable() {
        // The plugin instance
        PLUGIN = this;

        // Config
        getConfig().options().copyDefaults(true);
        saveConfig();

        // Chitchat
        MOD = new DefaultPlayerTag("Moderator", "extchat.mod", encloseTag(ChatColor.DARK_GREEN + "M"), 5);
        MOD_PLUS = new DefaultPlayerTag("Moderator+", "extchat.modplus", encloseTag(ChatColor.DARK_GREEN + "M" + ChatColor.DARK_AQUA + "+"), 5);
        ADMIN = new DefaultPlayerTag("Admin", "extchat.admin", encloseTag(ChatColor.GOLD + "A"), 5);
        SERVER_TAG = new ServerIdTag();
        TRUSTED_TAG = new TrustedTag();

        Chitchat.getChatFormat().addAll(new PlayerTag[]{MOD, MOD_PLUS, ADMIN, SERVER_TAG, TRUSTED_TAG});

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

    private static String encloseTag(String middle) {
        return ChatColor.DARK_GRAY + "[" + middle + ChatColor.DARK_GRAY + "]";
    }
}
