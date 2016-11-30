package com.example.stw.messagerhw3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

//This generates the list for our responses
public class ResponseHelp {
    @SerializedName("result_list")
    @Expose
    public List<RegistrationResponse> resultList = new ArrayList<RegistrationResponse>();
}