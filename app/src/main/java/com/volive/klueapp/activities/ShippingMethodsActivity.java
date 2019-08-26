package com.volive.klueapp.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.API_class;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.PreferenceUtils;
import com.volive.klueapp.utils.Retrofit_funtion_class;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShippingMethodsActivity extends AppCompatActivity implements View.OnClickListener {
    public static EditText tv_delevery_date;
    public static RadioButton rd_time_2pm, rd_time_3pm;
    public static TextView tv_delevery_price;
    Toolbar rl_header;
    ImageView navigation_icon;
    TextView title, process;
    PreferenceUtils preferenceUtils;
    Calendar myCalendar;
    String msg, note, img_path, responseDate;
    DatePickerDialog mDatePicker;
    String strDeliveryTime;
    Boolean rdChecked = false;
    LinearLayout nav_home, nav_top_pics, nav_categories, nav_whishlist, nav_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shipping_methods);
        preferenceUtils = new PreferenceUtils(ShippingMethodsActivity.this);
        preferenceUtils.getStringFromPreference(PreferenceUtils.DEL_TIMINGS, "");

        img_path = getIntent().getStringExtra("img_path");
        msg = getIntent().getStringExtra("msg");
        note = getIntent().getStringExtra("note");

        initializeUI();
        shippingMethodsService();
        myCalendar = Calendar.getInstance();

        tv_delevery_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] date = responseDate.trim().split("-");
                final int mYear, mMonth, mDay;
                myCalendar.set(Calendar.YEAR, Integer.parseInt(date[0]));
                myCalendar.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
                myCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));
                mYear = myCalendar.get(Calendar.YEAR);
                mMonth = myCalendar.get(Calendar.MONTH);
                mDay = myCalendar.get(Calendar.DAY_OF_MONTH);
                mDatePicker = new DatePickerDialog(ShippingMethodsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub

                        // validate here with condition to avoid to select past dates
                        if (selectedyear == mYear && (selectedmonth - 1) == mMonth - 1) {
                            if (selectedday < mDay) {
                                Toast.makeText(ShippingMethodsActivity.this, "Invalidate date", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }

                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        updateLabel();
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setMinDate(myCalendar.getTimeInMillis() - 1000);
                if (!mDatePicker.isShowing()) {
                    mDatePicker.show();
                }
            }
        });

    }

    private void initializeUI() {
        nav_home = findViewById(R.id.nav_home);
        nav_top_pics = findViewById(R.id.nav_top_pics);
        nav_categories = findViewById(R.id.nav_categories);
        nav_whishlist = findViewById(R.id.nav_whishlist);
        nav_account = findViewById(R.id.nav_account);
        nav_home.setOnClickListener(this);
        nav_top_pics.setOnClickListener(this);
        nav_categories.setOnClickListener(this);
        nav_whishlist.setOnClickListener(this);
        nav_account.setOnClickListener(this);

        tv_delevery_price = findViewById(R.id.tv_delevery_price);
        tv_delevery_date = findViewById(R.id.tv_delevery_date);
        rd_time_2pm = findViewById(R.id.time_2pm);
        rd_time_3pm = findViewById(R.id.time_3pm);
        rl_header = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText("Shipping Charges Details");
        process = findViewById(R.id.process);
        navigation_icon = (ImageView) findViewById(R.id.back_button);
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rd_time_2pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdChecked = true;
                strDeliveryTime = Constants.St_2PM;
            }
        });
        rd_time_3pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdChecked = true;
                strDeliveryTime = Constants.St_3PM;
            }
        });
        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdChecked != false) {
                    Intent intent = new Intent(ShippingMethodsActivity.this, Payment_Method.class);
                    intent.putExtra("img_path", img_path);
                    intent.putExtra("msg", msg);
                    intent.putExtra("note", note);
                    intent.putExtra("delivery_dt", tv_delevery_date.getText().toString());
                    intent.putExtra("delivery_tym", strDeliveryTime);
                    startActivity(intent);
                } else {
                    Toast.makeText(ShippingMethodsActivity.this, getResources().getString(R.string.plz_select_delivery_time), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void shippingMethodsService() {
        preferenceUtils = new PreferenceUtils(ShippingMethodsActivity.this);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.SHIPPING_METHODS_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, ""), preferenceUtils.getStringFromPreference(preferenceUtils.USER_ID, ""));

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
                            tv_delevery_price.setText(jsonObject.getString("delivery_price"));
                            tv_delevery_date.setText(jsonObject.getString("delivery_date"));
                            checkDayToast(jsonObject.getString("delivery_date"));
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

    private Date checkDayToast(String dt) {
        responseDate = dt;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = new Date();
            date = format.parse(dt);
            String dayOfTheWeek = (String) DateFormat.format("EEEE", date);
            if (dayOfTheWeek.equalsIgnoreCase("Friday")) {
                rd_time_2pm.setVisibility(View.VISIBLE);
                rd_time_3pm.setVisibility(View.GONE);
            } else {
                rd_time_2pm.setVisibility(View.VISIBLE);
                rd_time_3pm.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        tv_delevery_date.setText(sdf.format(myCalendar.getTime()));
        checkDayToast(tv_delevery_date.getText().toString());
        //day.setText(sdf12.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.nav_home:
                intent = new Intent(ShippingMethodsActivity.this, MainActivity.class);
                intent.putExtra("Option", "true");
                intent.putExtra("Position", "0");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                MainActivity.rl_header.setVisibility(View.VISIBLE);
                MainActivity.title.setText(getResources().getString(R.string.home));
                break;
            case R.id.nav_top_pics:
                MainActivity.rl_header.setVisibility(View.VISIBLE);
                MainActivity.title.setText(getResources().getString(R.string.top_pics));
                intent = new Intent(ShippingMethodsActivity.this, MainActivity.class);
                intent.putExtra("Option", "true");
                intent.putExtra("Position", "1");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.nav_categories:
                intent = new Intent(ShippingMethodsActivity.this, MainActivity.class);
                intent.putExtra("Option", "true");
                intent.putExtra("Position", "2");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.nav_whishlist:
                intent = new Intent(ShippingMethodsActivity.this, MainActivity.class);
                intent.putExtra("Option", "true");
                intent.putExtra("Position", "3");
                startActivity(intent);
                break;
            case R.id.nav_account:
                intent = new Intent(ShippingMethodsActivity.this, MainActivity.class);
                intent.putExtra("Option", "true");
                intent.putExtra("Position", "4");
                startActivity(intent);
                break;
        }
    }
}