package com.playseasons.registry;

import com.demigodsrpg.util.datasection.DataSection;
import com.playseasons.impl.PlaySeasons;
import com.playseasons.model.PlayerModel;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PlayerRegistry extends AbstractRegistry<PlayerModel> {
    public PlayerRegistry(PlaySeasons backend) {
        super(backend, "players", true, 3);
    }

    @Override
    protected PlayerModel fromDataSection(String s, DataSection dataSection) {
        return new PlayerModel(s, dataSection);
    }


    public Optional<PlayerModel> fromPlayer(OfflinePlayer player) {
        return fromKey(player.getUniqueId().toString());
    }

    @Deprecated
    public Optional<PlayerModel> fromName(String name) {
        return getFromDb().values().stream().filter(model -> model.getLastKnownName().equalsIgnoreCase(name)).
                findAny();
    }

    public PlayerModel invite(OfflinePlayer player, Player inviteFrom) {
        return invite(player, inviteFrom.getUniqueId().toString());
    }

    public PlayerModel invite(OfflinePlayer player, String inviteFrom) {
        PlayerModel model = new PlayerModel(player, inviteFrom);
        Optional<PlayerModel> invite = fromKey(inviteFrom);
        invite.get().getInvited().add(model.getKey());
        if (invite.isPresent()) {
            register(model);
            register(invite.get());
        }
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

    public boolean isExpelled(OfflinePlayer player) {
        return fromPlayer(player).isPresent() && fromPlayer(player).get().isExpelled();
    }

    public boolean isVisitorOrExpelled(OfflinePlayer player) {
        return isVisitor(player) || isExpelled(player);
    }

    public boolean isTrusted(OfflinePlayer player) {
        Optional<PlayerModel> oModel = fromPlayer(player);
        return oModel.isPresent() && oModel.get().isTrusted();
    }
}
