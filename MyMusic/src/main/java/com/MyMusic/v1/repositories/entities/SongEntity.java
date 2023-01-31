package com.MyMusic.v1.repositories.entities;

import java.math.BigDecimal;
import java.util.Objects;

public class SongEntity {
    public final int id;
    public final String name;
    public final String url;
    public final String authorUsername;
    public final BigDecimal price;
    public final int votes;
    private boolean isBought=false;
    private boolean isLoved=false;

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }

    public boolean isLoved() {
        return isLoved;
    }

    public void setLoved(boolean loved) {
        isLoved = loved;
    }

    public SongEntity(int id, String name, String url, String authorUsername, BigDecimal price, int votes) {
        this.id=id;
        this.name = name;
        this.url=url;
        this.authorUsername = authorUsername;
        this.price=price;
        this.votes = votes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongEntity that = (SongEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
