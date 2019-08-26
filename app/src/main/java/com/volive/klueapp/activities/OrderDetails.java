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
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.klueapp.models.OrderDetailModel;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.API_class;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.PreferenceUtils;
import com.volive.klueapp.utils.Retrofit_funtion_class;
import com.volive.klueapp.adpaters.OrderDetailAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderDetails extends BaseActivity {

    public static ArrayList<OrderDetailModel> orderDetailModels;
    Toolbar rl_header;
    ImageView navigation_icon, order_img;
    PreferenceUtils preferenceUtils;
    String st_order_id;
    RecyclerView rview_order_detail_list;
    String order_dat, estimated_dat,st_currency_type,st_order_status,st_payment_status;
    TextView title, order_date, order_id, estimate_date, username, address, landmark,
            city, email, mobile_num, price_type,price_type1,price_type2, pdt_value, net_price, payment_type,
            total_price,currency_type,currency_type1,cod_value,shipping_value,item_status;
    LinearLayout layout_cod;
    LinearLayout nav_home,nav_top_pics,nav_categories,nav_whishlist,nav_account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.orderdetails);
        preferenceUtils = new PreferenceUtils(OrderDetails.this);

        Intent intent = getIntent();
        st_order_id = intent.getStringExtra("order_id");
        order_dat = intent.getStringExtra("order_date");
        estimated_dat = intent.getStringExtra("estimate_date");
        st_currency_type=intent.getStringExtra("currency_type");
        st_order_status=intent.getStringExtra("order_status");
        st_payment_status=intent.getStringExtra("payment_type");

        initializeUI();
        if (checkConnection()) {
            orderDetailService();
        } else
            Toast.makeText(OrderDetails.this, R.string.plz_check_internet_connection, Toast.LENGTH_SHORT).show();
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
        title.setText(R.string.order_details);
        layout_cod=findViewById(R.id.layout_cod);
        if (st_payment_status!=null&&st_payment_status.equalsIgnoreCase("1")){
            layout_cod.setVisibility(View.VISIBLE);
        }
        order_date = findViewById(R.id.order_date);
        order_date.setText(order_dat);

        estimate_date = findViewById(R.id.estimate_date);
        estimate_date.setText(estimated_dat);
        order_id = findViewById(R.id.order_id_detail);
        order_id.setText(st_order_id);

        price_type = findViewById(R.id.price_type);
        price_type.setText(st_currency_type);

        price_type2=findViewById(R.id.price_type2);
        price_type2.setText(st_currency_type);

        price_type1=findViewById(R.id.price_type1);
        price_type1.setText(st_currency_type);
        currency_type=findViewById(R.id.currency_type);
        currency_type.setText(st_currency_type);
        shipping_value=findViewById(R.id.shipping_value);

        order_img = findViewById(R.id.order_img);
        username = findViewById(R.id.username);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);
        landmark = findViewById(R.id.landmark);
        email = findViewById(R.id.email);
        mobile_num = findViewById(R.id.mobile_number);
        pdt_value = findViewById(R.id.pdt_value);
        payment_type = findViewById(R.id.payment_type);
        net_price = findViewById(R.id.net_price);
        total_price = findViewById(R.id.total_price);
        item_status=findViewById(R.id.item_status);

        currency_type1=findViewById(R.id.currency_type1);
        currency_type1.setText(st_currency_type);
        price_type2.setText(st_currency_type);
        cod_value=findViewById(R.id.cod_value);
        rview_order_detail_list = findViewById(R.id.rview_order_detail_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderDetails.this, LinearLayoutManager.VERTICAL, false);
        rview_order_detail_list.setLayoutManager(layoutManager);

        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void orderDetailService() {
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.MY_ORDER_DETAILS_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""), st_order_id);

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
                        String base_path = responseObject.getString("base_path");
                        String shipping_charges=responseObject.getString("delivery_price");
                        shipping_value.setText(shipping_charges);
                        String total_amt = responseObject.getString("total");
                        total_price.setText(total_amt);
                        net_price.setText(total_amt);
                        cod_value.setText(responseObject.getString("cod_price"));
                        pdt_value.setText(responseObject.getString("sub_total"));
                        item_status.setText(st_order_status);

                        if (status == 1) {
                            JSONArray jsonArray = responseObject.getJSONArray("data");
                            orderDetailModels = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                OrderDetailModel detailModel = new OrderDetailModel();
                                detailModel.setProd_id(jsonObject.getString("prod_id"));
                                detailModel.setProduct_qty(jsonObject.getString("product_qty"));
                                detailModel.setPrice(jsonObject.getString("price"));
                                detailModel.setBrand_name(jsonObject.getString("brand_name"));
                                detailModel.setProd_name(jsonObject.getString("prod_name"));
                                detailModel.setProd_image(base_path + jsonObject.getString("prod_image"));
                                orderDetailModels.add(detailModel);
                            }
                            setUprecyclerView();
                            JSONObject shipping_info_data = responseObject.getJSONObject("shipping_details");
                            username.setText(shipping_info_data.getString("username"));
                            address.setText(shipping_info_data.getString("address"));
                            landmark.setText(shipping_info_data.getString("landmark"));
                            city.setText(shipping_info_data.getString("city"));
                            email.setText(shipping_info_data.getString("email"));
                            mobile_num.setText(shipping_info_data.getString("mobile"));
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
        if (orderDetailModels == null && orderDetailModels.size() == 0) {
//            Toast.makeText(this, R.string.no_items, Toast.LENGTH_LONG).show();
        } else {
            OrderDetailAdapter adapter = new OrderDetailAdapter(OrderDetails.this, orderDetailModels, "my_orders",st_currency_type);
            rview_order_detail_list.setAdapter(adapter);
        }
    }
}