package com.example.auth_app.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.auth_app.PaymentGateway;
import com.example.auth_app.R;
import com.example.auth_app.ui.EVDetails.ProfileFormFragment;


public class Challan extends Fragment {


    public Button back;
    public Button pay1b,pay2b,pay3b;
    public int amount;
    public int a1i,a2i,a3i;
    public static String a1s,a2s,a3s;
    public static int pay_number;
    public boolean b1=false,b2=false,b3=false;
    public static Button payment_done;
    public static TextView a1,a2,a3;
    public boolean bx1,bx2,bx3;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        boolean bb1=false,bb2=false,bb3=false;
//        if(savedInstanceState!=null){
//
//            bb1 = savedInstanceState.getBoolean("Button_1");
//            bb2 = savedInstanceState.getBoolean("Button_2");
//            bb3 = savedInstanceState.getBoolean("Button_3");
//        }
//        if(bb1){
//            pay1b.setText("Paid");
//            pay1b.setEnabled(false);
//        }
//        if(bb2){
//            pay2b.setText("Paid");
//            pay2b.setEnabled(false);
//        }
//        if(bb3){
//            pay3b.setText("Paid");
//            pay3b.setEnabled(false);
//        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("State being saved", String.valueOf(b1));
        outState.putBoolean("Button_1",b1);
        outState.putBoolean("Button_2",b2);
        outState.putBoolean("Button_3",b3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_challan, container, false);
        back=(Button)view.findViewById(R.id.challanback);
        pay1b=(Button)view.findViewById(R.id.pay1);
        pay2b=(Button)view.findViewById(R.id.pay2);
        pay3b=(Button)view.findViewById(R.id.pay3);
        a1=(TextView)view.findViewById(R.id.amount1);
        a2=(TextView)view.findViewById(R.id.amount2);
        a3=(TextView)view.findViewById(R.id.amount3);
        pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = pref.edit();

//        boolean bb1=false,bb2=false,bb3=false;
//        if(savedInstanceState!=null){
//            bb1 = savedInstanceState.getBoolean("Button_1");
//            bb2 = savedInstanceState.getBoolean("Button_2");
//            bb3 = savedInstanceState.getBoolean("Button_3");
//        }
//        if(bb1){
//            pay1b.setText("Paid");
//            pay1b.setEnabled(false);
//        }
//        if(bb2){
//            pay2b.setText("Paid");
//            pay2b.setEnabled(false);
//        }
//        if(bb3){
//            pay3b.setText("Paid");
//            pay3b.setEnabled(false);
//        }
        a1.setText("2000");
        a2.setText("500");
        a3.setText("1000");
        bx1 = pref.getBoolean("Btn_1",false);
        bx2 = pref.getBoolean("Btn_2",false);
        bx3 = pref.getBoolean("Btn_3",false);

        if(bx1){
            pay1b.setText("Paid!");
            pay1b.setEnabled(false);
        }
        if(bx2){
            pay2b.setText("Paid!");
            pay2b.setEnabled(false);
        }
        if(bx3){
            Log.i("Button Retrieved",String.valueOf(bx3));
            pay3b.setText("Paid!");
            pay3b.setEnabled(false);
        }

        pay1b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a1s = a1.getText().toString();  //textview value to string
                a1i = Integer.parseInt(a1s);    //string to integer
                a1i=a1i*100;                    //multiply by 100
                a1s=String.valueOf(a1i);        // converting the updated value to string.
                pay_number=1;
                b1=true;
                editor.putBoolean("Btn_1",b1);
                editor.apply();
                payment_done = pay1b;
                Intent intent = new Intent(getActivity(), PaymentGateway.class);
                startActivity(intent);
            }
        });
        pay2b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a1s = a2.getText().toString();
                a1i = Integer.parseInt(a1s);    //string to integer
                a1i=a1i*100;                    //multiply by 100
                a1s=String.valueOf(a1i);        // converting the updated value to string.
                b2=true;
                editor.putBoolean("Btn_2",b2);
                editor.apply();
                pay_number=2;
                payment_done = pay2b;
                Intent intent = new Intent(getActivity(), PaymentGateway.class);
                startActivity(intent);
            }


        });
        pay3b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a1s = a3.getText().toString();
                a1i = Integer.parseInt(a1s);    //string to integer
                a1i=a1i*100;                    //multiply by 100
                a1s=String.valueOf(a1i);        // converting the updated value to string.
                b3=true;
                editor.putBoolean("Btn_3",b3);
                editor.apply();
                Log.i("Button put",String.valueOf(b3));
                pay_number=3;
                payment_done = pay3b;

                Intent intent = new Intent(getActivity(), PaymentGateway.class);
                startActivity(intent);
            }



        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new ProfileFragment()).addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bx1 = pref.getBoolean("Btn_1",false);
        bx2 = pref.getBoolean("Btn_2",false);
        bx3 = pref.getBoolean("Btn_3",false);

        if(bx1){
            pay1b.setText("Paid!");
            pay1b.setEnabled(false);
        }
        if(bx2){
            pay2b.setText("Paid!");
            pay2b.setEnabled(false);
        }
        if(bx3){
            Log.i("Button Retrieved",String.valueOf(bx3));
            pay3b.setText("Paid!");
            pay3b.setEnabled(false);
        }
    }
}