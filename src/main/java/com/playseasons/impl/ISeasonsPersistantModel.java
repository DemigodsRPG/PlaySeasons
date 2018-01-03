package com.playseasons.impl;

public interface ISeasonsPersistantModel {
    default PlaySeasons getSeasons() {
        return PlaySeasons.INST;
    }
}
