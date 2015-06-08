package com.playseasons.chitchat;

import com.demigodsrpg.chitchat.tag.PlayerTag;
import org.bukkit.entity.Player;

public class TrustedTag extends PlayerTag {
    @Override
    public String getName() {
        return "Trusted Tag";
    }

    @Override
    public String getFor(Player player) {
        return ""; // TODO
    }

    @Override
    public int getPriority() {
        return 2;
    }
}
