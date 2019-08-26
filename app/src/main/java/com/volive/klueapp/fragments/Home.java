package com.volive.klueapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.klueapp.activities.NewArrivals;
import com.volive.klueapp.models.Ads_Model;
import com.volive.klueapp.models.Category_Model;
import com.volive.klueapp.models.Home_banner_model;
import com.volive.klueapp.models.NewArrivalModel;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.API_class;
import com.volive.klueapp.utils.ConnectivityReceiver;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.CustomViewPager;
import com.volive.klueapp.utils.PreferenceUtils;
import com.volive.klueapp.utils.Retrofit_funtion_class;
import com.volive.klueapp.adpaters.AddsImageSliderAdapter;
import com.volive.klueapp.adpaters.CatListAdapter;
import com.volive.klueapp.adpaters.NewArrivalAdapter;
import com.volive.klueapp.adpaters.SlidingImage_Adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.volive.klueapp.utils.NetworkHelper.isInternetConnected;
import static com.volive.klueapp.utils.NetworkHelper.showDialog;

public class Home extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {
    public static ArrayList<Category_Model> category_list;
    public static ArrayList<NewArrivalModel> new_arrival_list;
    private static int currentPage = 0;
    private static int currentPage_add = 0;
    ArrayList<Category_Model> category_list_array;
    CustomViewPager customViewPager, customViewPager1;
    Button btn_viewAll;
    CatListAdapter adapter;
    NewArrivalAdapter arrivalAdapter;
    RecyclerView recyclerView, recyclerView1;
    ArrayList<Ads_Model> ads_array;
    ArrayList<Home_banner_model> images_array;
    PreferenceUtils preferenceUtils;
    ProgressDialog progressDoalog;

    int NUM_PAGES = 0;
    ViewPager viewPager;
    ImageView[] dots;
    int dotcount;
    SlidingImage_Adapter slidingImage_adapter;
    LinearLayout layoutIndicators;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home, container, false);

        Home home = new Home();

        preferenceUtils = new PreferenceUtils(getActivity());
        customViewPager = new CustomViewPager(getActivity());

        inializeUI(view);
        if (isInternetConnected(getActivity())) {
            home_fragment_service();
        } else {
            showDialog(getActivity(), SweetAlertDialog.WARNING_TYPE, "", String.valueOf(R.string.plz_check_internet_connection), "OK!",
                    new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            if (isInternetConnected(getActivity())) {
                                getActivity().recreate();
                            } else {
                                sweetAlertDialog.show();
                            }
                        }
                    });
        }
        return view;
    }

    private void inializeUI(View view) {
        //view All --New Arrivals
        btn_viewAll = view.findViewById(R.id.view_all);
        btn_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewArrivals.class);
                intent.putExtra("arraivals", new_arrival_list);
                getActivity().startActivity(intent);
            }
        });

        // RecyclerView Categories
        recyclerView = view.findViewById(R.id.rview_cat_list);
        LinearLayoutManager lManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(lManager);
        // RecyclerView New Arrivals
        recyclerView1 = view.findViewById(R.id.rview_new_arrivals);
        GridLayoutManager gManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView1.setLayoutManager(gManager);
        recyclerView1.setNestedScrollingEnabled(false);
//        customViewPager = view.findViewById(R.id.pager);
        customViewPager1 = view.findViewById(R.id.pager_adds);
        viewPager = view.findViewById(R.id.pager);
        layoutIndicators = view.findViewById(R.id.home_indicators);

    }

    public void home_fragment_service() {
        new_arrival_list = new ArrayList();
        category_list = new ArrayList<>();
        images_array = new ArrayList<>();
        ads_array = new ArrayList<>();

        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;

        callRetrofit = service.HOME_FRAGMENT_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""));

        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.hide();
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
                        JSONObject lObj = new JSONObject(searchResponse);
                        int status = lObj.getInt("status");
                        String message = lObj.getString("message");

                        if (status == 1) {
                            JSONObject data_Object = lObj.getJSONObject("data");
                            String base_path = lObj.getString("base_path");
                            JSONArray cat_array = data_Object.getJSONArray("categories");
                            category_list_array = new ArrayList<>();
                            Category_Model model = new Category_Model("-1", getResources().getString(R.string.all_categories), "");
                            category_list_array.add(model);
                            for (int i = 0; i < cat_array.length(); i++) {
                                JSONObject cat_object = cat_array.getJSONObject(i);
                                Category_Model category_model = new Category_Model();
                                category_model.setCat_id(cat_object.getString("cat_id"));
                                category_model.setCname(cat_object.getString("cname"));
                                category_model.setCat_image(base_path + cat_object.getString("cat_image"));

                                category_list.add(category_model);
                                category_list_array.add(category_model);
                            }

                            if (category_list == null && category_list.size() == 0) {
                                Toast.makeText(getActivity(), "No Items", Toast.LENGTH_LONG).show();
                            } else {
                                Log.e("catagery list", category_list.toString());
                                System.out.println("catasd" + category_list.toString());
                                adapter = new CatListAdapter(getActivity(), category_list_array);
                                recyclerView.setAdapter(adapter);
                            }

                            JSONArray pdt_array = data_Object.getJSONArray("products");
                            for (int k = 0; k < pdt_array.length(); k++) {
                                JSONObject pdt_object = pdt_array.getJSONObject(k);
                                NewArrivalModel newArrivalModel = new NewArrivalModel();
                                newArrivalModel.setProd_id(pdt_object.getString("prod_id"));
                                newArrivalModel.setPname(pdt_object.getString("pname"));
                                newArrivalModel.setBrand_name(pdt_object.getString("brand_name"));
                                newArrivalModel.setProd_image(base_path + pdt_object.getString("prod_image"));
                                if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.SAR)) {
                                    newArrivalModel.setPrice_sar(pdt_object.getString("price_sar"));
                                    newArrivalModel.setRegular_price_sar(pdt_object.getString("regular_price_sar"));
                                } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.AED)) {
                                    newArrivalModel.setPrice_aed(pdt_object.getString("price_aed"));
                                    newArrivalModel.setRegular_price_aed(pdt_object.getString("regular_price_aed"));
                                } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.KWD)) {
                                    newArrivalModel.setPrice_kwd(pdt_object.getString("price_kwd"));
                                    newArrivalModel.setRegular_price_kwd(pdt_object.getString("regular_price_kwd"));
                                } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.USD)) {
                                    newArrivalModel.setPrice_usd(pdt_object.getString("price_usd"));
                                    newArrivalModel.setRegular_price_usd(pdt_object.getString("regular_price_usd"));
                                } else {
                                    newArrivalModel.setPrice_sar(pdt_object.getString("price_sar"));
                                    newArrivalModel.setRegular_price_sar(pdt_object.getString("regular_price_sar"));
                                }
                                newArrivalModel.setIs_wish_list(pdt_object.getInt("is_wish_list"));
                                new_arrival_list.add(newArrivalModel);
                            }
                            if (new_arrival_list == null && new_arrival_list.size() == 0) {
                                recyclerView1.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), R.string.no_items, Toast.LENGTH_LONG).show();
                            } else if (new_arrival_list != null && new_arrival_list.size() == 2) {
                                recyclerView1.setVisibility(View.VISIBLE);
                                ViewGroup.LayoutParams params = recyclerView1.getLayoutParams();
                                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                recyclerView1.setLayoutParams(params);
                            } else if (new_arrival_list != null && new_arrival_list.size() > 2) {
                                recyclerView1.setVisibility(View.VISIBLE);
                                arrivalAdapter = new NewArrivalAdapter(getActivity(), new_arrival_list, "Home");
                                recyclerView1.setAdapter(arrivalAdapter);
                            }

                            JSONArray banner_array = data_Object.getJSONArray("banner");
                            for (int j = 0; j < banner_array.length(); j++) {
                                JSONObject banner_object = banner_array.getJSONObject(j);
                                Home_banner_model home_banner_model = new Home_banner_model();
                                home_banner_model.setBanner_id(banner_object.getString("banner_id"));
                                home_banner_model.setBanner_image(base_path + banner_object.getString("banner_image"));
                                home_banner_model.setBanner_link(banner_object.getString("banner_link"));
                                home_banner_model.setStatus(banner_object.getString("status"));
                                images_array.add(home_banner_model);
                            }

                            /*mPager.setAdapter(new ImageSliderAdapter(getActivity(), images_array, true));
                            setIndicator(indicator, mPager);*/
//                            customViewPager.setAdapter(new SlidingImage_Adapter(getActivity(),images_array,true));
                            slidingImage_adapter = new SlidingImage_Adapter(getActivity(), images_array);
                            viewPager.setAdapter(slidingImage_adapter);
//                            viewPager.setAdapter(new SlidingImage_Adapter(getActivity(), images_array));

                            home_banner_Ads();
//                            indicator.setViewPager(mPager);

                            JSONArray ads_Array = data_Object.getJSONArray("ads");

                            for (int l = 0; l < ads_Array.length(); l++) {
                                JSONObject ads_jsonObject = ads_Array.getJSONObject(l);

                                Ads_Model ads_model = new Ads_Model();
                                ads_model.setAds_id(ads_jsonObject.getString("id"));
                                ads_model.setImage(base_path + ads_jsonObject.getString("image"));

                                ads_array.add(ads_model);
                            }
                            customViewPager1.setAdapter(new AddsImageSliderAdapter(getActivity(), ads_array, true));
                            home_Ads();

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
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

    public void home_banner_Ads() {
        dotcount = slidingImage_adapter.getCount();
        dots = new ImageView[dotcount];

        for (int i = 0; i < dotcount; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.unselecetd_circle_indicater));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(10, 0, 10, 0);
            layoutIndicators.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selected_dot_drawable));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (isAdded()) {
                    for (int i = 0; i < dotcount; i++) {
                        // dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.unselect_dot_drwable));
                        dots[i].setImageDrawable(getResources().getDrawable(R.drawable.unselecetd_circle_indicater));
                    }
                    dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selected_dot_drawable));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        NUM_PAGES = images_array.size();

//        customViewPager.setDurationScroll(2000);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == images_array.size()) {
                    currentPage = 0;
                }
//                                    mPager.setDurationScroll(2000);
                viewPager.setCurrentItem(currentPage++, true);

            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);
    }

    public void home_Ads() {
//        customViewPager1.setDurationScroll(1000);
        final Handler handler = new Handler();
        final Runnable Adds_Update = new Runnable() {
            public void run() {
                if (currentPage_add == ads_array.size()) {
                    currentPage_add = 0;
                }
                customViewPager1.setCurrentItem(currentPage_add++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Adds_Update);
            }
        }, 3000, 3000);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Toast.makeText(getActivity(), isConnected + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (arrivalAdapter != null)
            arrivalAdapter.notifyDataSetChanged();
    }}