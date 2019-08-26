package com.volive.klueapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.klueapp.models.Category_Model;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.API_class;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.PreferenceUtils;
import com.volive.klueapp.utils.Retrofit_funtion_class;
import com.volive.klueapp.adpaters.OccationsSubCatAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OccationsSubCategory extends BaseActivity {
    ArrayList<Category_Model> occationsModelArrayList;
    ImageView navigation_icon;
    TextView tv_title;
    Toolbar toolbar;
    RecyclerView rview_sub_categories;
    PreferenceUtils preferenceUtils;
    String subcat_id;
    LinearLayout nav_home,nav_top_pics,nav_categories,nav_whishlist,nav_account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_occations_sub_category);

        preferenceUtils = new PreferenceUtils(OccationsSubCategory.this);
        initializeUI();

        if (checkConnection()) {
            getSubCategories();
        } else
            Toast.makeText(OccationsSubCategory.this, R.string.plz_check_internet_connection, Toast.LENGTH_SHORT).show();
    }

    private void getSubCategories() {

        subcat_id = getIntent().getStringExtra("cat_id");
        tv_title.setText(getIntent().getStringExtra("title_nme"));

        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.SUB_CAT_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), subcat_id);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(this);
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
                            String base_path = responseObject.getString("base_path");
                            JSONArray subcatArray = responseObject.getJSONArray("data");
                            occationsModelArrayList = new ArrayList<>();
                            for (int i = 0; i < subcatArray.length(); i++) {
                                JSONObject subcatObject = subcatArray.getJSONObject(i);
                                Category_Model category_model = new Category_Model();
                                category_model.setCat_id(subcatObject.getString("sub_cat_id"));
                                category_model.setCname(subcatObject.getString("sub_cat_name"));
                                category_model.setCat_image(base_path + subcatObject.getString("sub_cat_image"));
                                occationsModelArrayList.add(category_model);
                            }
                        }
                        setUprecyclerView();
                    } catch (Exception e) {
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

        toolbar = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        tv_title = findViewById(R.id.tv_title);
        navigation_icon = findViewById(R.id.back_button);
        rview_sub_categories = findViewById(R.id.rview_sub_categories);
        LinearLayoutManager lManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rview_sub_categories.setLayoutManager(lManager);
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUprecyclerView() {

        if (occationsModelArrayList != null && occationsModelArrayList.size() == 0) {
//            Toast.makeText(this, R.string.no_items, Toast.LENGTH_LONG).show();
        } else {
            OccationsSubCatAdapter occationsSubCatAdapter = new OccationsSubCatAdapter(this, occationsModelArrayList);
            rview_sub_categories.setAdapter(occationsSubCatAdapter);
        }
    }
}
