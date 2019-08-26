package com.volive.klueapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.volive.klueapp.R;
import com.volive.klueapp.utils.MyAddressHelper;
import com.volive.klueapp.utils.PreferenceUtils;
import com.volive.klueapp.adpaters.AddressAdapter;


public class OrderSummary extends BaseActivity {
    RecyclerView recyclerView;
    LinearLayout addnewaddress;
    Button button;
    Toolbar rl_header;
    ImageView navigation_icon;
    TextView title;
    PreferenceUtils preferenceUtils;
    MyAddressHelper addressHelper;
    String msg, note, img_path;
    LinearLayout nav_home,nav_top_pics,nav_categories,nav_whishlist,nav_account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.order_summary);

        img_path = getIntent().getStringExtra("img_path");
        msg = getIntent().getStringExtra("msg");
        note = getIntent().getStringExtra("note");

        initializeUI();
        addressHelper = new MyAddressHelper(OrderSummary.this);
        recyclerView = (RecyclerView) findViewById(R.id.rview_address_list);
        LinearLayoutManager lManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lManager);
        if (checkConnection()) {
            addressHelper.getShippingAddressService(OrderSummary.this, recyclerView);
        } else
            Toast.makeText(OrderSummary.this, R.string.plz_check_internet_connection, Toast.LENGTH_SHORT).show();
    }

    private void initializeUI() {
        nav_home=findViewById(R.id.nav_home);
        nav_top_pics=findViewById(R.id.nav_top_pics);
        nav_categories=findViewById(R.id.nav_categories);
        nav_whishlist=findViewById(R.id.nav_whishlist);
        nav_account=findViewById(R.id.nav_account);
        nav_home.setOnClickListener(this);
        nav_top_pics.setOnClickListener(this);
        nav_categories.setOnClickListener(this);
        nav_whishlist.setOnClickListener(this);
        nav_account.setOnClickListener(this);

        rl_header = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        navigation_icon = (ImageView) findViewById(R.id.back_button);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.order_summary);

        addnewaddress = (LinearLayout) findViewById(R.id.addnewaddress);
        button = (Button) findViewById(R.id.proceed_pay);
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addnewaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderSummary.this, AddNewAddress.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AddressAdapter.addressPosition != -1) {
                    Intent intent = new Intent(OrderSummary.this, ShippingMethodsActivity.class);
                    intent.putExtra("img_path", img_path);
                    intent.putExtra("msg", msg);
                    intent.putExtra("note", note);
                    startActivity(intent);
                } else {
                    Toast.makeText(OrderSummary.this, getResources().getString(R.string.please_select_address), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        addressHelper.getShippingAddressService(OrderSummary.this, recyclerView);
    }
}