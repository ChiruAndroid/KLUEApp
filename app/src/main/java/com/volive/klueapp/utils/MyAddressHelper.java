package com.volive.klueapp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.klueapp.models.AddressModel;
import com.volive.klueapp.R;
import com.volive.klueapp.adpaters.AddressAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAddressHelper {
    static PreferenceUtils preferenceUtils;
    public static ArrayList<AddressModel> address_list_array;
    public static AddressAdapter addressAdapter;
    Context context;

    public MyAddressHelper(Context context) {
        this.context = context;
    }

    public void getShippingAddressService(final Context context1, final RecyclerView recyclerView) {
        preferenceUtils=new PreferenceUtils(context1);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.GET_ADDRESS_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language,""), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""));

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(context1, R.style.AppCompatAlertDialogStyle);
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
                            address_list_array = new ArrayList();
                            for (int i = 0; i < data_array.length(); i++) {
                                JSONObject address_object = data_array.getJSONObject(i);
                                AddressModel addressModel = new AddressModel();
                                addressModel.setShipping_id(address_object.getString("shipping_id"));
                                addressModel.setUsername(address_object.getString("username"));
                                addressModel.setEmail(address_object.getString("email"));
                                addressModel.setAddress(address_object.getString("address"));
                                addressModel.setCity(address_object.getString("city"));
                                addressModel.setMobile(address_object.getString("mobile"));
                                addressModel.setLandmark(address_object.getString("landmark"));
                                address_list_array.add(addressModel);
                            }

                            setUpRecyclerView(context1,recyclerView);
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

    private void setUpRecyclerView(Context context1, RecyclerView recyclerView) {

        if(address_list_array !=  null){
            addressAdapter = new AddressAdapter(context1, address_list_array);
            recyclerView.setAdapter(addressAdapter);
            addressAdapter.notifyDataSetChanged();
        }else {

        }
    }

    public static void delAddressService(final Context context, final int position) {
        PreferenceUtils preferenceUtils=new PreferenceUtils(context);
        String shipping_id = MyAddressHelper.address_list_array.get(position).getShipping_id();
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.DEL_ADDRESS_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language,""),shipping_id,preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""));

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(context);
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
                        String message = responseObject.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            address_list_array.remove(position);
                            addressAdapter.notifyDataSetChanged();
                            addressAdapter.notifyItemRemoved(position);
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
