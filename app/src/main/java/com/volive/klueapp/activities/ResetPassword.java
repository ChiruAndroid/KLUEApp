package com.volive.klueapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.API_class;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.PreferenceUtils;
import com.volive.klueapp.utils.Retrofit_funtion_class;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassword extends BaseActivity implements TextWatcher {
    LinearLayout layout;
    Button btn_reset_password;
    TextView txt_title;
    Toolbar toolbar;
    ImageView back_img;
    EditText et_old_pwd, et_new_pwd, et_con_pwd;
    PreferenceUtils preferenceUtils;
    String old_pass, new_pass, con_pass;
    Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reset_password);

        preferenceUtils = new PreferenceUtils(this);
        initializeUI();
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ResetPassword.this, MainActivity.class);
                mainIntent.putExtra("Action", "reset password");
                startActivity(mainIntent);
                finish();
                onBackPressed();
            }
        });

        btn_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) {
                    old_pass = et_old_pwd.getText().toString().trim();
                    new_pass = et_new_pwd.getText().toString().trim();
                    con_pass = et_con_pwd.getText().toString().trim();

                    if (!old_pass.isEmpty() && !new_pass.isEmpty() && !con_pass.isEmpty()) {
                        reset_password_service();
//                    doAndroidNetworking();
                    } else {
                        Toast.makeText(ResetPassword.this, R.string.plz_fill_all_fileds, Toast.LENGTH_SHORT).show();
                    }
                }else
                    Toast.makeText(ResetPassword.this, R.string.plz_check_internet_connection, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initializeUI() {
        toolbar = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        et_old_pwd = findViewById(R.id.old_pwd);
        et_new_pwd = findViewById(R.id.new_pwd);
        et_con_pwd = findViewById(R.id.con_pwd);
        back_img = findViewById(R.id.back_button);
        txt_title = findViewById(R.id.tv_title);
        txt_title.setText(R.string.reset_password);
        btn_reset_password = findViewById(R.id.reset_password);
        btn_reset_password.setEnabled(false);

        et_old_pwd.addTextChangedListener(this);
        et_new_pwd.addTextChangedListener(this);
        et_con_pwd.addTextChangedListener(this);
    }

    public void reset_password_service() {

        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.RESET_PASSWORD(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""), old_pass, new_pass, con_pass);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ResetPassword.this,R.style.AppCompatAlertDialogStyle);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                progressDoalog.dismiss();
                System.out.println("----------------------------------------------------");
                Log.d("Call request", call.request().toString());
                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
                Log.d("Response raw", String.valueOf(response.raw().body()));
                Log.d("Response code", String.valueOf(response.code()));

                System.out.println("responce" + response.toString());

                System.out.println("----------------------------------------------------");

                if (response.isSuccessful()) {
                    String searchResponse = response.body().toString();
                    Log.d("ResetPassword", "response  >>" + searchResponse.toString());

                    try {
                        JSONObject lObj = new JSONObject(searchResponse);
                        int status = lObj.getInt("status");
                        String message = lObj.getString("message");

                        if (status == 1) {
                            Toast.makeText(ResetPassword.this, message, Toast.LENGTH_LONG).show();
                            mainIntent = new Intent(ResetPassword.this, MainActivity.class);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mainIntent.putExtra("Action", "reset password");
                            startActivity(mainIntent);
                            finish();
                        } else
                            Toast.makeText(ResetPassword.this, message, Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Log.e("error", e.getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
//                progressDoalog.dismiss();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        s = et_con_pwd.getText().toString().trim();
        if (!s.toString().trim().isEmpty() &&
                !et_old_pwd.getText().toString().trim().isEmpty() && !et_new_pwd.getText().toString().trim().isEmpty()) {
            btn_reset_password.setEnabled(true);
        } else {
            btn_reset_password.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainIntent = new Intent(ResetPassword.this, MainActivity.class);
        mainIntent.putExtra("Action", "reset password");
        startActivity(mainIntent);
        finish();
    }
}
