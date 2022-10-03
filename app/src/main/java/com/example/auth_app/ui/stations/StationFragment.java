package com.example.auth_app.ui.stations;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auth_app.R;
import com.example.auth_app.databinding.FragmentStationBinding;
import com.example.auth_app.ui.maps.StationMapActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class StationFragment extends Fragment implements StationAdapter.ItemClickListener{

    private static final long MIN_TIME_BW_UPDATES = 10;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000*60*1;
    // private static final Object LOCATION_SERVICE = null;
    private FragmentStationBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public ArrayList<StationListItem> stationListItems;
    public static  final String EXTRA_TEXT = "com.example.auth_app.ui.stations.EXTRA_TEXT";
    public static String name;
    public static double price ;
    public static double lati ;
    public static double longi ;
    public static String address ;
    public static String strtTime ;
    public static String closeTime ;
    public static double rating;
    public boolean checkGPS;
    public boolean checkNetwork;
    Context mContext;
    FusedLocationProviderClient client;
    double latitude_current_loc;
    double longitude_current_loc;
    private boolean canGetLocation;
    public float[] results = new float[2];
    public int current_time_hour;
    public String status;
    public double distance_from_curr_location;
    public int image_station;
    public static long waitCount = 0;
    public int k=0;
    // public static String url;
//    @Override
//    public void onStart() {
//        super.onStart();
//        LocationRequest mLocationRequest = LocationRequest.create();
//        mLocationRequest.setInterval(60000);
//        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        LocationCallback mLocationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult == null) {
//                    Toast.makeText(getContext(), "Please enable location services", Toast.LENGTH_SHORT).show();
//                }
//                for (Location location : locationResult.getLocations()) {
//                    if (location != null) {
//                        nearbyStations();
//                    }
//                }
//            }
//        };
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
//    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StationViewModel stationViewModel =
                new ViewModelProvider(this).get(StationViewModel.class);

        binding = FragmentStationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = (RecyclerView) root.findViewById(R.id.stationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        stationListItems = new ArrayList<>();

        //Accessing current location
        client = LocationServices.getFusedLocationProviderClient(getContext());
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        nearbyStations();
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();


        //Log.i("","Current Location: "+latitude_current_loc + ", "+ longitude_current_loc);

//        for(int i = 1; i <= 5; i++) {
//            StationListItem stationlistItem = new StationListItem(i,"PlugNgo Charging Station "+i,28.621734973925065,77.29355392625062,20.10,"9:00 am","18:00 pm",4,"PlugNgo Charging Station, Mother Dairy Marg, I.P.Extension, Mandawali, New Delhi, Delhi, India");
//            stationListItems.add(stationlistItem);
//        }

        //Log.i("TAG", "JJKKK>>>>>: "+stationlistItem.getStation_name());

//        Log.i("TAG", "JJKKK>>>>>: "+stationListItems.get(0));
//        adapter = new StationAdapter(stationListItems,this,this);
//        recyclerView.setAdapter(adapter);

        return root;
    }

    public void nearbyStations(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                try {
                    latitude_current_loc = location.getLatitude();
                    longitude_current_loc = location.getLongitude();
                    Date currentTime = Calendar.getInstance().getTime();
                    String[] str_arr = String.valueOf(currentTime).split(" ");
                    char a = str_arr[3].charAt(0);
                    char b = str_arr[3].charAt(1);
                    String str1 = String.valueOf(a);
                    String str2 = String.valueOf(b);
                    String current_time_hour_str = str1+str2;
                    current_time_hour = Integer.parseInt(current_time_hour_str);
                    Log.i("","Current Time"+current_time_hour);

                } catch (Exception e) {
                    Toast.makeText(getContext(), "Please enable location services", Toast.LENGTH_SHORT).show();
                }
                Log.i("", "Current Location : " + latitude_current_loc + ", " + longitude_current_loc);
                int[] images_station = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.f,R.drawable.g,R.drawable.h,R.drawable.i,R.drawable.j,R.drawable.k,R.drawable.l,R.drawable.m,R.drawable.n,R.drawable.o,R.drawable.p,R.drawable.q,R.drawable.r,R.drawable.s};
                // int k=0;
                //Database work
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
                                address = (String) map.get("Address");
                                strtTime = (String) map.get("Starting Time");
                                closeTime = (String) map.get("Closing Time");
                                rating = (double) map.get("Rating");
                                waitCount = (long) map.get("Wait Count");
                                //url = (String) map.get("img-url");
//               Log.i("TAG", "MAPPPPPP>>>>>: "+map);
//                                Log.i("TAG", "KRKRKRKRK>>>>>: " + rating);
                                android.location.Location.distanceBetween(latitude_current_loc,longitude_current_loc,lati,longi,results);
                                Log.i("", "Current Location : " + latitude_current_loc + ", " + longitude_current_loc + ", "+ results[0]);
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
                                if(results[0]<=10000.0) {
                                    distance_from_curr_location = results[0]/1000;
                                    String temp = String.format("%.2f",distance_from_curr_location);
                                    distance_from_curr_location = Double.parseDouble(temp);

                                    //Log.i("","K value: "+finalK);
                                    stationListItems.add(new StationListItem(finalI, name, lati, longi, price, strtTime, closeTime, rating, address, distance_from_curr_location, status,image_station, waitCount));
                                }
                            }
                            adapter = new StationAdapter(stationListItems, StationFragment.this, StationFragment.this);
                            recyclerView.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }




            }
        });
    }
    //    public void getmylocation() {
//
//    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(StationListItem modelObject) {

        int station_num = modelObject.getStation_num();
        String station_name = modelObject.getStation_name();
        String address = modelObject.getAddress();
        double price = modelObject.getPrice();
        double rating = modelObject.getRatings();
        double latitude = modelObject.getLati();
        double longitude = modelObject.getLongi();
        String status = modelObject.getStatus();
        String opening_time = modelObject.getStrt_time();
        String closing_time = modelObject.getStop_time();
//        long waitCount = modelObject.getWaitCount();
        String res = station_num+"$"+station_name+"$"+address+"$"+price+"$"+rating+"$"+latitude+"$"+longitude+"$"+status+"$"+opening_time+"$"+closing_time+"$"+waitCount;

        Intent serviceIntent = new Intent(getActivity(), StationDetailsActivity.class);
        serviceIntent.putExtra(EXTRA_TEXT,res);
        startActivity(serviceIntent);
    }
}