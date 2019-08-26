package com.volive.klueapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.volive.klueapp.models.BrandModel;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.API_class;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.PreferenceUtils;
import com.volive.klueapp.utils.Retrofit_funtion_class;
import com.volive.klueapp.adpaters.BrandItemAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Brand extends AppCompatActivity {

    public static ArrayList<BrandModel> brand_list_array;
    Toolbar rl_header;
    ImageView navigation_icon;
    TextView title;
    Button brand_save;
    PreferenceUtils preferenceUtils;
    RecyclerView recyclerView;
    String subcat_id;
    BrandItemAdapter adapter;
    String ids,subcat_ttl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.brand);
        if (getIntent().getStringExtra("subcat_id") != null) {
            subcat_id = getIntent().getStringExtra("subcat_id");
            subcat_ttl = getIntent().getStringExtra("title_subcat");
        }
        initializeUI();
        brandsService();
    }

    private void initializeUI() {
        rl_header = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        navigation_icon = (ImageView) findViewById(R.id.back_button);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.brand);

        brand_save = findViewById(R.id.brand_save);
        brand_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ids = adapter.getSelectedItem();
                Intent intent = new Intent(Brand.this, Filters.class);
                intent.putExtra("sub_cat_id", subcat_id);
                intent.putExtra("sub_cat_ttl",subcat_ttl);
                intent.putExtra("brand_ids", ids);
                startActivity(intent);
                finish();
            }
        });

        recyclerView = findViewById(R.id.rview_brand_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void brandsService() {
        preferenceUtils = new PreferenceUtils(Brand.this);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.BRAND_LIST_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""));

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDoalog.dismiss();
                System.out.println("----------------------------------------------------");
                Log.d("Call request", call.request().toString());
                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
                Log.d("Response raw", String.valueOf(response.raw().body()));
                Log.d("Response code", String.valueOf(response.code()));
                System.out.println("----------------------------------------------------");

                if (response.isSuccessful()) {
                    String searchResponse = response.body().toString();
                    try {
                        JSONObject responseObject = new JSONObject(searchResponse);
                        int status = responseObject.getInt("status");
                        if (status == 1) {
                            JSONArray brand_list__Array = responseObject.getJSONArray("data");
                            brand_list_array = new ArrayList();
                            for (int i = 0; i < brand_list__Array.length(); i++) {
                                JSONObject orderObject = brand_list__Array.getJSONObject(i);
                                BrandModel brandModel = new BrandModel();
                                brandModel.setId(orderObject.getString("id"));
                                brandModel.setName(orderObject.getString("name"));
                                brandModel.setChecked(false);
                                brand_list_array.add(brandModel);
                            }
                            setUprecyclerView();
                        }

                    } catch (Exception e) {
//                        e.printStackTrace();
                        Log.e("error", e.getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }

    private void setUprecyclerView() {
        if (brand_list_array == null && brand_list_array.size() == 0) {
//            Toast.makeText(this, R.string.no_items, Toast.LENGTH_LONG).show();
        } else {
            adapter = new BrandItemAdapter(Brand.this, brand_list_array);
            recyclerView.setAdapter(adapter);

        }
    }
}
