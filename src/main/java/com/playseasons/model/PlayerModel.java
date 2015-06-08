package com.playseasons.model;

import com.demigodsrpg.util.datasection.AbstractPersistentModel;
import com.demigodsrpg.util.datasection.DataSection;
import com.playseasons.PlaySeasons;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerModel extends AbstractPersistentModel<String> {

    // -- META DATA -- //

    String mojangId;
    String lastKnownName;
    boolean trusted;

    // -- GREYLIST DATA -- //

    long timeInvited;
    String invitedFrom;
    List<String> invited;

    // -- CONSTRUCTORS -- //

    public PlayerModel(Player player, boolean console) {
        this(player, console ? "CONSOLE" : player.getUniqueId().toString());
        trusted = !console;
    }

    public PlayerModel(Player player, String invitedFrom) {
        mojangId = player.getUniqueId().toString();
        lastKnownName = player.getName();
        trusted = false;

        timeInvited = System.currentTimeMillis();
        this.invitedFrom = invitedFrom;
        invited = new ArrayList<>();
    }

    public PlayerModel(String mojangId, DataSection data) {
        this.mojangId = mojangId;
        lastKnownName = data.getString("lastKnownName");
        trusted = data.getBoolean("trusted");
        timeInvited = data.getLong("timeInvited", System.currentTimeMillis());
        invitedFrom = data.getString("invitedFrom");
        invited = data.getStringList("invited");
    }

    // -- GETTERS -- //

    public String getLastKnownName() {
        return lastKnownName;
    }

    public long getTimeInvited() {
        return timeInvited;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public String getInvitedFrom() {
        return invitedFrom;
    }

    public List<String> getInvited() {
        return invited;
    }

    @Override
    public String getPersistentId() {
        return mojangId;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("lastKnownName", lastKnownName);
        map.put("trusted", trusted);

        map.put("timeInvited", timeInvited);
        map.put("invitedFrom", invitedFrom);
        map.put("invited", invited);

        return map;
    }

    // -- MUTATORS -- //

    public void setLastKnownName(String name) {
        lastKnownName = name;
        PlaySeasons.getPlayerRegistry().register(this);
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
        PlaySeasons.getPlayerRegistry().register(this);
    }
}
