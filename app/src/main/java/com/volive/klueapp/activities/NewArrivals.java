package com.volive.klueapp.activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.volive.klueapp.fragments.Home;
import com.volive.klueapp.R;
import com.volive.klueapp.adpaters.NewArrivalAdapter;

public class NewArrivals extends BaseActivity {

    RecyclerView recyclerView;
    ImageView back_button;
    TextView tv_title;
    Toolbar rl_header;
    NewArrivalAdapter arrivalAdapter;

    LinearLayout nav_home,nav_top_pics,nav_categories,nav_whishlist,nav_account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_new_arrivals);
        initializeUI();
        if (checkConnection()) {
           /* if (getIntent().getExtras() != null) {
                new_arrival_list = (ArrayList<NewArrivalModel>) getIntent().getSerializableExtra("arraivals");
            }*/
            arrivalAdapter = new NewArrivalAdapter(this, Home.new_arrival_list, "New Arrivals");
            recyclerView.setAdapter(arrivalAdapter);
        }else
            Toast.makeText(NewArrivals.this, R.string.plz_check_internet_connection, Toast.LENGTH_SHORT).show();
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
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.new_arrivals);
        recyclerView = findViewById(R.id.rview_new_arrivals);
        GridLayoutManager gManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gManager);
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (arrivalAdapter!=null)
            arrivalAdapter.notifyDataSetChanged();
    }
}