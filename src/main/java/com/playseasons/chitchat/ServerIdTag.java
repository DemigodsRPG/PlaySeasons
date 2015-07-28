package com.playseasons.chitchat;

import com.demigodsrpg.chitchat.tag.PlayerTag;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class ServerIdTag extends PlayerTag {
    @Override
    public String getName() {
        return "Server Id Tag";
    }

    @Override
    public TextComponent getComponentFor(Player player) {
        TextComponent server = new TextComponent("[");
        server.setColor(ChatColor.DARK_GRAY);
        TextComponent middle = new TextComponent("S");
        middle.setColor(ChatColor.DARK_GREEN);
        server.addExtra(middle);
        server.addExtra("]");
        server.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Seasons").color(ChatColor.DARK_GREEN).create()));
        server.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/seasons"));
        return server;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
