package com.example.auth_app.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auth_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;


public class FastTag extends Fragment {
    public Integer fasttagamount;
    public Button back;
    TextView fastamount;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firestore;
    private String userID;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference reference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_fast_tag, container, false);
        back=(Button)v.findViewById(R.id.fasttagback);
        fastamount=(TextView)v.findViewById(R.id.fastamountID);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore= FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new ProfileFragment()).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
       databaseReference.addValueEventListener(new ValueEventListener(){
           @Override
           public void onDataChange(DataSnapshot mainSnapshot) {
               ProfileData userProfile = mainSnapshot.getValue(ProfileData.class);
               String fast=userProfile.u_FastTagBalance;
               if(fast!=null) {
                   fastamount.setText(fast);
               }
           }
           @Override
           public void onCancelled(DatabaseError databaseError) {
           }
       });





        return v;
    }
}