package com.example.panth.weatherapp.data;

import org.json.JSONObject;

/**
 * Created by panth on 7/14/2016.
 */
public class Channel implements JSONPopulator {
    private Units units;
    private Item item;
    @Override
    public void populate(JSONObject data) {
        units = new Units();
        units.populate(data.optJSONObject("units"));

        item = new Item();
        item.populate(data.optJSONObject("item"));
    }

    public Units getUnits() {
        return units;
    }

    public Item getItem(){
        return item;
    }
}
