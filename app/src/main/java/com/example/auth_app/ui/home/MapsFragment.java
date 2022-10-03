package com.example.auth_app.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.fragment.app.Fragment;

import com.example.auth_app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MapsFragment extends Fragment {
    private static final String TAG = "HEAT MAP" ;
    View view;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
//           LatLng sydney = new LatLng(28.7041, 77.1025);
//           googleMap.addMarker(new MarkerOptions().position(sydney).title("Delhi"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            //get latlong for corners for specified place
            LatLng zero = new LatLng(27.2046, 77.4977);
            LatLng one = new LatLng(28.644800, 77.216721);
            LatLng two = new LatLng(28.679079, 77.069710);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            //add them to builder
            //builder.include(zero);
            builder.include(one);
            builder.include(two);

            LatLngBounds bounds = builder.build();

            //get width and height to current display screen
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;

            // 20% padding
            int padding = (int) (width * 0.40);

            //set latlong bounds
            googleMap.setLatLngBoundsForCameraTarget(bounds);

            //move camera to fill the bound to screen
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));

            //set zoom to level to current so that you won't be able to zoom out viz. move outside bounds
            googleMap.setMinZoomPreference(googleMap.getCameraPosition().zoom);
            addHeatMap(googleMap);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_maps, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
    //

    private void addHeatMap(GoogleMap map) {
        List<LatLng> latLngs = null;

        // Get the data: latitude/longitude positions of stations.
        try {
            latLngs = readItems(R.raw.jstations);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Problem reading list of locations.", Toast.LENGTH_LONG).show();
        }
        Log.i(TAG, "latLngs: "+latLngs);
        // Create the gradient.
        int[] colors = {
                Color.rgb(102, 225, 0), // green
                Color.rgb(255, 0, 0)    // red
        };

        float[] startPoints = {
                0.2f, 1f
        };

        Gradient gradient = new Gradient(colors, startPoints);
        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                .data(latLngs)
                .gradient(gradient)
                .build();

        // Add a tile overlay to the map, using the heat map tile provider.
        TileOverlay overlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
    }

    private List<LatLng> readItems(@RawRes int resource) throws Exception {
        List<LatLng> result = new ArrayList<>();
        InputStream inputStream = getContext().getResources().openRawResource(resource);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            result.add(new LatLng(lat, lng));
        }
        return result;
//        for(int i=1;i<=15;i++) {
//        FirebaseDatabase.getInstance().getReference().child("Stations").child(String.valueOf(i)).addValueEventListener(new ValueEventListener(){
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
////
//                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
//                    Log.i(TAG, "map>>>>>: " + map);
//                    double lati = (double) map.get("Latitude");
//                    double longi = (double) map.get("Longitude");
//                    result.add(new LatLng(lati, longi));
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e(TAG, "onCancelled: ");
//            }
//        });
//        }
//        return result;
    }


}