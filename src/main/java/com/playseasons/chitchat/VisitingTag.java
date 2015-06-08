package com.playseasons.chitchat;

import com.demigodsrpg.chitchat.tag.PlayerTag;
import com.playseasons.PlaySeasons;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class VisitingTag extends PlayerTag {
    @Override
    public String getName() {
        return "Visiting Tag";
    }

    @Override
    public String getFor(Player player) {
        if (PlaySeasons.getPlayerRegistry().isVisitor(player)) {
            return ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "V" + ChatColor.DARK_GRAY + "]";
        }
        return "";
    }

    @Override
    public int getPriority() {
        return 2;
    }
}
