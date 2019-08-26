package com.volive.klueapp.adpaters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.volive.klueapp.activities.ShoppingCart;
import com.volive.klueapp.models.CartItemModel;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.API_class;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.PreferenceUtils;
import com.volive.klueapp.utils.Retrofit_funtion_class;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.CartHolder> {
   public static ShoppingCartAdapter adapter;
    Context context;
   public static ArrayList<CartItemModel> cart_list;
    PreferenceUtils preferenceUtils;

    public ShoppingCartAdapter(Context shoppingCart, ArrayList<CartItemModel> cart_array) {
        this.context = shoppingCart;
        this.cart_list = cart_array;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_view, parent, false);
        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartHolder holder, final int position) {
        preferenceUtils=new PreferenceUtils(context);
        holder.item_title.setText(cart_list.get(position).getPname());
        holder.item_brand_type.setText(cart_list.get(position).getBrand_name());
        holder.pdt_num.setText(cart_list.get(position).getQuantity());
        holder.result.setText(cart_list.get(position).getQuantity());

        if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.SAR)) {
            holder.price_offer.setText(cart_list.get(position).getPrice_sar());
            holder.price_type_offer.setText(Constants.SAR);
        } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.AED)) {
            holder.price_offer.setText(cart_list.get(position).getPrice_aed());
            holder.price_type_offer.setText(Constants.AED);
        } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.KWD)) {
            holder.price_offer.setText(cart_list.get(position).getPrice_kwd());
            holder.price_type_offer.setText(Constants.KWD);
        } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.USD)) {
            holder.price_offer.setText(cart_list.get(position).getPrice_usd());
            holder.price_type_offer.setText(Constants.USD);
        } else {
            holder.price_offer.setText(cart_list.get(position).getPrice_sar());
            holder.price_type_offer.setText(Constants.SAR);
        }

        Glide.with(context).load(cart_list.get(position).getProd_image()).into(holder.item_img);

        holder.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ttlcnt = Integer.parseInt(holder.result.getText().toString());
                String cnt = String.valueOf(ttlcnt + 1);
                holder.pdt_num.setText(cnt);
                holder.result.setText(cnt);
                add_to_cart_service(holder);
                CartItemModel cartItemModel = cart_list.get(holder.getAdapterPosition());
                cartItemModel.setQuantity(cnt);
                cart_list.set(holder.getAdapterPosition(), cartItemModel);
            }
        });

        holder.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ttlcnt = Integer.parseInt(holder.result.getText().toString());

                if (ttlcnt == 1) {
                    CartItemModel cartItemModel = cart_list.get(holder.getAdapterPosition());
                    cartItemModel.setQuantity("1");
                    cart_list.set(holder.getAdapterPosition(), cartItemModel);

                } else {
                    String cnt = String.valueOf(ttlcnt - 1);
                    holder.pdt_num.setText(cnt);
                    holder.result.setText(cnt);
                    del_from_cart_service(holder);
                    /*if (context instanceof ShoppingCart) {
                        ((ShoppingCart) context).methodtotalprc(Double.parseDouble(cart_list.get(holder.getAdapterPosition()).getPrice_sar()), false);
                    }*/
                    CartItemModel cartItemModel = cart_list.get(holder.getAdapterPosition());
                    cartItemModel.setQuantity(cnt);
                    cart_list.set(holder.getAdapterPosition(), cartItemModel);
                }
            }
        });
        holder.cancel_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCart.removeFromCart(context, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cart_list.size();
    }

    public void add_to_cart_service(RecyclerView.ViewHolder holder) {

        preferenceUtils = new PreferenceUtils(context);

        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.ADD_CART_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), cart_list.get(holder.getAdapterPosition()).getProd_id(), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""));

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
                            String message = responseObject.getString("message");
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            ((ShoppingCart) context).get_cart_services();
//                            ShoppingCart.setTextView(responseObject.getString("sub_total"));
                        }
                    } catch (Exception e) {
                        Log.e("error", e.getMessage());
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

    public void del_from_cart_service(RecyclerView.ViewHolder holder) {

        preferenceUtils = new PreferenceUtils(context);

        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.DECREMENT_CART_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), cart_list.get(holder.getAdapterPosition()).getProd_id(), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""));

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
                            String message = responseObject.getString("message");
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                            ShoppingCart.setTextView(responseObject.getString("sub_total"));
//                            Toast.makeText(context, cnt, Toast.LENGTH_SHORT).show();
                            ((ShoppingCart) context).get_cart_services();
                        }
                    } catch (Exception e) {
//                        e.printStackTrace();
                        Log.e("error", e.getMessage());
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

    public class CartHolder extends RecyclerView.ViewHolder {
        ImageView item_img, cancel_item;
        TextView item_title, item_brand_type, qty, decrement, result, increment, price_type_offer, price_offer, pdt_multiply, pdt_num;
        public CartHolder(View itemView) {
            super(itemView);
            item_img = itemView.findViewById(R.id.item_img);
            cancel_item = itemView.findViewById(R.id.cancel_item);
            qty = itemView.findViewById(R.id.qty);
            decrement = itemView.findViewById(R.id.decrement);
            result = itemView.findViewById(R.id.result);
            increment = itemView.findViewById(R.id.increment);
            item_title = itemView.findViewById(R.id.item_title);
            item_brand_type = itemView.findViewById(R.id.item_brand_type);
            price_type_offer = itemView.findViewById(R.id.price_type_offer);
            price_offer = itemView.findViewById(R.id.price_offer);
            pdt_multiply = itemView.findViewById(R.id.pdt_multiply);
            pdt_num = itemView.findViewById(R.id.pdt_num);

        }

        public void setText(String text) {
            result.setText(text);
        }
    }

}