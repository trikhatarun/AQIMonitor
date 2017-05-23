package com.android.aqimonitor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.aqimonitor.Api.ApiClient;
import com.android.aqimonitor.Api.ApiInterface;
import com.android.aqimonitor.models.AirData;
import com.android.aqimonitor.models.Reading;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final String TAG = MainActivity.class.getSimpleName();
    private final int REQUEST_PERMISSION_CODE = 110;
    @BindView(R.id.aqi_reading)
    TextView aqiReadingTextView;
    @BindView(R.id.constraint_layout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.aqi_category)
    TextView aqiCategory;
    @BindView(R.id.aqi_reading_loading_bar)
    ProgressBar aqiReadingProgressBar;
    @BindView(R.id.readings)
    RecyclerView readingsRecyclerView;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.suggestion)
    TextView suggestionView;
    private Location locationRecent;

    private GoogleApiClient googleApiClientInstance;
    private DecimalFormat decimalFormat;
    private Typeface Josefin_Bold, Josefin_Regular, Josefin_Light, Josefin_SemiBold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_CODE);
        }

        initiate();
    }

    private void initiate() {
        googleApiClientInstance = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        decimalFormat = new DecimalFormat("#.##");

        Josefin_Regular = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-Regular.ttf");
        Josefin_Bold = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-Bold.ttf");
        Josefin_Light = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-Light.ttf");
        Josefin_SemiBold = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-SemiBold.ttf");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (resultCode == RESULT_OK) {
                initiate();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest locationRequestInstance = LocationRequest.create();
        locationRequestInstance.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequestInstance.setInterval(10000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "permission not granted");
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClientInstance, locationRequestInstance, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        locationRecent = location;
        postGettingLocation();
    }

    private void postGettingLocation() {
        aqiReadingProgressBar.setVisibility(View.VISIBLE);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<AirData> airDataCall = apiInterface.getAirData("geo:" + String.valueOf(Float.valueOf(decimalFormat.format(locationRecent.getLatitude()))) + ";" + String.valueOf(Float.valueOf(decimalFormat.format(locationRecent.getLongitude()))), "99d38e3e6ccba9bba8ccb30a2cd07a9eebe80a3f");

        airDataCall.enqueue(new Callback<AirData>() {
            @Override
            public void onResponse(Call<AirData> call, Response<AirData> response) {
                ArrayList<Reading> dataList = new ArrayList<>();
                AirData.Data data = response.body().getData();
                int aqi = data.getAqi();
                AirData.IAQI iaqi = data.getIaqi();
                AirData.CO co = iaqi.getCo();
                AirData.H h = iaqi.getH();
                AirData.O3 o3 = iaqi.getO3();
                AirData.T t = iaqi.getT();
                AirData.NO2 no2 = iaqi.getNo2();
                AirData.PM25 pm25 = iaqi.getPm25();
                AirData.City city = data.getCity();
                String locationText = city.getName();
                Log.v("location", locationRecent.getLatitude() + " long: " + locationRecent.getLongitude());
                location.setText(locationText);
                location.setTypeface(Josefin_SemiBold);
                int color;

                if (aqi < 51) {
                    color = ContextCompat.getColor(getApplicationContext(), R.color.safe_color);
                    location.setBackgroundColor(color);
                    aqiReadingTextView.setTextColor(color);
                    aqiCategory.setTextColor(color);
                    location.setTextColor(Color.WHITE);
                    aqiCategory.setText(getString(R.string.safe_string));
                    suggestionView.setText(getString(R.string.safe_suggestion));

                } else if (aqi < 101) {
                    color = ContextCompat.getColor(getApplicationContext(), R.color.moderate_color);
                    location.setBackgroundColor(color);
                    aqiReadingTextView.setTextColor(color);
                    aqiCategory.setTextColor(color);
                    aqiCategory.setText(getString(R.string.moderate_string));
                    suggestionView.setText(getString(R.string.moderate_suggestion));

                } else if (aqi < 151) {
                    color = ContextCompat.getColor(getApplicationContext(), R.color.usg_color);
                    location.setBackgroundColor(color);
                    location.setTextColor(Color.WHITE);
                    aqiReadingTextView.setTextColor(color);
                    aqiCategory.setTextColor(color);
                    suggestionView.setText(getString(R.string.sensitive_string));
                } else if (aqi < 201) {
                    color = ContextCompat.getColor(getApplicationContext(), R.color.unhealthy_color);
                    location.setBackgroundColor(color);
                    location.setTextColor(Color.WHITE);
                    aqiReadingTextView.setTextColor(color);
                    aqiCategory.setTextColor(color);
                    aqiCategory.setText(getString(R.string.unhealthy_string));
                    suggestionView.setText(getString(R.string.unhealthy_suggestions));
                } else if (aqi < 301) {
                    color = ContextCompat.getColor(getApplicationContext(), R.color.very_unhealthy_color);
                    location.setBackgroundColor(color);
                    location.setTextColor(Color.WHITE);
                    aqiReadingTextView.setTextColor(color);
                    aqiCategory.setTextColor(color);
                    aqiCategory.setText(getString(R.string.very_unhealthy_string));
                    suggestionView.setText(getString(R.string.very_unhealthy_suggestion));
                } else {
                    color = ContextCompat.getColor(getApplicationContext(), R.color.hazardous_color);
                    location.setBackgroundColor(color);
                    location.setTextColor(Color.WHITE);
                    aqiReadingTextView.setTextColor(color);
                    aqiCategory.setTextColor(color);
                    aqiCategory.setText(getString(R.string.hazardous_string));
                    suggestionView.setText(getString(R.string.hazardous_suggestion));
                }
                aqiReadingProgressBar.setVisibility(View.GONE);
                aqiReadingTextView.setText(String.valueOf(aqi));

                if (co != null) {
                    dataList.add(new Reading(getString(R.string.co_heading_string), String.valueOf(co.getV()), getString(R.string.co_measure_unit)));
                }
                if (h != null) {
                    dataList.add(new Reading(getString(R.string.h_heading_string), String.valueOf(h.getV()), getString(R.string.h_measure_unit)));
                }
                if (o3 != null) {
                    dataList.add(new Reading(getString(R.string.o3_heading_string), String.valueOf(o3.getV()), getString(R.string.p_so2_o3_pm10_pm25_measure_unit)));
                }
                if (t != null) {
                    dataList.add(new Reading(getString(R.string.t_heading_string), String.valueOf(t.getV()), getString(R.string.t_measure_unit)));
                }
                if (no2 != null) {
                    dataList.add(new Reading(getString(R.string.no2_heading_string), String.valueOf(no2.getV()), getString(R.string.p_so2_o3_pm10_pm25_measure_unit)));
                }
                if (pm25 != null) {
                    dataList.add(new Reading(getString(R.string.pm25_heading_string), String.valueOf(pm25.getV()), getString(R.string.p_so2_o3_pm10_pm25_measure_unit)));
                }
                ReadingAdapter readingAdapter = new ReadingAdapter(MainActivity.this, dataList);
                readingsRecyclerView.hasFixedSize();
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 3);
                readingsRecyclerView.setLayoutManager(layoutManager);
                readingsRecyclerView.setAdapter(readingAdapter);
            }

            @Override
            public void onFailure(Call<AirData> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClientInstance.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClientInstance.disconnect();
    }
}
