package com.mywatchs.model.serie;

import com.google.gson.annotations.SerializedName;

class Creator {
    @SerializedName("id")
    private int id;

    @SerializedName("credit_id")
    private String creditId;

    @SerializedName("name")
    private String name;

    @SerializedName("gender")
    private int gender;

    @SerializedName("profile_path")
    private String profilePath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}
