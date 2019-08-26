package com.volive.klueapp.adpaters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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
import com.volive.klueapp.activities.GiftsCardsActivity;
import com.volive.klueapp.models.GiftItemModel;
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

class Inner_list_adpter extends RecyclerView.Adapter<Inner_list_adpter.GIftHolder> {

    ArrayList<GiftItemModel> giftListArray;
    Context context;
    PreferenceUtils preferenceUtils;
    boolean isPlus;
    Float amount;
    int ttlcnt;
    int verticalPosition;
    String cnt;


    public Inner_list_adpter(Context context, ArrayList<GiftItemModel> giftItemModels, int position) {
        this.context = context;
        this.giftListArray = giftItemModels;
        this.verticalPosition = position;
//        setHasStableIds(false);
        Log.e("Inner_list_adpter: ", "" + giftItemModels.size());
    }

    @NonNull
    @Override
    public GIftHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gift_item_view, parent, false);
        return new GIftHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GIftHolder holder, final int position) {
        preferenceUtils = new PreferenceUtils(context);
        holder.gift_brand_name.setText(giftListArray.get(position).getBrand_name());

        Glide.with(context).load(giftListArray.get(position).getProd_image()).into(holder.gift_image);

        holder.currency_type.setPaintFlags(holder.currency_type.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.currency_value.setPaintFlags(holder.currency_value.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.SAR)) {
            holder.currency_value.setText(giftListArray.get(position).getRegular_price_sar());
            holder.currency_value1.setText(giftListArray.get(position).getPrice_sar());
            holder.currency_type1.setText(Constants.SAR);
            holder.currency_type.setText(Constants.SAR);
        } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.AED)) {
            holder.currency_value.setText(giftListArray.get(position).getRegular_price_aed());
            holder.currency_value1.setText(giftListArray.get(position).getPrice_aed());
            holder.currency_type1.setText(Constants.AED);
            holder.currency_type.setText(Constants.AED);
        } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.KWD)) {
            holder.currency_value.setText(giftListArray.get(position).getRegular_price_kwd());
            holder.currency_value1.setText(giftListArray.get(position).getPrice_kwd());
            holder.currency_type1.setText(Constants.KWD);
            holder.currency_type.setText(Constants.KWD);
        } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.USD)) {
            holder.currency_value.setText(giftListArray.get(position).getRegular_price_usd());
            holder.currency_value1.setText(giftListArray.get(position).getPrice_usd());
            holder.currency_type1.setText(Constants.USD);
            holder.currency_type.setText(Constants.USD);
        } else {
            holder.currency_value.setText(giftListArray.get(position).getRegular_price_sar());
            holder.currency_value1.setText(giftListArray.get(position).getPrice_sar());
            holder.currency_type1.setText(Constants.SAR);
            holder.currency_type.setText(Constants.SAR);
        }

       /* holder.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttlcnt = Integer.parseInt(holder.count_item.getText().toString());
                holder.count_item.setText(String.valueOf(ttlcnt + 1));
                add_to_gift_service(holder);
            }
        });*/
        holder.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ttlcnt = Integer.parseInt(holder.count_item.getText().toString());
                cnt = String.valueOf(ttlcnt + 1);
                holder.count_item.setText(cnt);
//                notifyDataSetChanged();
                add_to_gift_service(holder);

            }
        });

        holder.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ttlcnt = Integer.parseInt(holder.count_item.getText().toString());
                if (ttlcnt > 1) {
                    cnt = String.valueOf(ttlcnt - 1);
                    holder.count_item.setText(cnt);
                    del_gift_service(holder);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return giftListArray.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void add_to_gift_service(final RecyclerView.ViewHolder holder) {
        preferenceUtils = new PreferenceUtils(context);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.GIFT_ADDCART_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), GiftsCardsActivity.title_models.get(verticalPosition).getGiftItemModels().get(holder.getAdapterPosition()).getProd_id(), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""),cnt);

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
                        if (status == 1) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            if (context instanceof GiftsCardsActivity) {
                                GiftsCardsActivity.title_models.get(verticalPosition).getGiftItemModels().get(holder.getAdapterPosition()).setQuantity(cnt);
                                ((GiftsCardsActivity) context).methodtotalprc(amount, isPlus, GiftsCardsActivity.title_models.get(verticalPosition).getGiftItemModels().get(holder.getAdapterPosition()), GiftsCardsActivity.title_models.get(verticalPosition).getGiftItemModels().get(holder.getAdapterPosition()).getProd_id(), holder.getAdapterPosition());
                            }
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

    public void del_gift_service(final RecyclerView.ViewHolder holder) {
        preferenceUtils = new PreferenceUtils(context);

        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.DECREMENT_CART_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), giftListArray.get(holder.getAdapterPosition()).getProd_id(), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""));

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
                            if (context instanceof GiftsCardsActivity) {
                                GiftsCardsActivity.title_models.get(verticalPosition).getGiftItemModels().get(holder.getAdapterPosition()).setQuantity(cnt);
                                ((GiftsCardsActivity) context).methodtotalprc(amount, isPlus, GiftsCardsActivity.title_models.get(verticalPosition).getGiftItemModels().get(holder.getAdapterPosition()), GiftsCardsActivity.title_models.get(verticalPosition).getGiftItemModels().get(holder.getAdapterPosition()).getProd_id(), holder.getAdapterPosition());
                            }
//                            ShoppingCart.setTextView(responseObject.getString("sub_total"));
//                            Toast.makeText(context, cnt, Toast.LENGTH_SHORT).show();
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

    public class GIftHolder extends RecyclerView.ViewHolder {
        CardView gift_item_view_layout;
        ImageView gift_image;
        TextView decrement, count_item, increment, gift_brand_name, currency_type, currency_value, currency_value1, currency_type1;

        public GIftHolder(View itemView) {
            super(itemView);
            gift_item_view_layout = itemView.findViewById(R.id.gift_item_view_layout);
            gift_image = itemView.findViewById(R.id.gift_image);
            decrement = itemView.findViewById(R.id.decrement);
            count_item = itemView.findViewById(R.id.count_item);
            increment = itemView.findViewById(R.id.increment);
            gift_brand_name = itemView.findViewById(R.id.gift_brand_name);
            currency_type = itemView.findViewById(R.id.currency_type);
            currency_type1 = itemView.findViewById(R.id.currency_type1);
            currency_value = itemView.findViewById(R.id.currency_value);
            currency_value1 = itemView.findViewById(R.id.currency_value1);
        }
    }
}