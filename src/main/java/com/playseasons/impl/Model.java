package com.playseasons.impl;

import java.util.Map;

public interface Model {
    default PlaySeasons getSeasons() {
        return PlaySeasons.INST;
    }

    String getKey();

    Map<String, Object> serialize();
}
