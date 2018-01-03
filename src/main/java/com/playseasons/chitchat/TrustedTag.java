package com.playseasons.chitchat;

import com.demigodsrpg.chitchat.tag.ChatScope;
import com.demigodsrpg.chitchat.tag.PlayerTag;
import com.playseasons.impl.PlaySeasons;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;

public class TrustedTag extends PlayerTag {

    final PlaySeasons plugin;

    public TrustedTag(PlaySeasons plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "Trusted Tag";
    }

    @Override
    public TextComponent getComponentFor(Player player) {
        if (plugin.getPlayerRegistry().isTrusted(player)) {
            TextComponent trusted = new TextComponent("[");
            trusted.setColor(ChatColor.DARK_GRAY);
            TextComponent middle = new TextComponent("T");
            middle.setColor(ChatColor.DARK_AQUA);
            trusted.addExtra(middle);
            trusted.addExtra("]");
            trusted.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("Trusted").color(ChatColor.DARK_AQUA).create()));
            trusted.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/seasonshelp TRUSTED"));
            return trusted;
        }
        return null;
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public ChatScope getScope() {
        return ChatScope.LOCAL;
    }
}
