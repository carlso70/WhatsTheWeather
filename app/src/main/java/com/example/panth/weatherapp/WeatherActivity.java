package com.example.panth.weatherapp;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.panth.weatherapp.data.Channel;
import com.example.panth.weatherapp.data.Item;
import com.example.panth.weatherapp.service.WeatherServiceCallback;
import com.example.panth.weatherapp.service.YahooWeatherService;


public class WeatherActivity extends ActionBarActivity implements WeatherServiceCallback {

    // UI elements
    private TextView temperatureTextView;
    private TextView locationTextView;
    private TextView conditionTextView;
    private ImageView weatherIconImageView;
    private EditText searchWeatherText;
    private Button searchWeatherButton;

    // Service
    private YahooWeatherService service;
    private ProgressDialog dialog;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherIconImageView = (ImageView)findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView)findViewById(R.id.temperatureTextView);
        locationTextView = (TextView)findViewById(R.id.locationTextView);
        conditionTextView = (TextView)findViewById(R.id.conditionTextView);
        searchWeatherText = (EditText)findViewById(R.id.addressSearch);
        searchWeatherButton = (Button)findViewById(R.id.searchWeatherButton);

        service = new YahooWeatherService(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....please wait");
        dialog.show();

        service.refreshWeather("Chicago, IL");
    }


    // Weather service callback method
    @Override
    public void serviceSuccess(Channel channel) {
        dialog.hide();

        Item item = channel.getItem();
        System.out.println("Image resource = " + item.getCondition().getCode());
        int resource = getResources().getIdentifier("drawable/icon" + item.getCondition().getCode(), null, getPackageName());

        @SuppressWarnings("deprecation")
        Drawable weatherIcon = getResources().getDrawable(resource);

        // \u00B0 is the unicode for degree symbol
        temperatureTextView.setText(item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature());
        locationTextView.setText(service.getLocation());
        conditionTextView.setText(item.getCondition().getDescription());
        weatherIconImageView.setImageDrawable(weatherIcon);
    }

    // Weather service callback method
    @Override
    public void serviceFailure(Exception e) {
        Toast.makeText(WeatherActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        dialog.hide();
    }

    // Ran when button is clicked
    public void searchWeather() {
        if (searchWeatherText.getText().toString() == null) {
            Toast.makeText(WeatherActivity.this, "Please enter an address in the text field", Toast.LENGTH_LONG).show();
            return;
        }
        service.refreshWeather(searchWeatherText.getText().toString());
    }
}
