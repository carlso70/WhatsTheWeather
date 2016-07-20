package com.example.panth.weatherapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.example.panth.weatherapp.R;
import com.example.panth.weatherapp.constants.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * WORK IN PROGRESS, this class will automatically get the location of the device
 * Created by jimmycarlson on 7/18/16.
 */
public class FetchAddressIntentService extends IntentService {

    private static final String TAG = "FetchAddressIS";

    // The receiver where results are forward from this service
    protected ResultReceiver mReciever;

    public FetchAddressIntentService() {
        // TAG is the name of the worker thread
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";

        mReciever = intent.getParcelableExtra(Constants.RECEIVER);

        if (mReciever == null) {
            Log.wtf(TAG, "No receiver received. No where to send the results");
            return;
        }

        // Get the location passed to this service through and extra
        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);

        // Makes sure that the location data was really sent over through an extra. If not sends error message and return
        if (location == null) {
            errorMessage = getString(R.string.no_location_data_provided);
            Log.wtf(TAG, errorMessage);
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        // Address found using the Geocoder.
        List<Address> addresses = null;

        try {
            // 1 is the max result of address' that will be received for now
            // getFromLocation returns an array of Addresses for the area immediatly
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            // Catch network or other i/o problems
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, e);
        } catch (IllegalArgumentException illegalArg) {
            // Catch invalid latitude or longitude
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    "Longitude = " + location.getLongitude());
        }

        // Handle case where no address was found
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();
            // Fetch the address lines using {@code getAddressLine},
            // join them, and send them to the thread. The {@link android.location.address}
            // class provides other options for fetching address details that you may prefer
            // to use. Here are some examples:
            // getLocality() ("Mountain View", for example)
            // getAdminArea() ("CA", for example)
            // getPostalCode() ("94043", for example)
            // getCountryCode() ("US", for example)
            // getCountryName() ("United States", for example)
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, getString(R.string.address_found));
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"), addressFragments));
        }
    }

    // Sends a result code and message to the receiver
    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mReciever.send(resultCode, bundle);
    }
}
