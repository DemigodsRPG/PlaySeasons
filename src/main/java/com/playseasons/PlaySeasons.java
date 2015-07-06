package com.playseasons;

import com.demigodsrpg.chitchat.Chitchat;
import com.demigodsrpg.chitchat.tag.DefaultPlayerTag;
import com.demigodsrpg.chitchat.tag.PlayerTag;
import com.playseasons.chitchat.ServerIdTag;
import com.playseasons.chitchat.TrustedTag;
import com.playseasons.chitchat.VisitingTag;
import com.playseasons.command.*;
import com.playseasons.dungeon.mob.DungeonMobs;
import com.playseasons.listener.LockedBlockListener;
import com.playseasons.listener.PlayerListener;
import com.playseasons.registry.LockedBlockRegistry;
import com.playseasons.registry.PlayerRegistry;
import com.playseasons.registry.ServerDataRegistry;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;

import java.util.Arrays;

public class PlaySeasons {

    // -- INSTANCE -- //

    private static PlaySeasons INST;

    // -- REGISTRIES -- //

    private static final ServerDataRegistry SERVER_DATA_REGISTRY = new ServerDataRegistry();
    private static final PlayerRegistry PLAYER_REGISTRY = new PlayerRegistry();
    private static final LockedBlockRegistry LOCKED_BLOCK_REGISTRY = new LockedBlockRegistry();

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
        SERVER_DATA_REGISTRY.registerFromDatabase();
        PLAYER_REGISTRY.registerFromDatabase();
        LOCKED_BLOCK_REGISTRY.registerFromDatabase();

        // Register listeners
        PluginManager manager = getPlugin().getServer().getPluginManager();
        manager.registerEvents(new PlayerListener(), getPlugin());
        manager.registerEvents(new LockedBlockListener(), getPlugin());

        // Register dungeon mobs
        Arrays.asList(DungeonMobs.values()).forEach(mob -> {
            manager.registerEvents(mob, getPlugin());
            mob.registerRunnables();
        });

        // Register commands
        getPlugin().getCommand("invite").setExecutor(new InviteCommand());
        getPlugin().getCommand("trust").setExecutor(new TrustCommand());
        getPlugin().getCommand("expel").setExecutor(new ExpelCommand());
        getPlugin().getCommand("spawn").setExecutor(new SpawnCommand());
        getPlugin().getCommand("visiting").setExecutor(new VisitingCommand());
        getPlugin().getCommand("psdebug").setExecutor(new DebugCommand());

        // Register tasks
        getPlugin().getServer().getScheduler().scheduleAsyncRepeatingTask(getPlugin(),
                SERVER_DATA_REGISTRY::clearExpired, 20, 20);
    }

    // -- STATIC METHODS -- //

    public static PlaySeasons getInst() {
        return INST;
    }

    public static PlaySeasonsPlugin getPlugin() {
        return PlaySeasonsPlugin.PLUGIN;
    }

    public static ServerDataRegistry getServerDataRegistry() {
        return SERVER_DATA_REGISTRY;
    }

    public static PlayerRegistry getPlayerRegistry() {
        return PLAYER_REGISTRY;
    }

    public static LockedBlockRegistry getLockedBlockRegistry() {
        return LOCKED_BLOCK_REGISTRY;
    }

    // -- PRIVATE HELPER METHODS -- //

    private String encloseTag(String middle) {
        return ChatColor.DARK_GRAY + "[" + middle + ChatColor.DARK_GRAY + "]";
    }
}
