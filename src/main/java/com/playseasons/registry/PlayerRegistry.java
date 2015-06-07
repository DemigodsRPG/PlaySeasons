package com.playseasons.registry;

import com.demigodsrpg.util.datasection.DataSection;
import com.playseasons.model.PlayerModel;

public class PlayerRegistry extends AbstractSeasonsRegistry<PlayerModel> {
    @Override
    protected PlayerModel valueFromData(String s, DataSection dataSection) {
        return null;
    }

    @Override
    protected String getName() {
        return "players";
    }
}
