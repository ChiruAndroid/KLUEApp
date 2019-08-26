package com.volive.klueapp.adpaters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.volive.klueapp.activities.GiftsCardsActivity;
import com.volive.klueapp.models.Title_model;
import com.volive.klueapp.R;

import java.util.ArrayList;

public class GiftsAdapter extends RecyclerView.Adapter<GiftsAdapter.GiftHolder> {
    Context context;
    ArrayList<Title_model> title_models_list;
    LinearLayoutManager layoutManager;

    public GiftsAdapter(GiftsCardsActivity giftsCardsActivity, ArrayList<Title_model> title_models) {
        this.title_models_list = title_models;
        this.context = giftsCardsActivity;
    }

    @NonNull
    @Override
    public GiftHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gift_cart_recycle_item, parent, false);
        return new GiftHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GiftHolder holder, int position) {
        holder.gift_item_number.setText(title_models_list.get(position).getTitles());
        holder.tv_gift_card_name.setText(title_models_list.get(position).getTitleName());
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.rview_gifts_list.setLayoutManager(layoutManager);
        holder.rview_gifts_list.setNestedScrollingEnabled(false);
        holder.rview_gifts_list.setAdapter(new Inner_list_adpter(context, title_models_list.get(position).getGiftItemModels(), holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return title_models_list.size();
    }

    public class GiftHolder extends RecyclerView.ViewHolder {
        TextView tv_gift_card_name, gift_item_number;
        RecyclerView rview_gifts_list;
        public GiftHolder(View itemView) {
            super(itemView);
            tv_gift_card_name = itemView.findViewById(R.id.tv_gift_card_name);
            gift_item_number = itemView.findViewById(R.id.gift_item_number);
            rview_gifts_list = itemView.findViewById(R.id.rview_gifts_list);
        }
    }
}