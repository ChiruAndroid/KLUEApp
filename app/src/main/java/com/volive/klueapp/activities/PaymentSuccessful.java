package com.volive.klueapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.volive.klueapp.R;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.PreferenceUtils;


public class PaymentSuccessful extends AppCompatActivity {

    Button payment;
    ImageView imageView;
    TextView tv_message,tv_price_type,tv_order_price,tv_order_id,tv_payment_type,tv_transaction_time;
    PreferenceUtils preferenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.payment_successful);
        preferenceUtils=new PreferenceUtils(PaymentSuccessful.this);
        initializeUI();
        if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency,"").equalsIgnoreCase(Constants.SAR)){
            tv_price_type.setText(Constants.SAR);
        }else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency,"").equalsIgnoreCase(Constants.AED)){
            tv_price_type.setText(Constants.AED);
        }else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency,"").equalsIgnoreCase(Constants.USD)){
            tv_price_type.setText(Constants.USD);
        }else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency,"").equalsIgnoreCase(Constants.KWD)){
            tv_price_type.setText(Constants.KWD);
        }
        tv_message.setText(getIntent().getStringExtra("message"));
        tv_order_price.setText(getIntent().getStringExtra("total_amount"));
        tv_order_id.setText(getIntent().getStringExtra("order_id"));
        tv_payment_type.setText(getIntent().getStringExtra("payment_type"));
        tv_transaction_time.setText(getIntent().getStringExtra("order_date"));

    }

    private void initializeUI() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        tv_message = findViewById(R.id.tv_message);
        tv_price_type = findViewById(R.id.tv_price_type);
        tv_order_price = findViewById(R.id.tv_order_price);
        tv_order_id = findViewById(R.id.tv_order_id);
        tv_payment_type = findViewById(R.id.tv_payment_type);
        tv_transaction_time = findViewById(R.id.tv_transaction_time);

        payment = (Button) findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentSuccessful.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        imageView = (ImageView) findViewById(R.id.close);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentSuccessful.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        Intent intent = new Intent(PaymentSuccessful.this, MainActivity.class);
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);
    }
}
