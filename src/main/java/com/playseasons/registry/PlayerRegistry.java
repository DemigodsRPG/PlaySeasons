package com.playseasons.registry;

import com.demigodsrpg.util.datasection.DataSection;
import com.playseasons.model.PlayerModel;
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

    public Optional<PlayerModel> fromPlayer(Player player) {
        return Optional.ofNullable(fromId(player.getUniqueId().toString()));
    }

    public PlayerModel invite(Player player, Player inviteFrom) {
        PlayerModel model = new PlayerModel(player, inviteFrom.getUniqueId().toString());
        PlayerModel invite = fromId(inviteFrom.getUniqueId().toString());
        invite.getInvited().add(model.getPersistentId());
        register(model);
        register(invite);
        return model;
    }

    public PlayerModel inviteConsole(Player player) {
        PlayerModel model = new PlayerModel(player, true);
        register(model);
        return model;
    }

    public PlayerModel inviteSelf(Player player) {
        PlayerModel model = new PlayerModel(player, false);
        register(model);
        return model;
    }

    public boolean isVisitor(Player player) {
        return !fromPlayer(player).isPresent();
    }

    public boolean isTrusted(Player player) {
        Optional<PlayerModel> oModel = fromPlayer(player);
        return oModel.isPresent() && oModel.get().isTrusted();
    }
}
