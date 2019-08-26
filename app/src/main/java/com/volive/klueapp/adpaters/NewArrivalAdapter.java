package com.volive.klueapp.adpaters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.volive.klueapp.activities.Login;
import com.volive.klueapp.activities.ProductDetails;
import com.volive.klueapp.fragments.Home;
import com.volive.klueapp.fragments.Whishlist;
import com.volive.klueapp.models.NewArrivalModel;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.PreferenceUtils;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.volive.klueapp.activities.BaseActivity.showDialog;

public class NewArrivalAdapter extends RecyclerView.Adapter<NewArrivalAdapter.Holder> {

    ArrayList<NewArrivalModel> list;
    Context context;
    String fragmentName;
    PreferenceUtils preferenceUtils;

    public NewArrivalAdapter(Context context, ArrayList<NewArrivalModel> new_list, String fragmentName) {
        this.context = context;
        this.list = new_list;
        this.fragmentName = fragmentName;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_view, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        preferenceUtils = new PreferenceUtils(context);
        holder.pdt_price_off.setPaintFlags(holder.pdt_price_off.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.pdt_price_type.setPaintFlags(holder.pdt_price_type.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.pdt_name.setText(list.get(position).getPname());
        holder.pdt_brand.setText(list.get(position).getBrand_name());
        holder.pdt_price_off.setText(list.get(position).getRegular_price_sar());
        if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.SAR)) {
            holder.pdt_price_off.setText(list.get(position).getRegular_price_sar());
            holder.pdt_price.setText(list.get(position).getPrice_sar());
            holder.price_type.setText(Constants.SAR);
            holder.pdt_price_type.setText(Constants.SAR);
        } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.AED)) {
            holder.pdt_price.setText(list.get(position).getPrice_aed());
            holder.pdt_price_off.setText(list.get(position).getRegular_price_aed());
            holder.price_type.setText(Constants.AED);
            holder.pdt_price_type.setText(Constants.AED);
        } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.KWD)) {
            holder.pdt_price.setText(list.get(position).getPrice_kwd());
            holder.pdt_price_off.setText(list.get(position).getRegular_price_kwd());
            holder.price_type.setText(Constants.KWD);
            holder.pdt_price_type.setText(Constants.KWD);
        } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.USD)) {
            holder.pdt_price.setText(list.get(position).getPrice_usd());
            holder.pdt_price_off.setText(list.get(position).getRegular_price_usd());
            holder.pdt_price_type.setText(Constants.USD);
            holder.price_type.setText(Constants.USD);
        } else {
            holder.pdt_price_off.setText(list.get(position).getRegular_price_sar());
            holder.pdt_price.setText(list.get(position).getPrice_sar());
            holder.price_type.setText(Constants.SAR);
            holder.pdt_price_type.setText(Constants.SAR);
        }


        if (list.get(position).getIs_wish_list() == 1) {
            list.get(position).setChecked(true);
            holder.heart.setImageResource(R.mipmap.whishlist_hvr);
        } else {
            list.get(position).setChecked(false);
            holder.heart.setImageResource(R.mipmap.heart);
        }
        Glide.with(context)
                .load(list.get(position).getProd_image())
                .into(holder.image);
        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!preferenceUtils.getbooleanFromPreference(preferenceUtils.LOG_OUT, true)) {
                    if (!list.get(position).isChecked()) {
                        Home.new_arrival_list.get(position).setChecked(true);
                        Home.new_arrival_list.get(position).setIs_wish_list(1);
                        Whishlist.addToWishList(context, position, fragmentName);
                        holder.heart.setImageResource(R.mipmap.whishlist_hvr);
                        notifyItemChanged(position);
                        notifyDataSetChanged();
                    } else {
                        Home.new_arrival_list.get(position).setChecked(false);
                        Home.new_arrival_list.get(position).setIs_wish_list(0);
                        Whishlist.removeFromWhishList(context, position, fragmentName);
                        holder.heart.setImageResource(R.mipmap.heart);
                        notifyItemChanged(position);
                        notifyDataSetChanged();
                    }
                } else {
                    showDialog(context, SweetAlertDialog.WARNING_TYPE, "", context.getString(R.string.plz_login_to_save_into_fav), "OK!",
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent intent = new Intent(context, Login.class);
                                    context.startActivity(intent);
                                }
                            });
                }
            }
        });

        holder.grid_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pdt_detail_intent = new Intent(context, ProductDetails.class);
                pdt_detail_intent.putExtra("Position", position + "");
                pdt_detail_intent.putExtra("Context", fragmentName);
                context.startActivity(pdt_detail_intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView image, heart;
        TextView pdt_name, pdt_brand, pdt_price, pdt_price_off, price_type, pdt_price_type;
        LinearLayout grid_item;

        public Holder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            price_type = itemView.findViewById(R.id.price_type);
            pdt_price_type = itemView.findViewById(R.id.pdt_price_type);
            heart = itemView.findViewById(R.id.heart);
            pdt_name = itemView.findViewById(R.id.pdt_name);

            pdt_brand = itemView.findViewById(R.id.pdt_brand);
            pdt_price = itemView.findViewById(R.id.pdt_price);
            pdt_price_off = itemView.findViewById(R.id.pdt_price_off);
            grid_item = itemView.findViewById(R.id.grid_item);
        }
    }
}