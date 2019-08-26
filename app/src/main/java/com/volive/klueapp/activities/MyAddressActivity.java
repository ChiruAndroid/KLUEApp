package com.volive.klueapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.volive.klueapp.R;
import com.volive.klueapp.utils.MyAddressHelper;
import com.volive.klueapp.utils.PreferenceUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MyAddressActivity extends BaseActivity {

    Toolbar rl_header;
    ImageView navigation_icon;
    TextView title;
    RecyclerView recyclerView;
    LinearLayout addnewaddress;
    MyAddressHelper addressHelper;
    PreferenceUtils preferenceUtils;
    LinearLayout nav_home,nav_top_pics,nav_categories,nav_whishlist,nav_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_address);
        intializeUI();
        preferenceUtils = new PreferenceUtils(MyAddressActivity.this);

        addressHelper = new MyAddressHelper(MyAddressActivity.this);
        recyclerView = findViewById(R.id.rview_myAddress);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        if (!preferenceUtils.getbooleanFromPreference(preferenceUtils.LOG_OUT, true)) {
            addressHelper.getShippingAddressService(MyAddressActivity.this, recyclerView);
        } else {
            showDialog(MyAddressActivity.this, SweetAlertDialog.WARNING_TYPE, "", "Please Login,to go to Address Page.", "OK!",
                    new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            Intent intent = new Intent(MyAddressActivity.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        }
      /*  if (checkConnection()) {
            addressHelper.getShippingAddressService(MyAddressActivity.this, recyclerView);
        }else
            Toast.makeText(MyAddressActivity.this, "Please check Internet conncetion.", Toast.LENGTH_SHORT).show();
*/
    }

    private void intializeUI() {
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
        title.setText(getResources().getString(R.string.my_address));

        addnewaddress = findViewById(R.id.addnewaddress);
        addnewaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAddressActivity.this, AddNewAddress.class);
                startActivity(intent);
            }
        });


        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        addressHelper.getShippingAddressService(MyAddressActivity.this, recyclerView);

    }
}