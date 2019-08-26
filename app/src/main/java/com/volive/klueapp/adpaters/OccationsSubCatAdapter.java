package com.volive.klueapp.adpaters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.volive.klueapp.activities.SubcategoryActivity;
import com.volive.klueapp.models.Category_Model;
import com.volive.klueapp.R;

import java.util.ArrayList;

public class OccationsSubCatAdapter extends RecyclerView.Adapter<OccationsSubCatAdapter.OccationsHolder> {

    Context context;
    ArrayList<Category_Model> occations_sub_list;

    public OccationsSubCatAdapter(FragmentActivity activity, ArrayList<Category_Model> occationsModels_list) {
        this.context = activity;
        this.occations_sub_list = occationsModels_list;
    }


    @NonNull
    @Override
    public OccationsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.occations_item_view, parent, false);

        return new OccationsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OccationsHolder holder, final int position) {


        holder.category_type.setText(occations_sub_list.get(position).getCname());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sub_cat_pdt_intent = new Intent(context, SubcategoryActivity.class);
                sub_cat_pdt_intent.putExtra("Position", position + "");
                sub_cat_pdt_intent.putExtra("title_nme", occations_sub_list.get(position).getCname());
                sub_cat_pdt_intent.putExtra("cat_id", occations_sub_list.get(position).getCat_id());
                sub_cat_pdt_intent.putExtra("Context", "OccationsAdapter");
                sub_cat_pdt_intent.putExtra("low_prc", "");
                sub_cat_pdt_intent.putExtra("high_prc", "");
                sub_cat_pdt_intent.putExtra("brand_ids", "");
                context.startActivity(sub_cat_pdt_intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return occations_sub_list.size();

    }

    public class OccationsHolder extends RecyclerView.ViewHolder {
        TextView category_type;
        CardView cardView;

        public OccationsHolder(View itemView) {
            super(itemView);
            category_type = itemView.findViewById(R.id.category_type);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
