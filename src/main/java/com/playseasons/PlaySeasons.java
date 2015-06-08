package com.playseasons;

import com.demigodsrpg.chitchat.Chitchat;
import com.demigodsrpg.chitchat.tag.DefaultPlayerTag;
import com.demigodsrpg.chitchat.tag.PlayerTag;
import com.playseasons.chitchat.ServerIdTag;
import com.playseasons.chitchat.TrustedTag;
import com.playseasons.chitchat.VisitingTag;
import com.playseasons.command.InviteCommand;
import com.playseasons.command.SpawnCommand;
import com.playseasons.command.VisitingCommand;
import com.playseasons.listener.PlayerListener;
import com.playseasons.registry.PlayerRegistry;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;

public class PlaySeasons {

    // -- INSTANCE -- //

    private static PlaySeasons INST;

    // -- REGISTRIES -- //

    private static final PlayerRegistry PLAYER_REGISTRY = new PlayerRegistry();

    // -- CHITCHAT INTEGRATION -- //

    private DefaultPlayerTag MOD;
    private DefaultPlayerTag MOD_PLUS;
    private DefaultPlayerTag ADMIN;
    private PlayerTag SERVER_TAG;
    private TrustedTag TRUSTED_TAG;
    private VisitingTag VISITING_TAG;

    // -- CONSTRUCTOR -- //

    public PlaySeasons() {
        INST = this;

        // Chitchat integration
        MOD = new DefaultPlayerTag("Moderator", "seasons.chat.mod", encloseTag(ChatColor.DARK_GREEN + "M"), 5);
        MOD_PLUS = new DefaultPlayerTag("Moderator+", "seasons.chat.modplus", encloseTag(ChatColor.DARK_GREEN + "M" + ChatColor.DARK_AQUA + "+"), 5);
        ADMIN = new DefaultPlayerTag("Admin", "seasons.chat.admin", encloseTag(ChatColor.GOLD + "A"), 5);
        SERVER_TAG = new ServerIdTag();
        TRUSTED_TAG = new TrustedTag();
        VISITING_TAG = new VisitingTag();

        Chitchat.getChatFormat().addAll(new PlayerTag[]{MOD, MOD_PLUS, ADMIN, SERVER_TAG, TRUSTED_TAG, VISITING_TAG});

        // Handle registries
        PLAYER_REGISTRY.registerFromDatabase();

        // Register listeners
        PluginManager manager = getPlugin().getServer().getPluginManager();
        manager.registerEvents(new PlayerListener(), getPlugin());

        // Register commands
        getPlugin().getCommand("invite").setExecutor(new InviteCommand());
        getPlugin().getCommand("spawn").setExecutor(new SpawnCommand());
        getPlugin().getCommand("visiting").setExecutor(new VisitingCommand());
    }

    // -- STATIC METHODS -- //

    public static PlaySeasons getInst() {
        return INST;
    }

    public static PlaySeasonsPlugin getPlugin() {
        return PlaySeasonsPlugin.PLUGIN;
    }

    public static PlayerRegistry getPlayerRegistry() {
        return PLAYER_REGISTRY;
    }

    // -- PRIVATE HELPER METHODS -- //

    private String encloseTag(String middle) {
        return ChatColor.DARK_GRAY + "[" + middle + ChatColor.DARK_GRAY + "]";
    }
}
