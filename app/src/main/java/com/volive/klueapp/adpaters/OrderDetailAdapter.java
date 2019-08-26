package com.volive.klueapp.adpaters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.volive.klueapp.models.OrderDetailModel;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.PreferenceUtils;

import java.util.ArrayList;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.Holder> {

    public static ArrayList<OrderDetailModel> list;
    Context context;
    String fragmentName;
    PreferenceUtils preferenceUtils;
    String st_currency_type;

    public OrderDetailAdapter(FragmentActivity activity, ArrayList<OrderDetailModel> new_list, String fragmentName, String st_currency_type) {
        this.context = activity;
        this.list = new_list;
        this.fragmentName = fragmentName;
        this.st_currency_type = st_currency_type;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_detail_item_view, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {

        preferenceUtils = new PreferenceUtils(context);
        holder.prod_name.setText(list.get(position).getProd_name());
        holder.brand.setText(list.get(position).getBrand_name());
        holder.price_value.setText(list.get(position).getPrice());
        holder.pdt_quantity.setText(list.get(position).getProduct_qty());
        Glide.with(context).load(list.get(position).getProd_image()).into(holder.order_img);

        holder.price_value.setText(list.get(position).getPrice());
        holder.price_type_offer.setText(st_currency_type);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView order_img;
        TextView prod_name, brand, price_type_offer, price_value, pdt_quantity;

        public Holder(View itemView) {
            super(itemView);
            order_img = itemView.findViewById(R.id.order_img);
            prod_name = itemView.findViewById(R.id.prod_name);
            brand = itemView.findViewById(R.id.brand);
            price_type_offer = itemView.findViewById(R.id.price_type_offer);
            price_value = itemView.findViewById(R.id.price_value);
            pdt_quantity = itemView.findViewById(R.id.pdt_quantity);


        }
    }

}