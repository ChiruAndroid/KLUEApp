package com.volive.klueapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
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

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends BaseActivity implements View.OnClickListener {

    public static LinearLayout linearLayout, lin_layout;
    EditText email, pwd, editText;
    LinearLayout forgot_password;
    ImageView back;
    Button signin;
    Context context;
    TextView textView, txt_ok;
    AlertDialog alertDialog;
    Intent intent;
    String st_email, st_password, token = "sdff";
    PreferenceUtils preferenceUtils;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);
        context = this;
        preferenceUtils = new PreferenceUtils(this);
        initaializeUI();

//        email.setText("volive@gmail.com");
//        pwd.setText("123");
    }

    private void initaializeUI() {

        email = (EditText) findViewById(R.id.email);
        pwd = (EditText) findViewById(R.id.password);
        forgot_password = (LinearLayout) findViewById(R.id.forgot_password);
        back = (ImageView) findViewById(R.id.backarrow);
        signin = (Button) findViewById(R.id.signinbtn);
        linearLayout = (LinearLayout) findViewById(R.id.linearlogin);
        lin_layout = findViewById(R.id.lin_layout);
        back.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
        signin.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(Login.this, OnBoardingScreen.class);
                startActivity(intent);*/
                finish();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signinbtn:
                if (isNetworkConnected()) {
                    st_email = email.getText().toString().trim();
                    st_password = pwd.getText().toString().trim();
                    if (!st_email.isEmpty() && !st_password.isEmpty()) {
                        loginService();
                    } else {
                        Toast.makeText(Login.this, R.string.plz_fill_all_fileds, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    BaseActivity.showDialog(Login.this, SweetAlertDialog.WARNING_TYPE, "", getString(R.string.plz_check_internet_connection), "OK!",
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            });
                }
                break;
            case R.id.linearlogin:
                intent = new Intent(Login.this, Register.class);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                startActivity(intent);
                finish();
                break;
            case R.id.forgot_password:
                if (isNetworkConnected()) {
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
                    View dialogView = LayoutInflater.from(context).inflate(R.layout.forgot_password_dialog, null);
                    dialogBuilder.setView(dialogView);
                    textView = dialogView.findViewById(R.id.tv_number);
                    editText = dialogView.findViewById(R.id.et_number);
                    txt_ok = dialogView.findViewById(R.id.ok);
                    alertDialog = dialogBuilder.create();
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    Window window = alertDialog.getWindow();
                    layoutParams.copyFrom(window.getAttributes());
                    // This makes the dialog take up the full width
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    window.setAttributes(layoutParams);
                    txt_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!editText.getText().toString().isEmpty()) {
                                st_email = editText.getText().toString().trim();
                                forgotPasswordService();

                            } else {
                                Toast.makeText(Login.this, R.string.plz_enter_ur_mail_or_mobile_num, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    alertDialog.show();
                } else {
                    BaseActivity.showDialog(Login.this, SweetAlertDialog.WARNING_TYPE, "", getString(R.string.plz_check_internet_connection), "OK!",
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                }
                            });
                }
                break;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void loginService() {
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.USER_LOGIN(Constants.API_KEY, st_email, st_password, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), Constants.DIVICE_TYPE, token);

        final ProgressDialog progressDoalog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
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
                    Log.d("Login", "response  >>" + searchResponse.toString());
                    try {
                        JSONObject lObj = new JSONObject(searchResponse);
                        int status = lObj.getInt("status");
                        String message = lObj.getString("message");
                        if (status == 1) {
                            JSONObject jsonObject = lObj.getJSONObject("data");
                            String user_id = jsonObject.getString("user_id");
                            String fname = jsonObject.getString("fname");
                            String email = jsonObject.getString("email");
                            String password = jsonObject.getString("password");
                            String mobile = jsonObject.getString("mobile");
                            String image = jsonObject.getString("image");
                            String base_path = lObj.getString("base_path");

                            preferenceUtils.saveString(PreferenceUtils.User_name, fname);
                            preferenceUtils.saveString(PreferenceUtils.USER_ID, user_id);
                            preferenceUtils.saveString(PreferenceUtils.Email, email);
                            preferenceUtils.saveString(PreferenceUtils.Password, password);
                            preferenceUtils.saveString(PreferenceUtils.Mobile, mobile);
                            preferenceUtils.saveString(PreferenceUtils.IMAGE, image);
                            preferenceUtils.saveString(PreferenceUtils.BASE_PATH, base_path);
                            preferenceUtils.saveBoolean(PreferenceUtils.LOG_OUT, false);
                            startActivity(new Intent(context, MainActivity.class));
                            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                            finish();
                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
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

    public void forgotPasswordService() {
        preferenceUtils = new PreferenceUtils(Login.this);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.FORGOT_PWD_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), st_email);
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
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
                            String message = responseObject.getString("message");
                            Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
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