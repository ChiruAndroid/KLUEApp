package com.volive.klueapp.adpaters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.klueapp.activities.AddNewAddress;
import com.volive.klueapp.models.AddressModel;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.API_class;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.MyAddressHelper;
import com.volive.klueapp.utils.PreferenceUtils;
import com.volive.klueapp.utils.Retrofit_funtion_class;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.Address_holder> {

    public static int addressPosition = -1;
    Context context;
    ArrayList<AddressModel> address_list;

    public AddressAdapter(Context context1, ArrayList<AddressModel> address_list_array) {
        this.context = context1;
        this.address_list = address_list_array;
    }

    @Override
    public Address_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.address_list_item, parent, false);
        return new Address_holder(view);
    }

    @Override
    public void onBindViewHolder(final Address_holder holder, final int position) {

        holder.edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddNewAddress.class);
                intent.putExtra("shipping_id", address_list.get(position).getShipping_id());
                intent.putExtra("username", address_list.get(position).getUsername());
                intent.putExtra("email", address_list.get(position).getEmail());
                intent.putExtra("mobile", address_list.get(position).getMobile());
                intent.putExtra("address", address_list.get(position).getAddress());
                intent.putExtra("city", address_list.get(position).getCity());
                intent.putExtra("landmark", address_list.get(position).getLandmark());
                context.startActivity(intent);
            }
        });

        holder.delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAddressService(context, position);
            }
        });

        holder.name.setText(address_list.get(position).getUsername());
        holder.mail.setText(address_list.get(position).getEmail());
        holder.phn_number.setText(address_list.get(position).getMobile());
        holder.address_line.setText(address_list.get(position).getAddress());
        holder.city.setText(address_list.get(position).getCity());
        holder.landmark.setText(address_list.get(position).getLandmark());

        holder.radioButton.setChecked(addressPosition == position);

    }

    private void deleteAddressService(final Context context, final int position) {
        PreferenceUtils preferenceUtils = new PreferenceUtils(context);
        String shipping_id = MyAddressHelper.address_list_array.get(position).getShipping_id();
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.DEL_ADDRESS_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), shipping_id, preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""));

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(context,R.style.AppCompatAlertDialogStyle);
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
                        address_list.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
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
    public int getItemCount() {
        return address_list.size();
    }

    public class Address_holder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        TextView name, address_line, city, mail, phn_number, landmark;
        ImageView edit_icon, delete_icon;

        public Address_holder(View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radioButton);
            name = itemView.findViewById(R.id.name);
            landmark = itemView.findViewById(R.id.landmark);
            address_line = itemView.findViewById(R.id.address_line);
            city = itemView.findViewById(R.id.city);
            mail = itemView.findViewById(R.id.mail);
            phn_number = itemView.findViewById(R.id.phn_number);
            edit_icon = itemView.findViewById(R.id.edit_icon);
            delete_icon = itemView.findViewById(R.id.delete_icon);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addressPosition = getAdapterPosition();
                    address_list.get(getAdapterPosition()).getShipping_id();
                    notifyDataSetChanged();
                }
            });
        }
    }

}