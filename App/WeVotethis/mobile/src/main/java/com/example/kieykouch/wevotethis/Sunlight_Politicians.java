package com.example.kieykouch.wevotethis;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kieykouch on 3/11/16.
 */
public class Sunlight_Politicians extends AsyncTask<String, Void, String> {

    private String urlString = null;
    private Data dc = Data.getInstance();
    private String key = "778b67a7e69b4f779d949b64602e1d46";


    public void setData(String Zipcode){
        urlString = "http://congress.api.sunlightfoundation.com/legislators/locate?zip=" + Zipcode + "&apikey=" + key;
    }

    public void setData(Double latitude, Double longitude){
        urlString = "http://congress.api.sunlightfoundation.com/legislators/locate?latitude=" + latitude.toString() + "&longitude=" + longitude.toString()+ "&apikey=" + key;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            System.out.println("****");

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            forecastJsonStr = buffer.toString();
        }
        catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }

        return forecastJsonStr;
    }
}
