package com.example.auth_app.ui.profile;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auth_app.Profile;
import com.example.auth_app.R;
import com.example.auth_app.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Insurance extends Fragment {

    public static String ren;
    public static String pholder;
    public static String PremiumDate;
    public static Integer amount;
    public Button back;
    public static String renewaldate;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firestore;
    private String userID;
    private FirebaseAuth.AuthStateListener mAuthListener;
    TextView days;
    String remaining;
    long interval;
    TextView renewal, policyamount, premium, policyholdername;

    NotificationManagerCompat nmc;
    Notification nf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.insurance, container, false);
        renewal = (TextView) v.findViewById(R.id.RenewalID);
        policyamount = (TextView) v.findViewById(R.id.InsuranceAmountID);
        premium = (TextView) v.findViewById(R.id.premiumID);
        policyholdername = (TextView) v.findViewById(R.id.policyholderID);
        policyamount = (TextView) v.findViewById(R.id.InsuranceAmountID);
        premium = (TextView) v.findViewById(R.id.premiumID);
        policyholdername = (TextView) v.findViewById(R.id.policyholderID);
        days = (TextView) v.findViewById(R.id.remainingID);
        back=(Button)v.findViewById(R.id.insuranceback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new ProfileFragment()).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot mainSnapshot) {
                ProfileData userProfile = mainSnapshot.getValue(ProfileData.class);
                ren = userProfile.u_RenewalDate;
                renewal.setText(ren);
                    policyamount.setText(userProfile.u_InsuranceAmount);
                    premium.setText(userProfile.u_PremiumDate);
                    policyholdername.setText(userProfile.u_PolicyHolder);

                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    try {
                        String currentDate = simpleDateFormat.format(c);
                        Date date1;
                        Date date2;
                        date1 = simpleDateFormat.parse(currentDate);
                        date2 = simpleDateFormat.parse(ren);
                        long diff = Math.abs(date1.getTime() - date2.getTime());
                        interval = diff / (24 * 60 * 60 * 1000);
                        remaining = Long.toString(interval);
                        days.setText("Remaining Days : " + remaining);
                    } catch (Exception exception) {
//        Toast.makeText(this, "Unable to find difference", Toast.LENGTH_SHORT).show();
                    }
                    if (interval < 10) {
                        //send notification to the user !!
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            NotificationChannel nc = new NotificationChannel("nchannel", "InsurranceNotification", NotificationManager.IMPORTANCE_DEFAULT);
                            NotificationManager NM;
                            NotificationManager notification = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
                            notification.createNotificationChannel(nc);
                        }
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "nchannel");
                        builder.setSmallIcon(android.R.drawable.stat_notify_sync)
                                .setContentTitle("INSURANCE DUE")
                                .setContentText(remaining + " " + "days left");

                        nf = builder.build();
                        nmc = NotificationManagerCompat.from(getActivity());
                        nmc.notify(1, nf);
                    }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
        return v;
}}


