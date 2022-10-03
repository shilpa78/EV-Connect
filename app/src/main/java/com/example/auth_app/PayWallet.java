package com.example.auth_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static com.example.auth_app.ui.profile.Challan.a1s;
import static com.example.auth_app.ui.profile.Challan.pay_number;
import static com.example.auth_app.ui.profile.Challan.payment_done;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auth_app.ui.profile.ProfileData;
import com.example.auth_app.ui.profile.ProfileFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class PayWallet extends AppCompatActivity {
    TextView fine_due,wallet_amt,walletID;
    public int x,y;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firestore;
    Button pay_via_wallet;
    private ProfileData data;
    private String kev_number;
    private String kev_type;
    private String kage;
    private String kemail;
    private String kname;
    private String kcity;
    private String kmfgDate;
    private String kmodel;
    private int kwallet;
    private String krenewalDate;
    private String krenewaldate;
    private String userID;
    public String kusername;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_wallet2);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore= FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        fine_due = (TextView) findViewById(R.id.fine_view);
        pay_via_wallet = (Button)findViewById(R.id.pay_via_wallet_btn);
        walletID = ProfileFragment.wallet_amount;
        wallet_amt = (TextView) findViewById(R.id.amount_view_wallet);
        int u = Integer.parseInt(a1s,10);
        u = u/100;
        fine_due.setText(Integer.toString(u));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot mainSnapshot) {
                ProfileData userProfile = mainSnapshot.getValue(ProfileData.class);
                if(userProfile!=null) {
                    int wallet1 = userProfile.wallet_amount;
//                    String upto=userProfile.regtUpto;
                    if(wallet1!=0) {
                        wallet_amt.setText(Integer.toString(wallet1));
                        //wallet_money = wallet1;
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        Log.i("Fine_due",fine_due.getText().toString());
        pay_via_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int f = Integer.parseInt(fine_due.getText().toString(),10);
//
                Log.i("Fine_due_inside",""+f);
                final int[] w = {Integer.parseInt(wallet_amt.getText().toString(), 10)};
                Log.i("Wallet_inside",""+ w[0]);
                if(f> w[0]){
                    Toast.makeText(PayWallet.this, "Balance Insufficient", Toast.LENGTH_SHORT).show();
                }
                else{
                    AlertDialog dialog = new AlertDialog.Builder(PayWallet.this).setTitle("Payment Confirmation").setMessage("Are you sure you want to pay?")
                            .setPositiveButton("Yes", null)
                            .setNegativeButton("Cancel",null)
                            .show();
                    Button positive_btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    int finalF = f;
                    positive_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Update new Wallet and save in database
                            w[0] -= finalF;
                            wallet_amt.setText(""+ w[0]);
                            //set fine to 0
                            fine_due.setText("0");
                            //Replace user data in database - wallet
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot mainSnapshot) {
                                    ProfileData userProfile = mainSnapshot.getValue(ProfileData.class);
                                    if(userProfile!=null) {
                                        userProfile.setWallet_amount(w[0]);
                                        databaseReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                databaseReference.setValue(userProfile);
                                                Button x = payment_done;
                                                x.setText("Paid!");
                                                x.setEnabled(false);
                                                Toast.makeText(PayWallet.this, "Fine Paid Successfully", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                                pay_via_wallet.setEnabled(false);
                                                //Toast.makeText(getApplicationContext(), "data added", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                //Toast.makeText(getApplicationContext(), "data can't be added" + error, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });



                            //set button to paid and disable it.

                        }
                    });
                }
            }
        });



    }

}