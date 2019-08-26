package com.volive.klueapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.klueapp.models.CartItemModel;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.API_class;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.PreferenceUtils;
import com.volive.klueapp.utils.Retrofit_funtion_class;
import com.volive.klueapp.adpaters.ShoppingCartAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoppingCart extends AppCompatActivity implements View.OnClickListener {
    public static ArrayList<CartItemModel> cart_array;
    public static TextView total_price, price_type;
    static PreferenceUtils preferenceUtils;
    static ShoppingCartAdapter adapter;
    static RecyclerView recyclerView;
    Toolbar rl_header;
    ImageView navigation_icon, cart;
    TextView title;
    Button continue_shop;
    LinearLayout nav_home,nav_top_pics,nav_categories,nav_whishlist,nav_account;

    public static void removeFromCart(final Context context, final int position) {
        preferenceUtils = new PreferenceUtils(context);
        String prod_id = ShoppingCart.cart_array.get(position).getProd_id();
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.DEL_CART_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""), prod_id);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(context,R.style.AppCompatAlertDialogStyle);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.dismiss();

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDoalog.dismiss();
                if (response.isSuccessful()) {
                    String searchResponse = response.body().toString();
                    try {
                        JSONObject responseObject = new JSONObject(searchResponse);
                        int status = responseObject.getInt("status");
                        if (status == 1) {
                            String message = responseObject.getString("message");
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            if (context instanceof ShoppingCart) {
                                cart_array.remove(position);
                                adapter.notifyItemRemoved(position);
                                adapter.notifyItemRangeChanged(position, cart_array.size());
                                adapter.notifyDataSetChanged();
                            }
                            ((ShoppingCart) context).get_cart_services();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.shopping_cart);
        intializeUI();

        get_cart_services();

    }

    private void intializeUI() {
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

        recyclerView = findViewById(R.id.rview_cart);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        total_price = findViewById(R.id.total_price);
//        price_type=findViewById(R.id.price_type);
        rl_header = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        navigation_icon = (ImageView) findViewById(R.id.back_button);
        title = (TextView) findViewById(R.id.tv_title);
        cart = findViewById(R.id.cart);
        cart.setVisibility(View.GONE);
        title.setText(R.string.shoppind_cart);
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        continue_shop = findViewById(R.id.continue_shop);
        continue_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cart_array.isEmpty()) {
                    Intent intent = new Intent(ShoppingCart.this, GiftsCardsActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(ShoppingCart.this, getResources().getString(R.string.cart_is_empty_add_items_into_cart), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUpRecyclerView(int position) {
        adapter = new ShoppingCartAdapter(ShoppingCart.this, cart_array);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void get_cart_services() {
        preferenceUtils = new PreferenceUtils(ShoppingCart.this);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.GET_CART_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""), preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, ""));

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ShoppingCart.this,R.style.AppCompatAlertDialogStyle);
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
                        total_price.setText(responseObject.getString("sub_total"));

                        if (status == 1) {
                            JSONArray cartArray = responseObject.getJSONArray("data");
                            cart_array = new ArrayList();
                            for (int i = 0; i < cartArray.length(); i++) {
                                JSONObject cart_object = cartArray.getJSONObject(i);
                                CartItemModel cartItemModel = new CartItemModel();
                                cartItemModel.setProd_id(cart_object.getString("prod_id"));
                                cartItemModel.setBrand_name(cart_object.getString("brand_name"));
                                cartItemModel.setQuantity(cart_object.getString("quantity"));
                                cartItemModel.setPname(cart_object.getString("pname"));
                                cartItemModel.setProd_image(base_path + cart_object.getString("prod_image"));
                                if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.SAR)) {
                                    cartItemModel.setPrice_sar(cart_object.getString("price_sar"));
//                                    price_type.setText(Constants.SAR);
                                } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.AED)) {
                                    cartItemModel.setPrice_aed(cart_object.getString("price_aed"));
//                                    price_type.setText(Constants.AED);
                                } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.KWD)) {
                                    cartItemModel.setPrice_kwd(cart_object.getString("price_kwd"));
//                                    price_type.setText(Constants.KWD);
                                } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.USD)) {
                                    cartItemModel.setPrice_usd(cart_object.getString("price_usd"));
//                                    price_type.setText(Constants.USD);
                                } else {
                                    cartItemModel.setPrice_sar(cart_object.getString("price_sar"));
//                                    price_type.setText(Constants.SAR);
                                }
                                cart_array.add(cartItemModel);
                            }
                            setUpRecyclerView(-1);
                        }
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

    @Override
    protected void onResume() {
        super.onResume();
        get_cart_services();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.nav_home:
                intent = new Intent(ShoppingCart.this, MainActivity.class);
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
                intent = new Intent(ShoppingCart.this, MainActivity.class);
                intent.putExtra("Option", "true");
                intent.putExtra("Position", "1");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.nav_categories:
                intent = new Intent(ShoppingCart.this, MainActivity.class);
                intent.putExtra("Option", "true");
                intent.putExtra("Position", "2");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.nav_whishlist:
                intent = new Intent(ShoppingCart.this, MainActivity.class);
                intent.putExtra("Option", "true");
                intent.putExtra("Position", "3");
                startActivity(intent);
                break;
            case R.id.nav_account:
                intent = new Intent(ShoppingCart.this, MainActivity.class);
                intent.putExtra("Option", "true");
                intent.putExtra("Position", "4");
                startActivity(intent);
                break;
        }
    }
}