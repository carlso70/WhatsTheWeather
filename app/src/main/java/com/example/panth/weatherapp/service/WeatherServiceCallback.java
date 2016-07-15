package com.example.panth.weatherapp.service;

import com.example.panth.weatherapp.data.Channel;

/**
 * Created by panth on 7/14/2016.
 */
public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);
    void serviceFailure(Exception e);
}
