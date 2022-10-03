package com.example.auth_app.ui.tripPlanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auth_app.R;
import com.example.auth_app.databinding.ActivityTripPlannerMapsBinding;
import com.example.auth_app.ui.maps.StationMapActivity;
import com.example.auth_app.ui.stations.StationAdapter;
import com.example.auth_app.ui.stations.StationFragment;
import com.example.auth_app.ui.stations.StationListItem;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TripPlannerMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private Geocoder geocoder;
    double latitude_source;
    double longitude_source;
    double latitude_drag;
    double longitude_drag;

    private Toast toastMessage;
    double latitude_destination;
    double longitude_destination;

    public ArrayList<StationListItem> stationListItems;
    public static String name;
    public static double price ;
    public static double lati ;
    public static double longi ;
    public static String address_station ;
    public static String strtTime ;
    public static String closeTime ;
    public static double rating;
    public static String nearbyRestaurant;

    public double lat_mid_point1;
    public double lon_mid_point1;
    public double lat_mid_point2;
    public double lon_mid_point2;
    public double lat_mid_point3;
    public double lon_mid_point3;

    public double lat_radius;
    public double lon_radius;
    public double radius;

    LinearLayout layout;
    public List<Marker> markerList;
    public List<Marker> marker_source_destination;

    public int current_time_hour;
    public String status;
    public static long waitCount=0;

    public double distance_from_curr_location;
    public int image_station;
    public int k=0;

   public ArrayList<String> title_list = new ArrayList<>();

    public TextView textview_stn_nearby;
    public float[] radius_results = new float[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_planner_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map_trip);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);
        stationListItems = new ArrayList<>();
        markerList = new ArrayList<Marker>();
        marker_source_destination = new ArrayList<Marker>();

        layout = new LinearLayout(this);
        Log.i("","Toast");
        Toast.makeText(TripPlannerMapsActivity.this, "Click Orange markers to see nearby Restaurants", Toast.LENGTH_LONG).show();

    }


    //Reference - https://stackoverflow.com/questions/4656802/midpoint-between-two-latitude-and-longitude
    public double[] find_mid_point(double lat1,double lon1,double lat2,double lon2){

        double dLon = Math.toRadians(lon2 - lon1);

        double[] res = new double[2];

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);
        res[0] = Math.toDegrees(lat3);
        res[1] = Math.toDegrees(lon3);
        //System.out.println(Math.toDegrees(lat3) + " " + Math.toDegrees(lon3));
        return res;
    }

    public void mark_stations(double radius, double latitude_source, double longitude_source){

        int[] images_station = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.f,R.drawable.g,R.drawable.h,R.drawable.i,R.drawable.j,R.drawable.k,R.drawable.l,R.drawable.m,R.drawable.n,R.drawable.o,R.drawable.p,R.drawable.q,R.drawable.r,R.drawable.s};
        float[] results = new float[2];
        for(int i=1;i<=55;i++) {

            int finalI = i;
            FirebaseDatabase.getInstance().getReference().child("Stations").child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {
                private static final String TAG = "StationFragment";

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        //
                        Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                        Log.i("TAG", "map>>>>>: " + map);
                        name = (String) map.get("Name");
                        price = (double) map.get("Price");
                        lati = (double) map.get("Latitude");
                        longi = (double) map.get("Longitude");
                        address_station = (String) map.get("Address");
                        strtTime = (String) map.get("Starting Time");
                        closeTime = (String) map.get("Closing Time");
                        rating = (double) map.get("Rating");
                        nearbyRestaurant = (String) map.get("Nearby");
                        waitCount = (long) map.get("Wait Count");
                        Log.i("TAG", "KRKRKRKRK>>>>>: " + rating);
                        android.location.Location.distanceBetween(latitude_source,longitude_source,lati,longi,results);
                        Log.i("", "Current Location : " + latitude_source + ", " + longitude_source + ", "+ results[0]);
                        Log.i("","Station Time : "+strtTime+", "+closeTime);

                        char a = strtTime.charAt(0);
                        char b = strtTime.charAt(1);
                        String str1 = String.valueOf(a);
                        String str2 = String.valueOf(b);

                        String strt_time_hour_str = "";

                        if(!str2.equals(":"))
                            strt_time_hour_str = str1+str2;
                        else if(str2.equals(":"))
                            strt_time_hour_str = str1;

                        int strt_time_hour = Integer.parseInt(strt_time_hour_str);

                        String close_time_hour_str = "";

                        char a1 = closeTime.charAt(0);
                        char b1 = closeTime.charAt(1);
                        String str3 = String.valueOf(a1);
                        String str4 = String.valueOf(b1);

                        Random rd = new Random();
                        int temp_rk = rd.nextInt(17);

                        if(!str4.equals(":"))
                            close_time_hour_str = str3+str4;
                        else if(str4.equals(":"))
                            close_time_hour_str = str3;

                        int close_time_hour = Integer.parseInt(close_time_hour_str);

                        if(current_time_hour>=strt_time_hour && current_time_hour<=close_time_hour)
                            status = "Opened Now";
                        else
                            status = "Closed Now";

                        Log.i("","Station Time Hour"+strt_time_hour+close_time_hour);
                        if(k<17) {
                            image_station = images_station[k++];
                            Log.i("", "K value: " + k);
                        }
                        else{
                            image_station = images_station[temp_rk];
                        }
                        if(results[0]<=radius) {
                            distance_from_curr_location = results[0]/1000;
                            String temp = String.format("%.2f",distance_from_curr_location);
                            distance_from_curr_location = Double.parseDouble(temp);
                            Log.i("","Radius value: "+results[0]);
                            //Log.i("","K value: "+finalK);
                            stationListItems.add(new StationListItem(finalI, name, lati, longi, price, strtTime, closeTime, rating, address_station, distance_from_curr_location, status,image_station,waitCount));

                            try {
                                List<Address> addresses = geocoder.getFromLocation(lati,longi,1);
                                if (addresses.size() > 0) {
                                    Marker marker;
                                    Address address = addresses.get(0);
                                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                                    MarkerOptions markerOptions = new MarkerOptions()
                                            .position(latLng)
                                            .title(name+" "+finalI).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                                    marker = mMap.addMarker(markerOptions);
                                    //marker.setTitle(name+" "+finalI);
                                    markerList.add(marker);
                                    title_list.add(name);
                                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(Marker marker) {
                                                    String title = marker.getTitle();
                                                    onButtonShowPopupWindowClick(title);
                                                    Log.i("","POPUP");
                                                    return false;
                                        }
                                    });

                                }
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//
//        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);

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
                .getBestProvider(criteria, false));
        latitude_source = TripActivity.latLng_source.latitude;
        longitude_source = TripActivity.latLng_source.longitude;

        latitude_destination = TripActivity.latLng_dest.latitude;
        longitude_destination = TripActivity.latLng_dest.longitude;

        double[] res1 = find_mid_point(latitude_source,longitude_source,latitude_destination,longitude_destination);
        lat_mid_point1 = res1[0];
        lon_mid_point1 = res1[1];

        Log.i("Mid Point 1",""+lat_mid_point1+" "+lon_mid_point1);

        double[] res2 = find_mid_point(latitude_source,longitude_source,lat_mid_point1,lon_mid_point1);
        lat_mid_point2 = res2[0];
        lon_mid_point2 = res2[1];

        double[] res3 = find_mid_point(lat_mid_point1,lon_mid_point1,latitude_destination,longitude_destination);
        lat_mid_point3 = res3[0];
        lon_mid_point3 = res3[1];


        android.location.Location.distanceBetween(latitude_source,longitude_source,latitude_destination,longitude_destination,radius_results);

        radius = radius_results[0]/8;
        Log.i("Radius",""+radius);
        Date currentTime = Calendar.getInstance().getTime();
        String[] str_arr = String.valueOf(currentTime).split(" ");
        char a = str_arr[3].charAt(0);
        char b = str_arr[3].charAt(1);
        String str1 = String.valueOf(a);
        String str2 = String.valueOf(b);
        String current_time_hour_str = str1+str2;
        current_time_hour = Integer.parseInt(current_time_hour_str);
        Log.i("","Current Time"+current_time_hour);


        try {
            List<Address> addresses = geocoder.getFromLocation(latitude_source,longitude_source,1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                Marker marker;
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(address.getAddressLine(0)).draggable(true);
                marker = mMap.addMarker(markerOptions);
                marker.setTitle("Source");
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                marker_source_destination.add(marker);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude_destination,longitude_destination,1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                Marker marker;
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(address.getAddressLine(0)).draggable(true);
                marker =  mMap.addMarker(markerOptions);
                marker.setTitle("Destination");
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                marker_source_destination.add(marker);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mark_stations(radius,lat_mid_point1,lon_mid_point1);
        mark_stations(radius,lat_mid_point2,lon_mid_point2);
        mark_stations(radius,lat_mid_point3,lon_mid_point3);
    }


    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.d(TAG, "onMarkerDragStart: ");
    }

    @Override
    public void onMarkerDrag(Marker marker) {


        Log.d(TAG, "onMarkerDrag: ");
    }

    public void onButtonShowPopupWindowClick(String title) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.popup, null);
        textview_stn_nearby = (TextView) popupView.findViewById(R.id.text_view_test);
        if(!(title.equals("Source")|| title.equals("Destination"))) {
            Log.i("", "TITLE" + title);
            int i = Integer.parseInt(String.valueOf(title.charAt(title.length() - 1)));

            FirebaseDatabase.getInstance().getReference().child("Stations").child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {
                private static final String TAG = "StationFragment";

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                        Log.i("TAG", "map>>>>>: " + map);
                        String nearbyRestaurant_temp = (String) map.get("Nearby");

                        String splitted_array[] = nearbyRestaurant_temp.split("\\$");

                        String nearby1 = splitted_array[0];
                        String nearby2 = splitted_array[1];

                        String name1_splitted[] = nearby1.split(":");
                        String name2_splitted[] = nearby2.split(":");

                        String name1 = name1_splitted[0];
                        String distance1 = name1_splitted[1];

                        String name2 = name2_splitted[0];
                        String distance2 = name2_splitted[1];
                        textview_stn_nearby.setText("Restaurant Recommended"+"\n"+"\n"+"1."+String.valueOf(name1) + "-" + String.valueOf(distance1) + " km away"+'\n' +
                                "2."+String.valueOf(name2) + "-"  + String.valueOf(distance2)+" km away");
                        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        boolean focusable = true;
                        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
                        popupView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                popupWindow.dismiss();
                                return true;
                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


    @Override
    public void onMarkerDragEnd(Marker marker_obj) {

        mMap.clear();
        markerList.clear();
        Marker marker_source = marker_source_destination.get(0);
        Marker marker_destination = marker_source_destination.get(1);

        Log.d(TAG, "onMarkerDragEnd: ");
        String tag = marker_obj.getTitle();
        LatLng latLng_drag = marker_obj.getPosition();
        if(tag.equals("Source")){
            latitude_source = latLng_drag.latitude;
            longitude_source = latLng_drag.longitude;
        }
        else{
            latitude_destination = latLng_drag.latitude;
            longitude_destination = latLng_drag.longitude;
        }



        double[] res1 = find_mid_point(latitude_source,longitude_source,latitude_destination,longitude_destination);
        lat_mid_point1 = res1[0];
        lon_mid_point1 = res1[1];

        Log.i("Mid Point 1",""+lat_mid_point1+" "+lon_mid_point1);

        double[] res2 = find_mid_point(latitude_source,longitude_source,lat_mid_point1,lon_mid_point1);
        lat_mid_point2 = res2[0];
        lon_mid_point2 = res2[1];

        double[] res3 = find_mid_point(lat_mid_point1,lon_mid_point1,latitude_destination,longitude_destination);
        lat_mid_point3 = res3[0];
        lon_mid_point3 = res3[1];


        android.location.Location.distanceBetween(latitude_source,longitude_source,latitude_destination,longitude_destination,radius_results);

        radius = radius_results[0]/8;
        Log.i("Radius",""+radius);
        Date currentTime = Calendar.getInstance().getTime();
        String[] str_arr = String.valueOf(currentTime).split(" ");
        char a = str_arr[3].charAt(0);
        char b = str_arr[3].charAt(1);
        String str1 = String.valueOf(a);
        String str2 = String.valueOf(b);
        String current_time_hour_str = str1+str2;
        current_time_hour = Integer.parseInt(current_time_hour_str);
        Log.i("","Current Time"+current_time_hour);


        try {
            List<Address> addresses = geocoder.getFromLocation(latitude_source,longitude_source,1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                Marker marker;
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(address.getAddressLine(0)).draggable(true);
                marker = mMap.addMarker(markerOptions);
                marker.setTitle("Source");
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                marker_source_destination.add(marker);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude_destination,longitude_destination,1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                Marker marker;
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(address.getAddressLine(0)).draggable(true);
                marker =  mMap.addMarker(markerOptions);
                marker.setTitle("Destination");
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                marker_source_destination.add(marker);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mark_stations(radius,lat_mid_point1,lon_mid_point1);
        mark_stations(radius,lat_mid_point2,lon_mid_point2);
        mark_stations(radius,lat_mid_point3,lon_mid_point3);
   }

}