package com.playseasons.chitchat;

import com.demigodsrpg.chitchat.tag.ChatScope;
import com.demigodsrpg.chitchat.tag.PlayerTag;
import com.playseasons.PlaySeasons;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class TrustedTag extends PlayerTag {
    @Override
    public String getName() {
        return "Trusted Tag";
    }

    @Override
    public TextComponent getComponentFor(Player player) {
        if (PlaySeasons.getPlayerRegistry().isTrusted(player)) {
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
