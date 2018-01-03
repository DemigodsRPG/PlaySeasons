package com.playseasons.chitchat;

import com.demigodsrpg.chitchat.tag.ChatScope;
import com.demigodsrpg.chitchat.tag.PlayerTag;
import com.playseasons.impl.PlaySeasons;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;

public class VisitingTag extends PlayerTag {

    final PlaySeasons plugin;

    public VisitingTag(PlaySeasons plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "Visiting Tag";
    }

    @Override
    public TextComponent getComponentFor(Player player) {
        if (plugin.getPlayerRegistry().isVisitorOrExpelled(player)) {
            TextComponent trusted = new TextComponent("[");
            trusted.setColor(ChatColor.DARK_GRAY);
            TextComponent middle = new TextComponent("V");
            middle.setColor(ChatColor.GREEN);
            trusted.addExtra(middle);
            trusted.addExtra("]");
            trusted.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("Visiting").color(ChatColor.GREEN).create()));
            trusted.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/seasonshelp VISITING"));
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
