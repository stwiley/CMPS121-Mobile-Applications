package com.example.stw.weathercheck;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//This is where the json strings for things related to the actual location are kept

public class ObservationLocation {

    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("full")
    @Expose
    public String full;
    @SerializedName("elevation")
    @Expose
    public String elevation;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("longitude")
    @Expose
    public String longitude;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("country_iso3166")
    @Expose
    public String countryIso3166;
    @SerializedName("latitude")
    @Expose
    public String latitude;

}
