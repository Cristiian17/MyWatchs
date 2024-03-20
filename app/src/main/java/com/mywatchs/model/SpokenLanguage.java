package com.mywatchs.model;

import com.google.gson.annotations.SerializedName;

public class SpokenLanguage {
    @SerializedName("english_name")
    private String englishName;
    @SerializedName("iso_639_1")
    private String iso6391;
    @SerializedName("name")
    private String name;

    public SpokenLanguage() {
    }

    public SpokenLanguage(String englishName, String iso6391, String name) {
        this.englishName = englishName;
        this.iso6391 = iso6391;
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}