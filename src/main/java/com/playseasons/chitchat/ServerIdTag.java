package com.playseasons.chitchat;

import com.demigodsrpg.chitchat.tag.PlayerTag;
import com.playseasons.Setting;
import org.bukkit.entity.Player;

public class ServerIdTag extends PlayerTag {
    @Override
    public String getName() {
        return "Server Id Tag";
    }

    @Override
    public String getFor(Player player) {
        return Setting.SERVER_TAG;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
