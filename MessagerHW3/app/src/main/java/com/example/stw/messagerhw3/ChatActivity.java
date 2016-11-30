package com.example.stw.messagerhw3;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class ChatActivity extends AppCompatActivity{

    //Declare all of the various items ChatActivity will use
    private LocationData locationData = LocationData.getLocationData();
    boolean userPost=false;
    EditText messText;
    float longi;
    float lat;
    TextView longlat;
    Button refreshButton;
    Button postButton;
    MyAdapter adapter;
    String nick;
    String user_id;
    String mess;
    String mess_id;
    ArrayList<ListElement> arrList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Get our userid and nickname as set in the previous activity
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = preferences.getString("user_id", null);
        nick = getIntent().getExtras().getString("nickname");
        //Show user their longitude and latitude for testing
        longi = (float) locationData.getLocation().getLongitude();
        lat = (float) locationData.getLocation().getLatitude();
        longlat= (TextView) findViewById(R.id.longlat);
        longlat.setText("Long: "+longi+" Lat: "+lat);
        //Set up all our buttons
        postButton = (Button) findViewById(R.id.post);
        refreshButton = (Button) findViewById(R.id.refresh);
        messText = (EditText) findViewById(R.id.message);
        //Set up the list of messages
        arrList = new ArrayList<ListElement>();
        adapter = new MyAdapter(this, R.layout.list_element, arrList);
        ListView lView = (ListView) findViewById(R.id.lView);
        lView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //Get any previous messages posted to the server
        getMessages();
    }

    @Override
    protected void onResume(){
        //When the app resumes, re-initialize our arrays
        arrList = new ArrayList<ListElement>();
        adapter = new MyAdapter(this, R.layout.list_element, arrList);
        ListView myListView = (ListView) findViewById(R.id.lView);
        myListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //Get the messages again
        getMessages();
        super.onResume();
    }

    //Our message service will handle the server calls
    public interface messageService{
        //Parameters for posting messages
        @POST("post_message")
        Call<RegistrationResponse> post_message(@Query("lat") float lat,
                                                @Query("lng") float lng,
                                                @Query("user_id") String user_id,
                                                @Query("nickname") String nickname,
                                                @Query("message") String message,
                                                @Query("message_id") String message_id);

        //Parameters for getting messages
        @GET("get_messages")
        Call<ResponseHelp> get_messages(@Query("lat") float lat,
                                        @Query("lng") float lng,
                                        @Query("user_id") String user_id);

    }


    private void messPost(){
        //Get the text we want to post
        mess = messText.getText().toString();
        //Create a random message id for the text
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SecureRandomString srs = new SecureRandomString();
        mess_id = srs.nextString();
        SharedPreferences.Editor e = preferences.edit();
        e.putString("message_id", mess_id);
        e.commit();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://luca-teaching.appspot.com/localmessages/default/")
                .addConverterFactory(GsonConverterFactory.create())	//parse Gson string
                .client(httpClient)	//add logging
                .build();

        //Set up a message service, much like our weather service from assignment 2
        messageService service = retrofit.create(messageService.class);
        //Somewhat of a fix to the user not being labelled as you correctly
        userPost=true;
        arrList.add(new ListElement(mess, user_id, "timestamp", nick, userPost, mess_id, null));
        adapter.notifyDataSetChanged();
        //Get the response from the server with our new message
        Call<RegistrationResponse> getResponse = service.post_message(lat, longi, user_id, nick, mess, mess_id);
        //Set our user boolean back to false
        userPost=false;
        //Call retrofit asynchronously
        getResponse.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Response<RegistrationResponse> response) {
            }

            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
            }
        });
    }

    //Get messages from the server
    private void getMessages(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://luca-teaching.appspot.com/localmessages/default/")
                .addConverterFactory(GsonConverterFactory.create())	//parse Gson string
                .client(httpClient)	//add logging
                .build();

        messageService service = retrofit.create(messageService.class);
        Call<ResponseHelp> getResponse = service.get_messages(lat,longi,user_id);

        //Call retrofit asynchronously
        getResponse.enqueue(new Callback<ResponseHelp>() {
            @Override
            public void onResponse(Response<ResponseHelp> response) {
                List<RegistrationResponse> mList;
                if (response.code() == 200) {
                    mList = response.body().resultList;
                } else
                    mList = new ArrayList<RegistrationResponse>();
                arrList.clear();
                //Display the messages from last refreshed.
                int x=0;
                for (int i = mList.size() - 1; i >=0; i--) {
                    //Get the various components of a message
                    x++;
                }
                String[] users =new String[x];
                x=0;
                for (int i = mList.size() - 1; i >=0; i--) {
                    x++;
                    String mess = response.body().resultList.get(i).mess;
                    String user_id = response.body().resultList.get(i).user_id;
                    String time = response.body().resultList.get(i).time;
                    String nick = response.body().resultList.get(i).nickname;
                    String mess_id = response.body().resultList.get(i).message_id;
                    users[x] = response.body().resultList.get(i).user_id;
                    arrList.add(new ListElement(mess, user_id, time, nick, userPost, mess_id, users));
                    //Check to see if any of them are messages by the user
                    youMessageCheck();
                }
                //Change the data set
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
            }
        });


    }
    //For some reason this method is needed for clicking refresh and getting messages
    public void refClick(View v){
        getMessages();
    }

    //When the post button is clicked
    public void postClicked(View v){
        messPost();
        //Reset text back to blank
        messText.setText("");
    }


    //Check for if the user posted a message. If so, add a (You!) to the end of it.
    public void youMessageCheck(){
        for(int i = 0; i < arrList.size(); i++){
            //Compare the user ids
            if(arrList.get(i).user_id.equals(this.user_id)){
                //Set our boolean to true so it adds the you!
                this.userPost = true;
            }else{
                //Else, don't set it
                this.userPost=false;
            }
        }
    }


}
