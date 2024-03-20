package com.mywatchs.model.serie;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Serie {
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("genre_ids")
    private List<Integer> genreIds;
    @SerializedName("id")
    private int id;
    @SerializedName("origin_country")
    private List<String> originCountry;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("original_name")
    private String originalName;
    @SerializedName("overview")
    private String overview;
    @SerializedName("popularity")
    private double popularity;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("first_air_date")
    private String firstAirDate;
    @SerializedName("name")
    private String name;
    @SerializedName("vote_average")
    private double voteAverage;
    @SerializedName("vote_count")
    private int voteCount;

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(List<String> originCountry) {
        this.originCountry = originCountry;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
