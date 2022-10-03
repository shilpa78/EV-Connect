package com.example.auth_app.ui.EVDetails;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.auth_app.databinding.FragmentProfileFormBinding;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.auth_app.R;
import com.example.auth_app.ui.profile.ProfileData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProfileFormFragment extends Fragment {
    private FragmentProfileFormBinding binding;
    public static EditText policyholder, insuranceamount, RenewalDate, PremiumDate;
    public static Button save;
    private ProfileData data;
    public static EditText regno, model, owner, mfgdate, regupto;
    public DatabaseReference databaseReference;
    public Button saverto;
    FirebaseDatabase firebaseDatabase;
    //    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    private String userID,kev_number,kev_type,kage,kemail,kname,kcity;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public String kusername;


    public ProfileFormFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileFormBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        policyholder = view.findViewById(R.id.policyholdername);
        insuranceamount = view.findViewById(R.id.insamount);
        PremiumDate = view.findViewById(R.id.PremiumDate);
        RenewalDate = view.findViewById(R.id.RenewalDate);
        regno = view.findViewById(R.id.reg_no);
        model = view.findViewById(R.id.model);
        owner = view.findViewById(R.id.ownername);
        mfgdate = view.findViewById(R.id.Mfgdate);
        regupto = view.findViewById(R.id.regupto);
        save = view.findViewById(R.id.save);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        System.out.println(databaseReference);
        data = new ProfileData();

        firebaseAuth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot mainSnapshot) {
                ProfileData userProfile = mainSnapshot.getValue(ProfileData.class);
                if(userProfile!=null) {
                    kusername = userProfile.u_name;
                    kev_number = userProfile.ev_number;
                    kev_type = userProfile.ev_type;
                    kage= userProfile.u_age;
                    kemail= userProfile.u_email;
                   kname= userProfile.u_name;
                   kcity=userProfile.u_city;

                }
            }
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = policyholder.getText().toString();
                String amount = insuranceamount.getText().toString();
                String pdate = PremiumDate.getText().toString();
                String rdate = RenewalDate.getText().toString();
                String regnos = regno.getText().toString();
                String models = model.getText().toString();
                String owners = owner.getText().toString();
                String mfgdates = mfgdate.getText().toString();
                String reguptos = regupto.getText().toString();


                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(pdate) && TextUtils.isEmpty(rdate) && TextUtils.isEmpty(regnos)
                        && TextUtils.isEmpty(models) && TextUtils.isEmpty(owners) && TextUtils.isEmpty(mfgdates)
                        && TextUtils.isEmpty(reguptos)) {
                    Toast.makeText(getContext(), "Please add some data.", Toast.LENGTH_SHORT).show();
                } else {
                    addDatatoFirebase(name, amount, pdate, rdate, regnos, models, owners, mfgdates, reguptos,kusername,kev_number,kev_type,kage,kemail,kname,kcity);
                }
            }
        });
        return view;
    }

    private void addDatatoFirebase(String name, String amount, String pdate, String rdate, String regnos, String models, String owners, String mfgdates, String reguptos,String kusername,String kev_number,String kev_type,String kage,String kemail,String kname,String kcity) {
        data.setU_city(kcity);
        data.setU_PolicyHolder(name);
        data.setU_InsuranceAmount(amount);
        data.setU_PremiumDate(pdate);
        data.setU_RenewalDate(rdate);
        data.setU_RegistrationNo(regnos);
        data.setregtUpto(reguptos);
        data.setmodel(models);
        data.setU_OwnerName(owners);
        data.setMfgDate(mfgdates);
        data.setEv_number(kev_number);
        data.setU_name(kusername);
        data.setU_email(kemail);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                databaseReference.setValue(data);
                Toast.makeText(getContext(), "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "data can't be added" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}


