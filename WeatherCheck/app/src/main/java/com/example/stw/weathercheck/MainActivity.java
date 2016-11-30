package com.example.stw.weathercheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {
    //Set the parameters for where our statements will show up
    TextView wind;
    TextView weather;
    TextView windchill;
    TextView pressure;
    TextView city;
    TextView elevation;
    TextView temperature;
    TextView humidity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set which data field will correspond to which bit of data
        city = (TextView) findViewById(R.id.city);
        elevation= (TextView) findViewById(R.id.elevation);
        temperature = (TextView) findViewById(R.id.temperature);
        humidity = (TextView) findViewById(R.id.humidity);
        wind = (TextView) findViewById(R.id.wind);
        weather = (TextView) findViewById(R.id.weather);
        windchill = (TextView) findViewById(R.id.windchill);
        pressure = (TextView) findViewById(R.id.pressure);
        }


    public void getData(View v){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        //Set our retrofit to get the string we need to parse
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://luca-teaching.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())	//parse Gson string
                .client(httpClient)	//add logging
                .build();

        //Use the Weathservice interface to create our retrofit
        WeathService service = retrofit.create(WeathService.class);

        //Call for a response from the server
        Call<ResponseHelp> queryResponseCall = service.getResponse();


        queryResponseCall.enqueue(new Callback<ResponseHelp>() {
            //What to do when a response is gotten
            @Override
            public void onResponse(Response<ResponseHelp> resp) {
                //If we get a 500 internal server error
                if(resp.code() == 500){
                    Toast.makeText(MainActivity.this, "There was a problem contacting the server. Please try again.", Toast.LENGTH_LONG).show();
                }else if(resp.body().response.result.equals("error")) {
                    //If we come across the string reading 'error'
                    Toast.makeText(MainActivity.this, "The data failed to display. Please try again.", Toast.LENGTH_LONG).show();
                }else{
                    //Everything is fine
                    printText(resp);
                }
            }


            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
                Toast.makeText(MainActivity.this, "If you're seeing this, something's gone wrong.", Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onResume(){
        super.onResume();
    }

    //Call GET to get the server response and such
    public interface WeathService {
        @GET("weather/default/get_weather")
        Call<ResponseHelp> getResponse();
    }

    public void printText(Response<ResponseHelp> response){
        //Set all of our strings to the proper parsed response
        Integer windS=response.body().response.conditions.windGustMph;
        Double windG=response.body().response.conditions.windMph;
        Double temp = response.body().response.conditions.tempF;
        String loc = response.body().response.conditions.observationLocation.city;
        String elev = response.body().response.conditions.observationLocation.elevation;
        String weath= response.body().response.conditions.weather;
        String humid=response.body().response.conditions.relativeHumidity;
        String windC= response.body().response.conditions.windchillC;
        String press= response.body().response.conditions.pressureMb;
        //Set the text views accordingly
        wind.setText("Wind(Average + gusts mph): "+windS+" and "+windG);
        weather.setText("Weather: "+weath);
        temperature.setText("Temperature (F): " + temp);
        city.setText("City: " + loc);
        elevation.setText("Elevation: "+elev);
        humidity.setText("Humidity: "+humid);
        windchill.setText("Wind Chill (C): "+windC);
        pressure.setText("Pressure (mb): "+press);


    }

}
