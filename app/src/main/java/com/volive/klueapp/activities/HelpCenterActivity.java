package com.volive.klueapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.API_class;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.PreferenceUtils;
import com.volive.klueapp.utils.Retrofit_funtion_class;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpCenterActivity extends BaseActivity {
    TextView tv_address, address, tv_email, email, tv_phn_num, phone_num, tv_pincode, pincode;
    PreferenceUtils preferenceUtils;
    Toolbar rl_header;
    ImageView navigation_icon;
    TextView title;
    LinearLayout nav_home,nav_top_pics,nav_categories,nav_whishlist,nav_account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_help_center);
        initalizeUi();
        helpCenterService();
    }

    private void initalizeUi() {
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

        tv_address = findViewById(R.id.tv_address);
        address = findViewById(R.id.address);
        tv_email = findViewById(R.id.tv_email);
        email = findViewById(R.id.email);
        tv_phn_num = findViewById(R.id.tv_phn_num);
        phone_num = findViewById(R.id.phone_num);
        tv_pincode = findViewById(R.id.tv_pincode);
        pincode = findViewById(R.id.pincode);

        rl_header = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.help_center));
        navigation_icon = (ImageView) findViewById(R.id.back_button);
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpCenterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    public void helpCenterService() {
        preferenceUtils = new PreferenceUtils(HelpCenterActivity.this);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.HELP_CENTER(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""));

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
                            JSONObject jsonObject = responseObject.getJSONObject("data");
                            address.setText(Html.fromHtml(jsonObject.getString("address").trim()));
                            email.setText(jsonObject.getString("email").trim());
                            pincode.setText(jsonObject.getString("pincode").trim());
                            phone_num.setText(jsonObject.getString("phone_no").trim());
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
}