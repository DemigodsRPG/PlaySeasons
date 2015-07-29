package com.playseasons.registry;

import com.demigodsrpg.util.datasection.DataSection;
import com.playseasons.model.PlayerModel;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PlayerRegistry extends AbstractSeasonsRegistry<PlayerModel> {
    @Override
    protected PlayerModel valueFromData(String s, DataSection dataSection) {
        return new PlayerModel(s, dataSection);
    }

    @Override
    protected String getName() {
        return "players";
    }

    public Optional<PlayerModel> fromPlayer(OfflinePlayer player) {
        return Optional.ofNullable(fromId(player.getUniqueId().toString()));
    }

    @Deprecated
    public Optional<PlayerModel> fromName(String name) {
        return getRegistered().stream().filter(model -> model.getLastKnownName().equalsIgnoreCase(name)).findAny();
    }

    public PlayerModel invite(OfflinePlayer player, Player inviteFrom) {
        return invite(player, inviteFrom.getUniqueId().toString());
    }

    public PlayerModel invite(OfflinePlayer player, String inviteFrom) {
        PlayerModel model = new PlayerModel(player, inviteFrom);
        PlayerModel invite = fromId(inviteFrom);
        invite.getInvited().add(model.getPersistentId());
        register(model);
        register(invite);
        return model;
    }

    public PlayerModel inviteConsole(OfflinePlayer player) {
        PlayerModel model = new PlayerModel(player, true);
        register(model);
        return model;
    }

    public PlayerModel inviteSelf(Player player) {
        PlayerModel model = new PlayerModel(player, false);
        register(model);
        return model;
    }

    public boolean isVisitor(OfflinePlayer player) {
        return !fromPlayer(player).isPresent();
    }

    public boolean isTrusted(OfflinePlayer player) {
        Optional<PlayerModel> oModel = fromPlayer(player);
        return oModel.isPresent() && oModel.get().isTrusted();
    }
}
