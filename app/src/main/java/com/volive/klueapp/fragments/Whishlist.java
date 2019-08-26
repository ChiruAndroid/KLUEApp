package com.volive.klueapp.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.klueapp.activities.Anniversary;
import com.volive.klueapp.models.CartItemModel;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.API_class;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.PreferenceUtils;
import com.volive.klueapp.utils.Retrofit_funtion_class;
import com.volive.klueapp.adpaters.WhisList_Adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Whishlist extends Fragment {
    public static ArrayList<CartItemModel> whis_list_array;
    public static WhisList_Adapter adapter;
    RecyclerView recyclerView;
    PreferenceUtils preferenceUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.whishlist, container, false);
        preferenceUtils = new PreferenceUtils(getActivity());
        Whishlist whishlist=new Whishlist();
        initailizeUi(view);
        get_whislist_service();
        return view;
    }

    public void  initailizeUi(View view){
        recyclerView = view.findViewById(R.id.rview_whishlist);
        LinearLayoutManager lManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lManager);
    }

    private void setUpRecyclerView(int position) {
        adapter = new WhisList_Adapter(getActivity(), whis_list_array);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public static void addToWishList(final Context context, final int position, final String fragmentName) {
        String prod_id;
        if (fragmentName.equalsIgnoreCase("Home"))
            prod_id = Home.new_arrival_list.get(position).getProd_id();
        else if (fragmentName.equalsIgnoreCase("New Arrivals"))
            prod_id = Home.new_arrival_list.get(position).getProd_id();
        else if (fragmentName.equalsIgnoreCase("Anniversary"))
            prod_id = Anniversary.product_arrayList.get(position).getProd_id();
        else if (fragmentName.equalsIgnoreCase("Top Pics"))
            prod_id = TopPicsFragment.top_pics_list.get(position).getProd_id();
        else
            prod_id = Anniversary.product_arrayList.get(position).getProd_id();

        PreferenceUtils preferenceUtils = new PreferenceUtils(context);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.ADD_WHISLIST_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""), prod_id);

        final ProgressDialog progressDoalog = new ProgressDialog(context,R.style.AppCompatAlertDialogStyle);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.dismiss();
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
                        String message = responseObject.getString("message");
                        if (status == 1) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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

    public static void removeFromWhishList(final Context context, final int position, String fragmentName) {
        String prod_id;
        if (fragmentName.equalsIgnoreCase("Home"))
            prod_id = Home.new_arrival_list.get(position).getProd_id();
        else if (fragmentName.equalsIgnoreCase("New Arrivals"))
            prod_id = Home.new_arrival_list.get(position).getProd_id();
        else if (fragmentName.equalsIgnoreCase("Anniversary"))
            prod_id = Anniversary.product_arrayList.get(position).getProd_id();
        else if (fragmentName.equalsIgnoreCase("Top Pics"))
            prod_id = TopPicsFragment.top_pics_list.get(position).getProd_id();
        else
            prod_id = whis_list_array.get(position).getProd_id();

        PreferenceUtils preferenceUtils = new PreferenceUtils(context);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.DEL_FAVOURITE_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""), prod_id);

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
                        String message = responseObject.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        if (status == 1) {
                            whis_list_array.remove(position);
                            adapter.notifyItemRemoved(position);
                            adapter.notifyItemRangeChanged(position, whis_list_array.size());
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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

    public void get_whislist_service() {
        whis_list_array = new ArrayList();
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.GET_WHISLIST_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""));

        final ProgressDialog progressDoalog = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
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
                            JSONArray data_array = responseObject.getJSONArray("data");
                            String base_path = responseObject.getString("base_path");
                            for (int i = 0; i < data_array.length(); i++) {
                                JSONObject cat_object = data_array.getJSONObject(i);
                                CartItemModel cartItemModel = new CartItemModel();
                                cartItemModel.setProd_id(cat_object.getString("prod_id"));
                                cartItemModel.setPname(cat_object.getString("pname"));
                                cartItemModel.setBrand_name(cat_object.getString("brand_name"));
                                cartItemModel.setProd_image(base_path + cat_object.getString("prod_image"));
                                if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.SAR)) {
                                    cartItemModel.setRegular_price_sar(cat_object.getString("regular_price_sar"));
                                    cartItemModel.setPrice_sar(cat_object.getString("price_sar"));
                                } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.AED)) {
                                    cartItemModel.setPrice_aed(cat_object.getString("price_aed"));
                                    cartItemModel.setRegular_price_aed(cat_object.getString("regular_price_aed"));
                                } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.KWD)) {
                                    cartItemModel.setPrice_kwd(cat_object.getString("price_kwd"));
                                    cartItemModel.setRegular_price_kwd(cat_object.getString("regular_price_kwd"));
                                } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.USD)) {
                                    cartItemModel.setPrice_usd(cat_object.getString("price_usd"));
                                    cartItemModel.setRegular_price_usd(cat_object.getString("regular_price_usd"));
                                }else {
                                    cartItemModel.setRegular_price_sar(cat_object.getString("regular_price_sar"));
                                    cartItemModel.setPrice_sar(cat_object.getString("price_sar"));
                                }

                                cartItemModel.setChecked(true);
                                whis_list_array.add(cartItemModel);
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
}