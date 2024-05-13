package com.mywatchs.db.Dao.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CompletedSerie {
    @PrimaryKey
    private long id;
    private String name;
    private String posterPath;

    public CompletedSerie(long id, String name, String posterPath) {
        this.id = id;
        this.name = name;
        this.posterPath = posterPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}
