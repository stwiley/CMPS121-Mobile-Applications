package com.example.stw.weathercheck;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//This is how we actually go about getting a response.

public class ResponseHelp {
    @SerializedName("response")
    @Expose
    public wResponse response;
}
