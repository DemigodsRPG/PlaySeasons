package com.playseasons.impl;

import com.demigodsrpg.chitchat.Chitchat;
import com.demigodsrpg.chitchat.tag.DefaultPlayerTag;
import com.demigodsrpg.chitchat.tag.PlayerTag;
import com.playseasons.Depends;
import com.playseasons.chitchat.*;
import com.playseasons.command.*;
import com.playseasons.dungeon.mob.DungeonMobs;
import com.playseasons.listener.LockedBlockListener;
import com.playseasons.listener.PlayerListener;
import com.playseasons.registry.*;
import com.playseasons.util.LibraryHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class PlaySeasons extends JavaPlugin {
    // -- REGISTRIES -- //

    private ServerDataRegistry SERVER_DATA_REGISTRY;
    private PlayerRegistry PLAYER_REGISTRY;
    private LockedBlockRegistry LOCKED_BLOCK_REGISTRY;

    // -- STATIC INSTANCE -- //

    static PlaySeasons INST;

    // -- BUKKIT ENABLE/DISABLE METHODS -- //

    @Override
    public void onEnable() {
        INST = this;

        // Config
        getConfig().options().copyDefaults(true);
        saveConfig();

        // Get and load the libraries
        LibraryHandler lib = new LibraryHandler(this);

        // Demigods RPG
        try {
            Class.forName("com.demigodsrpg.util.LocationUtil");
            getLogger().info("DG utility modules are bundled in jar.");
        } catch (Exception oops) {
            getLogger().info("DG utility modules are not bundled in jar.");
            if (!lib.addLocalLibrary(Depends.DG, Depends.DG_VER)) {
                getLogger().info("DG utility modules are not found. Disabling plugin.");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }

        // PostgreSQL & Iciql Libs
        if (getConfig().getBoolean("psql.use", false)) {
            lib.addMavenLibrary(Depends.GITBLIT_REPO, Depends.COM_ICIQL, Depends.ICIQL, Depends.ICIQL_VER);
            lib.addMavenLibrary(LibraryHandler.MAVEN_CENTRAL, Depends.ORG_PSQL, Depends.PSQL, Depends.PSQL_VER);
        }

        // Chitchat integration
        DefaultPlayerTag modTag = createTag(ChatColor.DARK_GREEN + "Moderator", "seasons.chat.mod",
                new ChatColor[]{ChatColor.DARK_GREEN},
                new String[]{"M"});
        DefaultPlayerTag modPlusTag = createTag(ChatColor.DARK_GREEN + "Moderator" + ChatColor.DARK_AQUA + "+",
                "seasons.chat.modplus", new ChatColor[]{ChatColor.DARK_GREEN, ChatColor.DARK_AQUA},
                new String[]{"M", "+"});
        DefaultPlayerTag adminTag = createTag(ChatColor.DARK_RED + "Admin", "seasons.chat.admin",
                new ChatColor[]{ChatColor.DARK_RED},
                new String[]{"A"});
        PlayerTag serverTag = new ServerIdTag();
        TrustedTag trustedTag = new TrustedTag(this);
        VisitingTag visitingTag = new VisitingTag(this);

        Chitchat.getChatFormat().addAll(new PlayerTag[]{
                modTag, modPlusTag, adminTag, serverTag, trustedTag, visitingTag
        });

        // Handle registries
        SERVER_DATA_REGISTRY = new ServerDataRegistry();
        PLAYER_REGISTRY = new PlayerRegistry();
        LOCKED_BLOCK_REGISTRY = new LockedBlockRegistry();

        // Register listeners
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerListener(this), this);
        manager.registerEvents(new LockedBlockListener(this), this);

        // Register dungeon mobs
        Arrays.asList(DungeonMobs.values()).forEach(mob -> {
            manager.registerEvents(mob, this);
            mob.registerRunnables(this);
        });

        // Register commands
        getCommand("seasons").setExecutor(new SeasonsCommand());
        getCommand("seasonshelp").setExecutor(new SeasonsHelpCommand());
        getCommand("invite").setExecutor(new InviteCommand(this));
        getCommand("trust").setExecutor(new TrustCommand(this));
        getCommand("expel").setExecutor(new ExpelCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("visiting").setExecutor(new VisitingCommand());
        getCommand("psdebug").setExecutor(new DebugCommand());

        // Register tasks
        getServer().getScheduler().scheduleAsyncRepeatingTask(this, SERVER_DATA_REGISTRY::clearExpired, 20, 20);
    }

    @Override
    public void onDisable() {
        // Unregister and cancel stuff
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
    }

    // -- GETTERS -- //

    public ServerDataRegistry getServerDataRegistry() {
        return SERVER_DATA_REGISTRY;
    }

    public PlayerRegistry getPlayerRegistry() {
        return PLAYER_REGISTRY;
    }

    public LockedBlockRegistry getLockedBlockRegistry() {
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
