package com.example.auth_app.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auth_app.R;
import com.example.auth_app.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;


public class RTO extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firestore;
    private String userID;
    public Button back;
    private FirebaseAuth.AuthStateListener mAuthListener;
    TextView reg_no,owner_name,model,mfg_dt,reg_upto;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.rto, container, false);
        reg_no=(TextView)v.findViewById(R.id.reg_no);
        reg_upto=(TextView)v.findViewById(R.id.reg_upto);
        owner_name=(TextView)v.findViewById(R.id.owner_name);
        model=(TextView)v.findViewById(R.id.model_name);
        mfg_dt=(TextView)v.findViewById(R.id.mfg_date);
        back=(Button)v.findViewById(R.id.rtoback);
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
        firestore= FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot mainSnapshot) {
                ProfileData userProfile = mainSnapshot.getValue(ProfileData.class);
                String ren=userProfile.model;
                    reg_no.setText(userProfile.u_RegistrationNo);
                    reg_upto.setText(userProfile.regtUpto);
                    owner_name.setText(userProfile.u_OwnerName);
                    model.setText(userProfile.model);
                    mfg_dt.setText(userProfile.mfgDate);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return v;
    }
}