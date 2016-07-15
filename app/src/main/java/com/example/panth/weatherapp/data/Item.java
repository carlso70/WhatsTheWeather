package com.example.panth.weatherapp.data;

import org.json.JSONObject;

/**
 * Created by panth on 7/14/2016.
 */
public class Item implements JSONPopulator{

    private Condition condition;

    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));
    }
}
