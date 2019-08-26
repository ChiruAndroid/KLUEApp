package com.volive.klueapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.volive.klueapp.models.SubProductListModel;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.PreferenceUtils;

import java.util.ArrayList;


public class Filters extends AppCompatActivity {

    public static ArrayList<SubProductListModel> product_arrayList;
    Toolbar rl_header;
    ImageView navigation_icon;
    TextView title, pdt_price, pdt_price_off;
    CrystalRangeSeekbar seekbar;
    Button button;
    LinearLayout brand_pg;
    PreferenceUtils preferenceUtils;
    String lowPrice = "";
    String highPrice = "";
    String subcat_id,subcat_ttl, brandList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.filter_screen);
        if (getIntent().getStringExtra("sub_cat_id") != null) {
            brandList = getIntent().getStringExtra("brand_ids");
            subcat_id = getIntent().getStringExtra("sub_cat_id");
            subcat_ttl = getIntent().getStringExtra("sub_cat_ttl");
        }
        initalizeUI();
        initalizeValues();
    }

    private void initalizeUI() {
        rl_header = (Toolbar) findViewById(R.id.header);
        pdt_price = findViewById(R.id.pdt_price);
        pdt_price_off = findViewById(R.id.pdt_price_off);
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        navigation_icon = (ImageView) findViewById(R.id.back_button);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.filters);
        button = (Button) findViewById(R.id.filter_apply);
        brand_pg = (LinearLayout) findViewById(R.id.brand_pg);
        seekbar = findViewById(R.id.rangeSeekbar);
    }

    private void initalizeValues() {
        seekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                pdt_price.setText(minValue.toString());
                pdt_price_off.setText(maxValue.toString());
                lowPrice = minValue.toString();
                highPrice = maxValue.toString();
            }
        });

        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(Filters.this, Anniversary.class);
                intent.putExtra("low_prc", "");
                intent.putExtra("high_prc", "");
                intent.putExtra("brand_ids", "");
                intent.putExtra("title_subcat",subcat_ttl);
                intent.putExtra("id_subcat", subcat_id);
                startActivity(intent);*/
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Filters.this, Anniversary.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("low_prc", lowPrice);
                intent.putExtra("high_prc", highPrice);
                intent.putExtra("brand_ids", brandList);
                intent.putExtra("id_subcat", subcat_id);
                intent.putExtra("title_subcat",subcat_ttl);
                startActivity(intent);
                finish();
            }
        });

        brand_pg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Filters.this, Brand.class);
                intent.putExtra("subcat_id", subcat_id);
                intent.putExtra("title_subcat",subcat_ttl);
                intent.putExtra("brand_ids",brandList);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}