package com.example.stw.messagerhw3;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.prefs.Preferences;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    //Set up our various variables
    private String user_id;
    EditText NickEnter;
    Button chatBttn;
    Boolean nickset;
    public String nick= "Nick";

    public static String LOG_TAG = "My log tag";
    public final static String Chat = "com.example.stw.messagerhw3";
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    private LocationData locationData = LocationData.getLocationData();//store location to share between activities

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialize the text and buttons
        NickEnter= (EditText)findViewById(R.id.nickEnter);
        chatBttn= (Button)findViewById(R.id.chatButton);
        chatBttn.setOnClickListener(this);
        chatBttn.setEnabled(false);
        // Gets the settings, and creates a random user id if missing.
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = settings.getString("user_id", null);
        if (user_id == null) {
            // Creates a random one, and sets it.
            SecureRandomString srs = new SecureRandomString();
            user_id = srs.nextString();
            SharedPreferences.Editor e = settings.edit();
            e.putString("user_id", user_id);
            e.commit();
        }
    }

    @Override
    public void onResume(){
        Log.i(LOG_TAG, "Inside resume of main activity");
        //check if user already gave permission to use location
        Boolean locationAllowed = checkLocationAllowed();

        if(locationAllowed){
            requestLocationUpdate();
        }else{
            chatBttn.setEnabled(false);
        }
        //Set the button text between "Enable Location" or "Disable Location"
        render();
        super.onResume();
    }

    //Set the user nickname
    public void nickSet(){
        //Using the edittext
        String nName= NickEnter.getText().toString();
        //If there's no nickname, don't allow the user to proceed
        if(nName.isEmpty()){
            chatBttn.setEnabled(false);
            nickset=false;
        } else{
            //Save our nickname and get the location
            nickset=true;
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor e = settings.edit();
            e.putString("nickname", nName);
            e.commit();
            nick=nName;
            requestLocationUpdate();
        }

    }

    private void render(){
        //Check to see if they allowed the location
        Boolean locationAllowed = checkLocationAllowed();
        Button button = (Button) findViewById(R.id.toggleButton);

        //If it was, set our button
        if(locationAllowed) {
            button.setText("Disable location");
        }
        else {
            button.setText("Enable location");
        }
    }

    /*
	Check users location sharing setting
	 */
    private boolean checkLocationAllowed(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        return settings.getBoolean("location_allowed", false);
    }

    //Set our location
    private void setLocationAllowed(boolean allowed){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("location_allowed", allowed);
        editor.commit();
    }

    /*
	Request location update. This must be called in onResume if the user has allowed location sharing
	 */
    private void requestLocationUpdate(){
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null &&
                (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 35000, 10, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 35000, 10, locationListener);

                Log.i(LOG_TAG, "requesting location update");
            }
            else {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    Log.i(LOG_TAG, "please allow to use your location");

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        } else{
            Log.i(LOG_TAG, "requesting location update from user");
            //prompt user to enable location
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }
    }

    /**
     * Listens to the location, and gets the most precise recent location.
     * Copied from Prof. Luca class code
     */
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            Location lastLocation = locationData.getLocation();

            String nName= NickEnter.getText().toString();
            // Do something with the location you receive.
            double newAccuracy = location.getAccuracy();
            Log.i(LOG_TAG, "Accuracy is: "+newAccuracy);

            long newTime = location.getTime();
            // Is this better than what we had?  We allow a bit of degradation in time.
            boolean isBetter = ((lastLocation == null) ||
                    newAccuracy < lastLocation.getAccuracy() + (newTime - lastLocation.getTime()));
            if (isBetter) {
                // We replace the old estimate by this one.
                locationData.setLocation(location);

                //Now we have the location.
                if(checkLocationAllowed() && nName.isEmpty()!= true)
                    chatBttn.setEnabled(true);//We must enable search button
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

    //Remove the location
    private void removeLocationUpdate() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

                locationManager.removeUpdates(locationListener);
                Log.i(LOG_TAG, "removing location update");
            }
        }

    }

    @Override
    public void onPause(){
        if(checkLocationAllowed())
            removeLocationUpdate();//if the user has allowed location sharing we must disable location updates now
        super.onPause();
    }


    //Allows us to toggle whether the location can be used
    public void toggleLocation(View v) {

        Boolean locationAllowed = checkLocationAllowed();

        if(locationAllowed){
            //disable it
            removeLocationUpdate();
            setLocationAllowed(false);//persist this setting

            Button chatButton = (Button) findViewById(R.id.chatButton);
            chatButton.setEnabled(false);//now that we cannot use location, we should disable search facility

        } else {
            //enable it
            requestLocationUpdate();
            setLocationAllowed(true);//persist this setting
        }

        //Set the button text between "Enable Location" or "Disable Location"
        render();
    }



    public void onClick(View v){
        //Create our Chat Activity
        Intent intent = new Intent(this, ChatActivity.class);
        EditText editText = (EditText) findViewById(R.id.nickEnter);
        //Set the nickname
        nickSet();
        if (nickset == true) {
            //Transfer the nickname over to the chat activity
            intent.putExtra("nickname", editText.getText().toString());
            startActivity(intent);//pass the cuisine to the search activity for searching
        }else if(nickset==false){
            //Don't allow blank names
            Toast.makeText(MainActivity.this, "Please enter a name that's not blank.", Toast.LENGTH_LONG).show();
        }
    }

}
