package com.MyMusic.v1.services.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Song {
    public final int id;
    public final String name;
    public final String url;
    public final String author;
    public final BigDecimal price;
    public final int votes;
    private boolean isLoved;
    private boolean isBought;

    public Song(int id, String author, String name, String url, BigDecimal price, int votes, boolean isLoved, boolean isBought) {
        this.id = id;
        this.author = author;
        this.name = name;
        this.url = url;
        this.price=price;
        this.votes = votes;
        this.isLoved=isLoved;
        this.isBought=isBought;
    }

    public boolean isLoved() {
        return isLoved;
    }

    public boolean isBought() {
        return isBought;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", author='" + author + '\'' +
                ", price='" + price + '\'' +
                ", votes=" + votes +
                '}';
    }
}
