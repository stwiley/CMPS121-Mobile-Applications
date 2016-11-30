package com.example.stw.messagerhw3;

//Adapted from Shobhit's code
public class ListElement {
    ListElement() {};

    //Set our various elements contained in our list of messages
    public String mess;
    public String user_id;
    public String nName;
    public String time;
    public boolean user;
    public String message_id;
    public String[] users_ids;


    ListElement(String ms, String ui, String t, String nick, boolean userYes, String mess_id, String[] users) {
        //Set them properly
        mess = ms;
        user_id = ui;
        nName = nick;
        time=t;
        user= userYes;
        users_ids = users;
        message_id=mess_id;
    }

}

