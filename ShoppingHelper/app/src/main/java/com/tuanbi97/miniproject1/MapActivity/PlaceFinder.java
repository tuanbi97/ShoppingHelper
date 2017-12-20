package com.tuanbi97.miniproject1.MapActivity;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 6/18/2017.
 */

class PlaceFinder {
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyCajZ2N1pFkzHFgak-mL3-h2zxoZt2dUEk";
    private PlaceFinderListener listener;
    private List<String> placeIDs;
    private int nPlace = 0;

    public PlaceFinder(PlaceFinderListener listener) {
        this.listener = listener;
    }

    public String getPlace(Marker... params) throws UnsupportedEncodingException {
        nPlace = params.length;
        Log.d("placefinder", Integer.toString(nPlace));
        placeIDs = new ArrayList<String>();
        for (int i = 0; i < params.length; i++){
            String url = createUrl(params[i].getPosition(), 30);
            Log.d("placefinder", url);
            new DownloadRawData().execute(url);
        }
        return null;
    }

    private String createUrl(LatLng p, Integer radius) throws UnsupportedEncodingException {
        return DIRECTION_URL_API + "location=" + Double.toString(p.latitude) + "," + Double.toString(p.longitude) + "&radius=" + radius.toString() + "&key=" + GOOGLE_API_KEY;
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                parseJSon(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJSon(String data) throws JSONException {
        Log.d("placefinder", "parse");
        if (data == null)
            return;

        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonResults = jsonData.getJSONArray("results");
        JSONObject jsonResult = jsonResults.getJSONObject(0);
        String placeID = jsonResult.getString("place_id");
        placeIDs.add("place_id:" + placeID);
        nPlace--;
        if (nPlace == 0)
            listener.onPlaceFinderSuccess(placeIDs);
    }
}
