package com.volive.klueapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class AddNewAddress extends AppCompatActivity {

    static LinearLayout lin_lay;
    Toolbar rl_header;
    ImageView navigation_icon;
    TextView title;
    ImageView backarrow;
    Button saveaddress;
    PreferenceUtils preferenceUtils;
    EditText et_name, et_address, et_landmark, et_mail, et_mobile_num, et_city;
    String st_username, st_address, st_landmark, st_mail, st_mobile_num, st_city;
    String st_shipping_id = "";
    Bundle extras;
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.add_new_address);
        preferenceUtils = new PreferenceUtils(AddNewAddress.this);
        preferenceUtils.getStringFromPreference(PreferenceUtils.Language, "");
        city = preferenceUtils.getStringFromPreference(PreferenceUtils.City, "");

        initializeUI();

//        extras = getIntent().getExtras();
//        if (extras != null) {
//            st_shipping_id = extras.getString("shipping_id");
//            st_username = extras.getString("username");
//            st_mail = extras.getString("email");
//            st_mobile_num = extras.getString("mobile");
//            st_address = extras.getString("address");
//            st_city = extras.getString("city");
//            st_landmark = extras.getString("landmark");
//
//        System.out.println("jdhfcdsj "+st_shipping_id+" "+st_username+" "+st_mail+" "+st_mobile_num+" "+st_address+" "+st_city+" "+st_landmark);
//        }else{
//            System.out.println("jdhfcdsj "+st_shipping_id+" "+st_username+" "+st_mail+" "+st_mobile_num+" "+st_address+" "+st_city+" "+st_landmark);
//        }
        saveaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                st_username = et_name.getText().toString().trim();
                st_address = et_address.getText().toString().trim();
                st_landmark = et_landmark.getText().toString().trim();
                st_city = et_city.getText().toString().trim();
                st_mail = et_mail.getText().toString().trim();
                st_mobile_num = et_mobile_num.getText().toString().trim();
                addNewAddress_service();
            }
        });
    }

    private void initializeUI() {
        lin_lay = findViewById(R.id.lin_lay);
        et_name = findViewById(R.id.et_name);
        et_address = findViewById(R.id.et_address);
        et_landmark = findViewById(R.id.et_landmark);
        et_mail = findViewById(R.id.et_mail);
        et_mobile_num = findViewById(R.id.et_mobile_num);
        et_city = findViewById(R.id.et_city);
        et_city.setText(city);

        rl_header = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.add_new_address));
        backarrow = (ImageView) findViewById(R.id.back_button);
        saveaddress = (Button) findViewById(R.id.saveaddress);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        extras = getIntent().getExtras();
        if (extras != null) {
            st_shipping_id = extras.getString("shipping_id");
            st_username = extras.getString("username");
            st_mail = extras.getString("email");
            st_mobile_num = extras.getString("mobile");
            st_address = extras.getString("address");
            st_city = extras.getString("city");
            st_landmark = extras.getString("landmark");

            System.out.println("jdhfcdsj "+st_shipping_id+" "+st_username+" "+st_mail+" "+st_mobile_num+" "+st_address+" "+st_city+" "+st_landmark);
        }else{
            System.out.println("jdhfcdsj "+st_shipping_id+" "+st_username+" "+st_mail+" "+st_mobile_num+" "+st_address+" "+st_city+" "+st_landmark);
        }

        if (extras != null) {
            et_name.setText(st_username);
            et_address.setText(st_address);
            et_city.setText(st_city);
            et_landmark.setText(st_landmark);
            et_mail.setText(st_mail);
            et_mobile_num.setText(st_mobile_num);
        }
    }

    private void addNewAddress_service() {
        preferenceUtils = new PreferenceUtils(AddNewAddress.this);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.ADD_NEW_ADDRESS_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""),
                preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""), st_username, st_mail, st_address,
                st_city, st_shipping_id, st_landmark, st_mobile_num);

        final ProgressDialog progressDoalog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDoalog.dismiss();
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
                            //  Toast.makeText(AddNewAddress.this, message, Toast.LENGTH_SHORT).show();

                            String res_message = responseObject.getString("message");
                            if (responseObject.has("shipping_id")) {
                                int shipping_id = responseObject.getInt("shipping_id");
                            }
                            message(res_message);
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("error", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDoalog.dismiss();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }

    public static void message(String message) {
        Snackbar snackbar = Snackbar
                .make(lin_lay, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}