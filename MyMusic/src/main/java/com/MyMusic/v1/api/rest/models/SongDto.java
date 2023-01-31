package com.MyMusic.v1.api.rest.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class SongDto {
    public final int id;
    public final String name;
    public final String url;
    public final String authorUsername;
    public final BigDecimal price;
    public final Integer votes;
    public final boolean isLoved;
    public final boolean isBought;

    @JsonCreator
    public SongDto(@JsonProperty("id") int id,@JsonProperty("name") String name, @JsonProperty("url") String url,
                     @JsonProperty("authorUsername") String authorUsername, @JsonProperty("price") BigDecimal price, @JsonProperty("votes") Integer votes,@JsonProperty("isLoved") Boolean isLoved,@JsonProperty("isBought") Boolean isBought) {
        this.id=id;
        this.name = name;
        this.url = url;
        this.authorUsername = authorUsername;
        this.price=price;
        this.votes = votes;
        this.isLoved=isLoved;
        this.isBought=isBought;
    }
}
