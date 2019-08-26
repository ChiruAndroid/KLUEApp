package com.volive.klueapp.activities;


import android.app.ProgressDialog;
import android.content.Intent;
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

import com.google.gson.JsonElement;
import com.volive.klueapp.models.OrdersModel;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.API_class;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.PreferenceUtils;
import com.volive.klueapp.utils.Retrofit_funtion_class;
import com.volive.klueapp.adpaters.MyOrder_Adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyOrder extends BaseActivity implements View.OnClickListener {
    public static ArrayList<OrdersModel> myOrders_list;
    Toolbar rl_header;
    ImageView navigation_icon;
    TextView title;
    PreferenceUtils preferenceUtils;
    RecyclerView recyclerView;
    MyOrder_Adapter adapter;

    LinearLayout nav_home,nav_top_pics,nav_categories,nav_whishlist,nav_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.myorder);
        initializeUI();
        preferenceUtils = new PreferenceUtils(MyOrder.this);
        if (!preferenceUtils.getbooleanFromPreference(preferenceUtils.LOG_OUT, true)) {
            myOrdersService();
        } else {
            Intent intent = new Intent(MyOrder.this, Login.class);
            intent.putExtra("Activity", "Product");
            startActivity(intent);
        }
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
        navigation_icon = (ImageView) findViewById(R.id.back_button);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.my_orders));
        recyclerView = (RecyclerView) findViewById(R.id.rview_myorders_list);
        LinearLayoutManager lManager = new LinearLayoutManager(MyOrder.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lManager);
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    public void myOrdersService() {
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.MY_ORDERS_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""));

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
                            JSONArray myOrderArray = responseObject.getJSONArray("data");
                            myOrders_list = new ArrayList();
                            for (int i = 0; i < myOrderArray.length(); i++) {
                                JSONObject orderObject = myOrderArray.getJSONObject(i);
                                OrdersModel ordersModel = new OrdersModel();
                                ordersModel.setOrder_id(orderObject.getString("order_id"));
                                ordersModel.setNo_of_items(orderObject.getString("no_of_items"));
                                ordersModel.setSub_total(orderObject.getString("sub_total"));
                                ordersModel.setCurrency_type(orderObject.getString("currency_type"));
                                ordersModel.setOrder_date(orderObject.getString("order_date"));
                                ordersModel.setEstimate_date(orderObject.getString("estimate_date"));
                                ordersModel.setOrder_status(orderObject.getString("order_status"));
                                myOrders_list.add(ordersModel);
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
        if (myOrders_list == null && myOrders_list.size() == 0) {
        } else {
            adapter = new MyOrder_Adapter(MyOrder.this, myOrders_list, "my_orders");
            recyclerView.setAdapter(adapter);
        }
    }

}