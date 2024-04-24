package com.example.project.myapplication;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private FusedLocationProviderApi locationProviderApi = LocationServices.FusedLocationApi;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    public static final int MILLISECONDS_PER_SECOND = 1000;

    public CountDownTimer myTimer;

    public static TextView tvSpeed;
    public static TextView tvNotification2;
    public static TextView tvNotification3;
    public static TextView tvConsumption;

    double oldLocationX = 0.0;
    double oldLocationY = 0.0;

    final long totalSeconds = 7200;
    final long intervalSeconds = 1;

 //   AlertDialog.Builder builder1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity_page);

        tvConsumption = (TextView) findViewById(R.id.tvConsumption);
        tvNotification2 = (TextView) findViewById(R.id.tvNotification2);
        tvNotification3 = (TextView) findViewById(R.id.tvNotification3);
        tvSpeed = (TextView) findViewById(R.id.tvSpeed);
        tvSpeed.setText("");


   /*     builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Write your message here.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

    */

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();

        locationRequest.setFastestInterval(MILLISECONDS_PER_SECOND); // 3 *
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Button closeButton = (Button) findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeProgram();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        myTimer = new CountDownTimer(totalSeconds * 1000, intervalSeconds * 1000) {

            long timerresult = 0;
            long secondBeenUsed = 0;
            int seconds = 0;

            int min = 0;
            int hours = 0;

            public void onTick(long millisUntilFinished) {

                timerresult = (totalSeconds * 1000 - millisUntilFinished) / 1000;

                min = (int) TimeUnit.SECONDS.toMinutes(timerresult);
                seconds = (int)TimeUnit.SECONDS.toSeconds(timerresult) % 60;
                hours = (int)TimeUnit.SECONDS.toHours(timerresult) / 3600 ;

               // tvNotification3.setText(String.valueOf(seconds) + " " +secondsInfo);

                /*
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                tvNotification3.setText(hms);//set text
                */

                String time = String.format("%02d:%02d:%02d", hours, min, seconds);
                tvNotification3.setText(time);

                secondBeenUsed = timerresult;
            }

            public boolean showConsume()
            {
                long consume = secondBeenUsed;
                double amount;
                amount = (0.5 + (consume * 0.00167));

                double d = amount;
                String formattedData = String.format("%.04f", d);

                String cInfo = getString(R.string.consumeinfo);
                String literInfo = getString(R.string.literinfo);
                String prizeInfo = getString(R.string.prize);

                double prize;
                prize = (amount * 1.33);
                String formattedDataPrize = String.format("%.02f", prize);

                Toast.makeText(MapsActivity.this,  cInfo + " "  + formattedData + " " + literInfo + "\n\n" + prizeInfo + formattedDataPrize +" €", Toast.LENGTH_SHORT).show();

                //below works OK!
                //Toast.makeText(MapsActivity.this,  cInfo + " " + (1 + (consume * 0.00167)) + " " + literInfo, Toast.LENGTH_SHORT).show();


                return false;
            }

            public void onFinish()
            {
             //   AlertDialog alert11 = builder1.create();
             //   alert11.show();
                showConsume();

                //tvNotification.setText( "done! Time's up!");      (1 + ((timerresult / 60 ) * 0.001667))  // (1 + (secondBeenUsed * 0.01667))
                //Toast.makeText(MapsActivity.this, "Approximal gasoline consume is  " + (1 + (secondBeenUsed * 0.01667)) + "liters" , Toast.LENGTH_SHORT).show();
                cancel();
            }
        };

        myTimer.start();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setMyLocationEnabled(true);

    }

    public void onSearch(View view) {
        EditText location_textview = (EditText) findViewById(R.id.location_textview);
        String location = location_textview.getText().toString();

        List<Address> addressList = null;

        if (!location.equals("") )       // (location != null || !location.equals(""))
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }


            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }

        else {
            Toast.makeText(this, R.string.validlocation, Toast.LENGTH_LONG).show();
        }
    }

    public void closeProgram()
    {
        myTimer.onFinish();
        myTimer.cancel();
        finish();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient.isConnected()) {
            requestLocationUpdates();
            myTimer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        myTimer.onFinish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {

        // car consumes 1 liter per 18km
        // 1 liter per minute, so every second consumes 16,67 ml of gasoline / 0,001667 liter

        // first, let's calculate speed
        //float gasolinePerSecondInMilliliters = 16.67f;
        float speed = (float) location.getSpeed();
        float speedInKmh = (float) (speed * 3600 / 1000);
        String formattedString = String.format("%.2f", speedInKmh);

        String speedString = String.format("%.1f", speed);
        tvSpeed.setText(formattedString + " /kmh");

     /*   if (speed == 0.0)
        {
            tvConsumption.setText(speedString + " m/s");
            //tvNotification.setText("Completely stopped");
        }

        if (speed < 0.9)
        {
            tvConsumption.setText(speedString + " m/s");
            //tvNotification.setText("Stopped");
        }

        if (speed > 0.9)
        {
            tvConsumption.setText(speedString + " m/s");
            //tvNotification.setText("Walking");
        }

        if (speed >= 1.5)
        {
            tvConsumption.setText(speedString + " m/s");
            //tvNotification.setText("Slow driving");
        }

        if (speed > 1.55)
        {
            tvConsumption.setText(speedString + " m/s");
            //tvNotification.setText("Moving");
        }

*/
        // now we get our X and Y coordinates
        double locationX = location.getLatitude();
        double locationY = location.getLongitude();
        LatLng newerLatlang = new LatLng(locationX, locationY);

        mMap.animateCamera(CameraUpdateFactory.newLatLng(newerLatlang));

        // Here we figure our direction
        /*
        // if Latitude is increasing, we are heading to the west
        if(locationX > oldLocationX)
        {
            //Toast.makeText(this, "going to west", Toast.LENGTH_SHORT).show();
            tvNotification.setText("West");
        }

        // .. and if Latitude is decreasing, then we are heading to the east
        if(locationX < oldLocationX)
        {
            tvNotification.setText("East");
        }

        // if Longitude is increasing, we are heading to south
        if(locationY > oldLocationY)
        {
            tvNotification.setText("South");
        }

        // ..therefore, decreasing Longitude indicates that we are heading to north
        if(locationY < oldLocationY)
        {
            tvNotification.setText("North");
        }

        if (locationX > oldLocationX || locationY > oldLocationY) // southwest
        {
            tvNotification.setText("Southwest");
        }

        if (locationX > oldLocationX || locationY < oldLocationY) // northwest
        {
            tvNotification.setText("Northwest");
        }

        if (locationX < oldLocationX || locationY < oldLocationY) // northeast
        {
            tvNotification.setText("Northeast");
        }

        if (locationX < oldLocationX || locationY > oldLocationY) // southeast
        {
            tvNotification.setText("Southeast");
        }
        */

        oldLocationX = locationX;
        oldLocationY = locationY;
    }

}