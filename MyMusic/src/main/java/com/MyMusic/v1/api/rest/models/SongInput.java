package com.MyMusic.v1.api.rest.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class SongInput {
    public final String name;
    public final String url;
    public final BigDecimal price;

    @JsonCreator
    public SongInput(@JsonProperty("name") String name,@JsonProperty("url") String url,
                     @JsonProperty("price") BigDecimal price) {
        this.name = name;
        this.url = url;
        this.price=price;
    }
}
