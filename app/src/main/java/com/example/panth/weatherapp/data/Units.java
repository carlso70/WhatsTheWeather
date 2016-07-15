package com.example.panth.weatherapp.data;

import org.json.JSONObject;

/**
 * Created by panth on 7/14/2016.
 */
public class Units implements JSONPopulator{

    private String temperature;

    @Override
    public void populate(JSONObject data) {
        temperature = data.optString("temperature");
    }
}
