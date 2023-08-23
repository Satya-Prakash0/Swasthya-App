package com.example.swasthya_2o;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FetchData extends AsyncTask<Object,String,String> {

    String googleMapNearbyPlacesData;
    GoogleMap googleMap;
    String url;

    @Override
    protected String doInBackground(Object... objects) {
        try{
            googleMap=(GoogleMap) objects[0];
            url=(String) objects[1];
            DownloadUrl downloadUrl=new DownloadUrl();
            googleMapNearbyPlacesData= downloadUrl.retriveUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("googleMapNearbyPlacesData", googleMapNearbyPlacesData);
        return googleMapNearbyPlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        try{
            JSONObject jsonObject=new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
//            Log.d("size", String.valueOf(jsonArray.length()));

            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                JSONObject getlocation=jsonObject1.getJSONObject("geometry")
                        .getJSONObject("location");

                String lat=getlocation.getString("lat");
                String lng=getlocation.getString("lng");

//                JSONObject getName=jsonArray.getJSONObject(i);
//                String name=getName.getString("name");
                String name = jsonObject1.getString("name");

                LatLng latLng=new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
                MarkerOptions markerOptions=new MarkerOptions();
                markerOptions.title(name);
                markerOptions.position(latLng);
                googleMap.addMarker(markerOptions);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
