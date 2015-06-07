package com.playseasons.registry;

import com.demigodsrpg.util.datasection.AbstractDataRegistry;
import com.demigodsrpg.util.datasection.AbstractPersistentModel;
import com.playseasons.Setting;

public abstract class AbstractSeasonsRegistry<T extends AbstractPersistentModel<String>> extends AbstractDataRegistry<T> {
    public AbstractSeasonsRegistry() {
        super(Setting.SAVE_PATH, Setting.SAVE_PRETTY, Setting.USE_PSQL, Setting.PSQL_CONNECTION);
    }
}
