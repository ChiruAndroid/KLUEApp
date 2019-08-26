package com.volive.klueapp.activities;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.API_class;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.DZ_URL;
import com.volive.klueapp.utils.PreferenceUtils;
import com.volive.klueapp.utils.Retrofit_funtion_class;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Register extends BaseActivity {

    EditText name, email, password, number;
    Context context;
    Button button;
    TextView txt_signin, register;
    ImageView back_arrow;
    ArrayList<String> country_list;
    Spinner countrySpinner;

    PreferenceUtils preferenceUtils;
    String st_name, st_email, st_password, st_mobile, token = "sdff";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.register);
        preferenceUtils = new PreferenceUtils(this);
        initializeUI();

        if (isNetworkConnected()) {
            getCountryService();
        } else {
            BaseActivity.showDialog(Register.this, SweetAlertDialog.WARNING_TYPE, "", getString(R.string.plz_check_internet_connection), "OK!",
                    new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    });
        }

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String country = country_list.get(position);
//                Toast.makeText(Register.this,  country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()) {
                    st_email = email.getText().toString().trim();
                    st_name = name.getText().toString().trim();
                    st_mobile = number.getText().toString().trim();
                    st_password = password.getText().toString().trim();

                    if (!st_name.equalsIgnoreCase("") && !st_email.equalsIgnoreCase("") && !st_password.equalsIgnoreCase("") && !st_mobile.equalsIgnoreCase("")) {
                        user_register();
//                    doAndroidNetworking();
                    } else {
                        Toast.makeText(Register.this, R.string.plz_fill_all_fileds, Toast.LENGTH_SHORT).show();
                    }
                } else{
                    BaseActivity.showDialog(Register.this, SweetAlertDialog.WARNING_TYPE, "", getString(R.string.plz_check_internet_connection), "OK!",
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            });
                }

            }
        });

    }

    private void initializeUI() {
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        number = (EditText) findViewById(R.id.number);
        register = (TextView) findViewById(R.id.register);
        back_arrow = (ImageView) findViewById(R.id.backarrow);
        countrySpinner = findViewById(R.id.countrySpinner);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_signin = (TextView) findViewById(R.id.txt_signin);
        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        button = (Button) findViewById(R.id.createaccount);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

   /* private void doAndroidNetworking() {
        AndroidNetworking.post("http://volivesolutions.com/Klue/services/register")
                .addBodyParameter("fname", name.getText().toString())
                .addBodyParameter("email", email.getText().toString())
                .addBodyParameter("mobile", number.getText().toString())
                .addBodyParameter("password", password.getText().toString())
                .addBodyParameter("device_type", "Android")
                .addBodyParameter("lang", "en")
                .addBodyParameter("API-KEY", "985869547")
                .addBodyParameter("device_token", "dfvhjg")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(Register.this, "Response\n"+response, Toast.LENGTH_LONG).show();
                        // do anything with response
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(Register.this, "Error\n"+error, Toast.LENGTH_LONG).show();
                        // handle error
                    }
                });
    }*/

    public void user_register() {

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        final API_class service = retrofit.create(API_class.class);

        Call<JsonElement> callRetrofit = null;

        callRetrofit = service.USER_REGISTER(st_name, st_email, st_mobile, st_password, Constants.DIVICE_TYPE, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), token, Constants.API_KEY);

       /* final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(Register.this);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();*/


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
                    Log.d("Regestration", "response  >>" + searchResponse.toString());

                    try {
                        JSONObject lObj = new JSONObject(searchResponse);
                        int status = lObj.getInt("status");
                        String message = lObj.getString("message");

                        if (status == 1) {
                            Toast.makeText(Register.this, message, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else
                            Toast.makeText(Register.this, message, Toast.LENGTH_LONG).show();

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

    public void getCountryService() {
        preferenceUtils = new PreferenceUtils(Register.this);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.COUNTRY_LIST_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""));

      /*  final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();*/

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

                System.out.println("----------------------------------------------------");

                if (response.isSuccessful()) {
                    String searchResponse = response.body().toString();
                    try {
                        JSONObject responseObject = new JSONObject(searchResponse);
                        int status = responseObject.getInt("status");
                        if (status == 1) {
                            JSONArray jsonArray = responseObject.getJSONArray("data");
                            country_list = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                country_list.add(jsonObject.getString("country_code"));
                            }
                            countrySpinner.setAdapter(new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_dropdown_item, country_list));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
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

}