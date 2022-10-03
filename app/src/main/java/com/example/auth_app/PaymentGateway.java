package com.example.auth_app;

import static com.example.auth_app.ui.profile.Challan.a1;
import static com.example.auth_app.ui.profile.Challan.a1s;
import static com.example.auth_app.ui.profile.Challan.a2;
import static com.example.auth_app.ui.profile.Challan.a3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auth_app.ui.profile.Challan;
import com.example.auth_app.ui.profile.RTO;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentGateway extends AppCompatActivity implements PaymentResultListener  {
    Button pay1b,pay2b,pay3b;
    TextView paymsgtv;
    Button backchallan;
    Button pay_via_wallet;
    Button pay;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);

        pay_via_wallet = (Button)findViewById(R.id.pay_wallet_btn);
        paymsgtv=(TextView)findViewById(R.id.paymsg);
        backchallan=(Button) findViewById(R.id.gobackchallan);
        pay=(Button)findViewById(R.id.paybutton);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment1();
            }
        });

        pay_via_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PaymentGateway.this,PayWallet.class));
            }
        });

        backchallan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 1) {
                    pay1b.setText("Paid");
                    getSupportFragmentManager().beginTransaction().replace(R.id.repid, new Challan()).commit();
                    backchallan.setVisibility(View.INVISIBLE);
                    pay.setVisibility(View.INVISIBLE);
                    paymsgtv.setVisibility(View.INVISIBLE);
                    pay_via_wallet.setVisibility(View.INVISIBLE);

                    // remove challan!!
                }
                else if (flag == 0) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.repid, new Challan()).commit();
                    backchallan.setVisibility(View.INVISIBLE);
                    pay.setVisibility(View.INVISIBLE);
                    paymsgtv.setVisibility(View.INVISIBLE);
                    pay_via_wallet.setVisibility(View.INVISIBLE);
                    // remove challan!!
                }
            }

        });

    }

    public void startPayment1() {

        Checkout checkout = new Checkout();

        checkout.setImage(R.mipmap.ic_launcher);

        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();
            options.put("name", R.string.app_name);
            options.put("description", "Payment for Challan");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", false);

            //You can omit the image option to fetch the image from dashboard
            options.put("currency", "INR");
            options.put("amount", a1s);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "ekta21025@iiitd.ac.in");
            preFill.put("contact", "8383077147");

            options.put("prefill", preFill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String s)
    {
        flag=1;
        paymsgtv.setText("Payment Successful " +"\n"+"Payment ID: " +s);
//        backchallan.setVisibility(View.VISIBLE);
        pay.setVisibility(View.INVISIBLE);
//        backchallan.setText(s);
        pay_via_wallet.setVisibility(View.INVISIBLE);

        // call fragment challan and remove tv1 and paynow button

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Not  Successful " + s, Toast.LENGTH_SHORT);

    }
}