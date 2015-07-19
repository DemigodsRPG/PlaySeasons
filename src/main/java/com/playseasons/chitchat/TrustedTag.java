package com.playseasons.chitchat;

import com.demigodsrpg.chitchat.tag.ChatScope;
import com.demigodsrpg.chitchat.tag.PlayerTag;
import com.playseasons.PlaySeasons;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class TrustedTag extends PlayerTag {
    @Override
    public String getName() {
        return "Trusted Tag";
    }

    @Override
    public String getFor(Player player) {
        if (PlaySeasons.getPlayerRegistry().isTrusted(player)) {
            return ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "T" + ChatColor.DARK_GRAY + "]";
        }
        return "";
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
