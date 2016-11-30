package com.example.stw.weathercheck;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//This is where and how we get a response from the server.

public class wResponse {
    @SerializedName("conditions")
    @Expose
    public Conditions conditions;
    @SerializedName("result")
    @Expose
    public String result;
}
