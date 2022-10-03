package com.example.auth_app.ui.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.auth_app.R;
import com.example.auth_app.User;
import com.example.auth_app.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firestore;
    private FragmentProfileBinding binding;
    @SuppressLint("StaticFieldLeak")
    private static ProfileFragment instance;
    View view;
    Button rto,insurance,fastag,challan;
    @SuppressLint("StaticFieldLeak")
    public static TextView profile_name,wallet_amount;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private String userID;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        instance = this;
        binding=FragmentProfileBinding.inflate(inflater,container,false);
        View view=binding.getRoot();

        rto = (Button) view.findViewById(R.id.RTOID);
        insurance=(Button) view.findViewById(R.id.InsuranceID);
        fastag=(Button)view.findViewById(R.id.FastTagID);
        challan=(Button) view.findViewById(R.id.ChallanID);
        profile_name=(TextView)view.findViewById(R.id.profileID);
        wallet_amount=(TextView)view.findViewById(R.id.walletID);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot mainSnapshot) {
                ProfileData userProfile = mainSnapshot.getValue(ProfileData.class);
                if(userProfile!=null) {
                    int wallet1 = userProfile.wallet_amount;
                    String upto=userProfile.regtUpto;
                    if(wallet1!=0) {
                        wallet_amount.setText("Rs. " + wallet1);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        rto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rto.setVisibility(View.GONE);
                insurance.setVisibility(View.GONE);
                fastag.setVisibility(View.GONE);
                challan.setVisibility(View.GONE);
                profile_name.setVisibility(View.GONE);
                wallet_amount.setVisibility(View.GONE);

                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new RTO()).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        insurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rto.setVisibility(View.GONE);
                insurance.setVisibility(View.GONE);
                fastag.setVisibility(View.GONE);
                challan.setVisibility(View.GONE);
                profile_name.setVisibility(View.GONE);
                wallet_amount.setVisibility(View.GONE);

                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new Insurance()).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        fastag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rto.setVisibility(View.GONE);
                insurance.setVisibility(View.GONE);
                fastag.setVisibility(View.GONE);
                challan.setVisibility(View.GONE);
                profile_name.setVisibility(View.GONE);
                wallet_amount.setVisibility(View.GONE);

                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new FastTag()).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        challan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rto.setVisibility(View.GONE);
                insurance.setVisibility(View.GONE);
                fastag.setVisibility(View.GONE);
                challan.setVisibility(View.GONE);
                profile_name.setVisibility(View.GONE);
                wallet_amount.setVisibility(View.GONE);

                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new Challan()).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }
    public void changeFragmentTextView(String s) {
        Fragment frag = getFragmentManager().findFragmentById(R.id.container);
        ((TextView) frag.getView().findViewById(R.id.walletID)).setText(s);
    }
    public static ProfileFragment GetInstance()
    {
        return instance;
    }
}