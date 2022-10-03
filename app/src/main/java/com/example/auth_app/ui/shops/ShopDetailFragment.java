package com.example.auth_app.ui.shops;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.auth_app.R;
import com.example.auth_app.ui.maps.ShopMapActivity;
import com.example.auth_app.ui.maps.StationMapActivity;

public class ShopDetailFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    public TextView shop_detail_num;
    public TextView shop_detail_name;
    public TextView shop_detail_addr;
    public TextView shop_detail_price;
    public TextView shop_detail_rating;
    public double latitude;
    public double longitude;
    public TextView shop_detail_status;
    public TextView shop_detail_opening_time;
    public TextView shop_detail_closing_time;
    public Button trackShopBtn;
    private int mParam1;
    //public ShopListItem modelObject;
    public String result;
    public static  final String EXTRA_TEXT = "com.example.auth_app.ui.shops.EXTRA_TEXT";

    public ShopDetailFragment(String result) {

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
        View view =  inflater.inflate(R.layout.fragment_shop_detail, container, false);
        shop_detail_name  = view.findViewById(R.id.shop_name);
        shop_detail_addr = view.findViewById(R.id.shop_address);
        shop_detail_price = view.findViewById(R.id.shop_price);
        shop_detail_rating = view.findViewById(R.id.shop_rating);
        shop_detail_status = view.findViewById(R.id.shop_status);
        shop_detail_opening_time = view.findViewById(R.id.shop_opening_time);
        shop_detail_closing_time = view.findViewById(R.id.shop_closing_time);

        //Splitting result
        String[] res_arr = result.split("\\$");


        Log.i("Result",res_arr[0]);
        shop_detail_name.setText(res_arr[1]);
        shop_detail_addr.setText("" + res_arr[2]);
        shop_detail_price.setText("Contact No. : " + String.valueOf(res_arr[9]));
        shop_detail_rating.setText("Rating: " + String.valueOf(res_arr[3]));
        shop_detail_status.setText(""+res_arr[6]);
        shop_detail_opening_time.setText("Opens at : "+res_arr[7]);
        shop_detail_closing_time.setText("Closes at : "+res_arr[8]);

        longitude = Double.parseDouble(res_arr[4]);
        latitude = Double.parseDouble(res_arr[5]);

        String res = longitude+"$"+latitude;

        trackShopBtn = view.findViewById(R.id.shop_track);

        trackShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent serviceIntent = new Intent(getActivity(), ShopMapActivity.class);
                serviceIntent.putExtra(EXTRA_TEXT,res);
                startActivity(serviceIntent);

            }
        });

        Log.i("HEREREEEE",ShopFragment.name);
        //sts_detail_num.setText(String.valueOf(sts_number));;
        return view;
    }
}