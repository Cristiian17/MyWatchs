package com.mywatchs.model.genre;

public class GenreResponse {
    private Genre[] genres;

    public Genre[] getResults() {
        return genres;
    }

    public void setGenres(Genre[] genres) {
        this.genres = genres;
    }
}
