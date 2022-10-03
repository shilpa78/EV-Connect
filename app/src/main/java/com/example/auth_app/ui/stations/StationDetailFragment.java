package com.example.auth_app.ui.stations;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.auth_app.R;
import com.example.auth_app.ui.maps.StationMapActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class StationDetailFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    public TextView sts_detail_num;
    public TextView stn_detail_name;
    public TextView stn_detail_addr;
    public TextView stn_detail_price;
    public TextView stn_detail_rating;
    public double latitude;
    public double longitude;
    public TextView stn_detail_status;
    public TextView stn_detail_opening_time;
    public TextView stn_detail_closing_time;
    public long waitCount = 0;
    public TextView waitCountTextView;
    public Button trackStationBtn, useServiceBtn, serviceDoneBtn;
    public static String name;
    public static double price ;
    public static double lati ;
    public static double longi ;
    public static String address ;
    public static String strtTime ;
    public static String closeTime ;
    public static double rating;
    //    public static long waitCount;
    public static String address_station ;
    public String status;

    public double distance_from_curr_location;
    public int image_station;
    private int mParam1;
    //public StationListItem modelObject;
    public String result;
    public static  final String EXTRA_TEXT = "com.example.auth_app.ui.stations.EXTRA_TEXT";

    public StationDetailFragment(String result) {

        this.result = result;

    }



//    public static StationDetailFragment newInstance(int param1) {
//        StationDetailFragment fragment = new StationDetailFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_PARAM1, param1);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_station_detail, container, false);
        stn_detail_name  = view.findViewById(R.id.station_name);
        stn_detail_addr = view.findViewById(R.id.station_address);
        stn_detail_price = view.findViewById(R.id.station_price);
        stn_detail_rating = view.findViewById(R.id.station_rating);
        stn_detail_status = view.findViewById(R.id.station_status);
        stn_detail_opening_time = view.findViewById(R.id.station_opening_time);
        stn_detail_closing_time = view.findViewById(R.id.station_closing_time);
        waitCountTextView = view.findViewById(R.id.waitCountTextView);
        //Splitting result
        String[] res_arr = result.split("\\$");


        Log.i("Result>>>>>>>>>>>",res_arr[1]+res_arr[4]+res_arr[5]+" "+res_arr[10]);
        stn_detail_name.setText(res_arr[1]);
        stn_detail_addr.setText("" + res_arr[2]);
        stn_detail_price.setText("Price: " + String.valueOf(res_arr[3]));
        stn_detail_rating.setText("Rating: " + String.valueOf(res_arr[4]));
        stn_detail_status.setText(""+res_arr[7]);
        stn_detail_opening_time.setText("Opens at : "+res_arr[8]);
        stn_detail_closing_time.setText("Closes at : "+res_arr[9]);
        waitCountTextView.setText(res_arr[10]);

        longitude = Double.parseDouble(res_arr[5]);
        latitude = Double.parseDouble(res_arr[6]);

        String res = longitude+"$"+latitude;

        trackStationBtn = view.findViewById(R.id.station_track);
        trackStationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent serviceIntent = new Intent(getActivity(), StationMapActivity.class);
                serviceIntent.putExtra(EXTRA_TEXT,res);
                startActivity(serviceIntent);

            }
        });

        useServiceBtn = view.findViewById(R.id.useServiceBtn);
        final String[] key = new String[1];
        FirebaseDatabase.getInstance().getReference("Stations").orderByChild("Name").equalTo(res_arr[1]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    key[0] = childSnapshot.getKey();
                    Log.i("Key *****************", key[0]);
                }



                FirebaseDatabase.getInstance().getReference("Stations").child(key[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                    private static final String TAG = "StationDetailFragment";

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
//
                            Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                            name = (String) map.get("Name");
                            price = (double) map.get("Price");
                            lati = (double) map.get("Latitude");
                            longi = (double) map.get("Longitude");
                            address = (String) map.get("Address");
                            strtTime = (String) map.get("Starting Time");
                            closeTime = (String) map.get("Closing Time");
                            rating = (double) map.get("Rating");
                            waitCount = (long) map.get("Wait Count");
                            waitCountTextView.setText(String.valueOf(waitCount));

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){

            }
        });
        useServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String[] key = new String[1];
                FirebaseDatabase.getInstance().getReference("Stations").orderByChild("Name").equalTo(res_arr[1]).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            key[0] = childSnapshot.getKey();
                            Log.i("Key *****************", key[0]);
                        }



                        FirebaseDatabase.getInstance().getReference("Stations").child(key[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                            private static final String TAG = "StationDetailFragment";

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
//
                                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                                    name = (String) map.get("Name");
                                    price = (double) map.get("Price");
                                    lati = (double) map.get("Latitude");
                                    longi = (double) map.get("Longitude");
                                    address = (String) map.get("Address");
                                    strtTime = (String) map.get("Starting Time");
                                    closeTime = (String) map.get("Closing Time");
                                    rating = (double) map.get("Rating");
                                    waitCount = (long) map.get("Wait Count");
                                    waitCount += 1;
                                    FirebaseDatabase.getInstance().getReference("Stations").child(key[0]).child("Wait Count").setValue(waitCount);
                                    waitCountTextView.setText(String.valueOf(waitCount));


                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }

                        });
                    }
                    @Override
                    public void onCancelled (@NonNull DatabaseError error){

                    }
                });

            }
        });

        serviceDoneBtn = view.findViewById(R.id.serviceDoneBtn);
        serviceDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] key = new String[1];
                FirebaseDatabase.getInstance().getReference("Stations").orderByChild("Name").equalTo(res_arr[1]).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            key[0] = childSnapshot.getKey();
                            Log.i("Key *****************", key[0]);
                        }



                        FirebaseDatabase.getInstance().getReference("Stations").child(key[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                            private static final String TAG = "StationDetailFragment";

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
//
                                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                                    name = (String) map.get("Name");
                                    price = (double) map.get("Price");
                                    lati = (double) map.get("Latitude");
                                    longi = (double) map.get("Longitude");
                                    address = (String) map.get("Address");
                                    strtTime = (String) map.get("Starting Time");
                                    closeTime = (String) map.get("Closing Time");
                                    rating = (double) map.get("Rating");
                                    waitCount = (long) map.get("Wait Count");
                                    waitCount -= 1;
                                    FirebaseDatabase.getInstance().getReference("Stations").child(key[0]).child("Wait Count").setValue(waitCount);
                                    Log.i("TAG", "MAP VALUES --------------> "+map);
                                    waitCountTextView.setText(String.valueOf(waitCount));


                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }

                        });
                    }
                    @Override
                    public void onCancelled (@NonNull DatabaseError error){

                    }
                });

            }
        });
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
//        String wc = prefs.getString("wait count "+res_arr[1], "");
//        waitCountTextView.setText(wc);
//        Log.i("HEREREEEE",StationFragment.name);
        //sts_detail_num.setText(String.valueOf(sts_number));
        return view;
    }
}