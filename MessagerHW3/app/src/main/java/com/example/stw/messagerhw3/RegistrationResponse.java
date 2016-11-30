package com.example.stw.messagerhw3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//The various components of the responses we post
public class RegistrationResponse {
    @SerializedName("timestamp")
    @Expose
    public String time;
    @SerializedName("message")
    @Expose
    public String mess;
    @SerializedName("nickname")
    @Expose
    public String nickname;
    @SerializedName("user_id")
    @Expose
    public String user_id;
    @SerializedName("message_id")
    @Expose
    public String message_id;
}
