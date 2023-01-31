package com.MyMusic.v1.services.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MusicCreator {
    public final User userProperties;

    public MusicCreator(User userProperties) {
        this.userProperties = userProperties;
    }


}
