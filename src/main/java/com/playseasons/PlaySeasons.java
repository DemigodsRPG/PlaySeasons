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
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
        MOD = createTag(ChatColor.DARK_GREEN + "Moderator", "seasons.chat.mod",
                new ChatColor[]{ChatColor.DARK_GREEN},
                new String[]{"M"});
        MOD_PLUS = createTag(ChatColor.DARK_GREEN + "Moderator" + ChatColor.DARK_AQUA + "+", "seasons.chat.modplus",
                new ChatColor[]{ChatColor.DARK_GREEN, ChatColor.DARK_AQUA},
                new String[]{"M", "+"});
        ADMIN = createTag(ChatColor.DARK_RED + "Admin", "seasons.chat.admin",
                new ChatColor[]{ChatColor.DARK_RED},
                new String[]{"A"});
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
        getPlugin().getCommand("seasons").setExecutor(new SeasonsCommand());
        getPlugin().getCommand("seasonshelp").setExecutor(new SeasonsHelpCommand());
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

    private DefaultPlayerTag createTag(String nameText, String permission, ChatColor[] color, String middleText[]) {
        TextComponent tag = new TextComponent("[");
        tag.setColor(ChatColor.DARK_GRAY);
        for (int i = 0; i < color.length; i++) {
            TextComponent middle = new TextComponent(middleText[i]);
            middle.setColor(color[i]);
            tag.addExtra(middle);
        }
        tag.addExtra("]");
        tag.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/seasonshelp " +
                ChatColor.stripColor(nameText).toUpperCase()));
        tag.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(nameText)));
        return new DefaultPlayerTag(ChatColor.stripColor(nameText), permission, tag, 5);
    }
}
