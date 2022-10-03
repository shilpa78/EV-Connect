package com.example.auth_app.ui.tripPlanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.auth_app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class TripActivity extends AppCompatActivity {

    public static EditText source;
    public static EditText destination;
    public Button search;
    public static LatLng latLng_dest;
    public static LatLng latLng_source;

    double latitude;
    double longitude;

    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        source = (EditText) findViewById(R.id.source_field);
        destination = (EditText) findViewById(R.id.destination_field);
        search = (Button) findViewById(R.id.search);
        geocoder = new Geocoder(this);

        LocationManager locationManager = (LocationManager)
                getSystemService(this.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager
                .NETWORK_PROVIDER);
        Log.i("",""+location);
        if(location!=null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                latLng_source = new LatLng(address.getLatitude(), address.getLongitude());
                Log.i("LAT LONG SOURCE",""+latLng_source.latitude+" "+latLng_source.longitude);
                System.out.println("dd");
                source.setText(address.getAddressLine(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                latLng_dest = getLocationFromAddress(getApplicationContext(), String.valueOf(destination.getText()));
                Log.i("LAT LONG DEST",""+latLng_dest.latitude+" "+latLng_dest.longitude);
                Intent intent = new Intent(getBaseContext(), TripPlannerMapsActivity.class);
                startActivity(intent);
            }
        });
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
}