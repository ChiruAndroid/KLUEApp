package com.volive.klueapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.klueapp.models.SubProductListModel;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.API_class;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.PreferenceUtils;
import com.volive.klueapp.utils.Retrofit_funtion_class;
import com.volive.klueapp.adpaters.SubProListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Anniversary extends BaseActivity {

    public static ArrayList<SubProductListModel> product_arrayList;
    RecyclerView recyclerView;
    GridLayoutManager manager;
    PopupWindow window;
    Toolbar toolbar;
    ImageView navigation_icon;
    TextView tv_title;
    SubProListAdapter proListAdapter;
    PreferenceUtils preferenceUtils;
    LinearLayout fltr_screen, sortbtn;
    RadioButton rbPopularity, rb_price_low_to_high, rb_price_high_to_low, rb_new_arrivals;
    CardView cardViewSubmit, cardViewCancel, filter_screen_layout;
    String sortId = "", lw_price = "", hgh_price = "", brnd_ids;
    String subcat_id, subcat_ttl;
    LinearLayout nav_home, nav_top_pics, nav_categories, nav_whishlist, nav_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.anniversary);
        preferenceUtils = new PreferenceUtils(Anniversary.this);
        initializeUI();
        checkValuesAndCallApi();
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

        toolbar = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.sub_category));
        navigation_icon = findViewById(R.id.back_button);
        sortbtn = findViewById(R.id.sortbtn);
        fltr_screen = (LinearLayout) findViewById(R.id.fltr_screen);
        filter_screen_layout = findViewById(R.id.filter_screen_layout);
        fltr_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Anniversary.this, Filters.class);
                intent.putExtra("sub_cat_id", subcat_id);
                intent.putExtra("sub_cat_ttl", subcat_ttl);
                intent.putExtra("brand_ids", brnd_ids);
                startActivity(intent);
            }
        });
        sortbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogSort = new Dialog(Anniversary.this);
                dialogSort.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogSort.setContentView(R.layout.dailog_sort_items);
                dialogSort.setCanceledOnTouchOutside(true);
                dialogSort.setCancelable(true);
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                Window window = dialogSort.getWindow();
                layoutParams.copyFrom(window.getAttributes());
                // This makes the dialog take up the full width
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(layoutParams);
                final RadioGroup sort_group = dialogSort.findViewById(R.id.sort_group);
//                rbPopularity = (RadioButton) dialogCurrency.findViewById(R.id.rbPopularity);
                rb_price_low_to_high = (RadioButton) dialogSort.findViewById(R.id.rb_price_low_to_high);
                rb_price_high_to_low = (RadioButton) dialogSort.findViewById(R.id.rb_price_high_to_low);
                rb_new_arrivals = (RadioButton) dialogSort.findViewById(R.id.rb_new_arrivals);
                cardViewSubmit = dialogSort.findViewById(R.id.btnapply);
                cardViewCancel = dialogSort.findViewById(R.id.btncancel);

                cardViewSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int selectedId = sort_group.getCheckedRadioButtonId();
                        RadioButton selectedbtn = dialogSort.findViewById(selectedId);
                        sortId = selectedbtn.getText().toString();
                        product_arrayList.clear();
                        checkValuesAndCallApi();
                        dialogSort.dismiss();

                    }
                });

                cardViewCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogSort.dismiss();
                    }
                });
                dialogSort.show();
            }
        });
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.rview_anniversary);
        manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
    }

    private void setUprecyclerView() {
        if (product_arrayList == null && product_arrayList.size() == 0) {
        } else {
            proListAdapter = new SubProListAdapter(Anniversary.this, product_arrayList, "Anniversary");
            recyclerView.setAdapter(proListAdapter);
        }
    }

    private void checkValuesAndCallApi() {
        if (getIntent() != null && getIntent().getStringExtra("id_subcat") != null && !getIntent().getStringExtra("id_subcat").equalsIgnoreCase(null)) {
            subcat_id = getIntent().getStringExtra("id_subcat");
            subcat_ttl = getIntent().getStringExtra("title_subcat");
            tv_title.setText(subcat_ttl);

            if (getIntent().getStringExtra("low_prc") != null) {
                brnd_ids = getIntent().getStringExtra("brand_ids");
                lw_price = getIntent().getStringExtra("low_prc");
                hgh_price = getIntent().getStringExtra("high_prc");
            }
            final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
            Call<JsonElement> callRetrofit = null;

            if (sortId.trim().length() > 0) {
                if (sortId.equalsIgnoreCase(getResources().getString(R.string.price_low_to_high)))
                    callRetrofit = service.SORT_BY_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""), subcat_id, "1", "", "");
                else if (sortId.equalsIgnoreCase(getResources().getString(R.string.price_high_to_low)))
                    callRetrofit = service.SORT_BY_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""), subcat_id, "", "1", "");
                else if (sortId.equalsIgnoreCase(getResources().getString(R.string.new_arrivals)))
                    callRetrofit = service.SORT_BY_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""), subcat_id, "", "", "1");

            } else if (lw_price.trim().length() > 0) {
                callRetrofit = service.FILTERS_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""), subcat_id, brnd_ids, lw_price, hgh_price);
            } else {
                callRetrofit = service.SUB_PRODUCT_LIST_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), subcat_id, preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""));
            }

            final ProgressDialog progressDoalog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
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
                                JSONArray subcat_product_array = responseObject.getJSONArray("data");
                                product_arrayList = new ArrayList();
                                for (int i = 0; i < subcat_product_array.length(); i++) {
                                    JSONObject sub_cat_pdt_object = subcat_product_array.getJSONObject(i);
                                    SubProductListModel subProductListModel = new SubProductListModel();
                                    subProductListModel.setProd_id(sub_cat_pdt_object.getString("prod_id"));
                                    subProductListModel.setPname(sub_cat_pdt_object.getString("pname"));
                                    subProductListModel.setBrand_name(sub_cat_pdt_object.getString("brand_name"));
                                    subProductListModel.setProd_image(base_path + sub_cat_pdt_object.getString("prod_image"));
                                    if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.SAR)) {
                                        subProductListModel.setRegular_price_sar(sub_cat_pdt_object.getString("regular_price_sar"));
                                        subProductListModel.setPrice_sar(sub_cat_pdt_object.getString("price_sar"));
                                    } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.AED)) {
                                        subProductListModel.setPrice_aed(sub_cat_pdt_object.getString("price_aed"));
                                        subProductListModel.setRegular_price_aed(sub_cat_pdt_object.getString("regular_price_aed"));
                                    } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.KWD)) {
                                        subProductListModel.setPrice_kwd(sub_cat_pdt_object.getString("price_kwd"));
                                        subProductListModel.setRegular_price_kwd(sub_cat_pdt_object.getString("regular_price_kwd"));
                                    } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.USD)) {
                                        subProductListModel.setPrice_usd(sub_cat_pdt_object.getString("price_usd"));
                                        subProductListModel.setRegular_price_usd(sub_cat_pdt_object.getString("regular_price_usd"));
                                    } else {
                                        subProductListModel.setRegular_price_sar(sub_cat_pdt_object.getString("regular_price_sar"));
                                        subProductListModel.setPrice_sar(sub_cat_pdt_object.getString("price_sar"));
                                    }
                                    subProductListModel.setIs_wish_list(Integer.parseInt(sub_cat_pdt_object.getString("is_wish_list")));
                                    product_arrayList.add(subProductListModel);

                                }
                                setUprecyclerView();
                            } else
                                Toast.makeText(Anniversary.this, responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
//                            e.printStackTrace();
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

    @Override
    protected void onResume() {
        super.onResume();
        checkValuesAndCallApi();
    }
}