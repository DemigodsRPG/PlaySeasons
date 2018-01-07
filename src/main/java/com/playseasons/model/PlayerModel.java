package com.playseasons.model;

import com.demigodsrpg.util.datasection.DataSection;
import com.playseasons.impl.Model;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.*;

public class PlayerModel implements Model {

    // -- META DATA -- //

    String mojangId;
    String lastKnownName;
    boolean trusted;
    boolean expelled;

    // -- GREYLIST DATA -- //

    long timeInvited;
    String invitedFrom;
    List<String> invited;


    // -- CONSTRUCTORS -- //

    public PlayerModel(OfflinePlayer player, boolean console) {
        this(player, console ? "CONSOLE" : player.getUniqueId().toString());
        trusted = !console;
    }

    public PlayerModel(OfflinePlayer player, String invitedFrom) {
        mojangId = player.getUniqueId().toString();
        lastKnownName = player.getName();
        trusted = false;
        expelled = false;

        timeInvited = System.currentTimeMillis();
        this.invitedFrom = invitedFrom;
        invited = new ArrayList<>();
    }

    public PlayerModel(String mojangId, DataSection data) {
        this.mojangId = mojangId;
        lastKnownName = data.getString("lastKnownName");
        trusted = data.getBoolean("trusted", false);
        expelled = data.getBoolean("expelled", false);
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

    public boolean isExpelled() {
        return expelled;
    }

    public String getInvitedFrom() {
        return invitedFrom;
    }

    public List<String> getInvited() {
        return invited;
    }

    @Override
    public String getKey() {
        return mojangId;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("lastKnownName", lastKnownName);
        map.put("trusted", trusted);
        map.put("expelled", expelled);

        map.put("timeInvited", timeInvited);
        map.put("invitedFrom", invitedFrom);
        map.put("invited", invited);

        return map;
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(UUID.fromString(mojangId));
    }

    // -- MUTATORS -- //

    public void setLastKnownName(String name) {
        lastKnownName = name;
        getSeasons().getPlayerRegistry().register(this);
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
        getSeasons().getPlayerRegistry().register(this);
    }

    public void setExpelled(boolean expelled) {
        this.expelled = expelled;
        getSeasons().getPlayerRegistry().register(this);
    }

    public void setInvitedFrom(String invitedFrom) {
        this.invitedFrom = invitedFrom;
        this.timeInvited = System.currentTimeMillis();
        getSeasons().getPlayerRegistry().register(this);
    }
}
