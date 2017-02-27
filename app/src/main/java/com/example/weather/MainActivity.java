package com.example.weather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView currentTV;
    ImageView weatherIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toast.makeText(this, "dfdsfds", Toast.LENGTH_SHORT).show();



        currentTV= (TextView) findViewById(R.id.textView);
        weatherIV= (ImageView) findViewById(R.id.imageView);

        ((Button) findViewById(R.id.goBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the ET
                String cityToSearch= ((EditText) findViewById(R.id.editText)).getText().toString();

                String urlsearch = "http://api.openweathermap.org/data/2.5/weather?q="+cityToSearch+"&appid=5a50565e4e371c28faa56d856e4e7e98&units=metric";
                Downloader downloader= new Downloader();
                downloader.execute(urlsearch);

            }
        });


    }



    public class Downloader extends AsyncTask<String ,Void, String>
    {
        @Override
        protected String doInBackground(String... params) {

            BufferedReader input = null;
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line="";
                while ((line=input.readLine())!=null){
                    response.append(line+"\n");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if (input!=null){
                    try {
                        //must close the reader
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(connection!=null){
                    //must disconnect the connection
                    connection.disconnect();
                }
            }
            return response.toString();

        }


        @Override
        protected void onPostExecute(String jsonText) {

            Log.d("string", jsonText);
            Gson gson = new Gson();

            BigJson bigJson= gson.fromJson(jsonText, BigJson.class);

            MyWeather currentWeather= bigJson.weather.get(0);

            currentTV.setText(currentWeather.description);

            String urlImage= "http://openweathermap.org/img/w/"+currentWeather.icon+".png";

            Picasso.with(MainActivity.this).load(urlImage).into(weatherIV);


            Log.d("dsdd","dsd");


        }
    }






}
