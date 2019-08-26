package com.volive.klueapp.adpaters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.volive.klueapp.activities.MainActivity;
import com.volive.klueapp.activities.SubcategoryActivity;
import com.volive.klueapp.models.Category_Model;
import com.volive.klueapp.R;

import java.util.ArrayList;


public class CatListAdapter extends RecyclerView.Adapter<CatListAdapter.Holder> {

    Context context;
    ArrayList<Category_Model> cat_list;

    public CatListAdapter(Context context, ArrayList<Category_Model> category_list) {
        this.cat_list = category_list;
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rview_cat_item_view, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        holder.cat_title.setText(cat_list.get(position).getCname());

        if (position == 0) {
            holder.cat_image.setImageResource(R.mipmap.all_categories_sml);
        } else {
            Glide.with(context)
                    .load(cat_list.get(position).getCat_image())
                    .into(holder.cat_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof MainActivity) {
                    if (position == 0) {
                        MainActivity.changeFragment(2);
                    } /*else if (cat_list.get(position).getCat_id().equalsIgnoreCase("6")) {
                        Intent occ_sub_intent = new Intent(context, OccationsSubCategory.class);
                        occ_sub_intent.putExtra("cat_id",cat_list.get(position).getCat_id());
                        occ_sub_intent.putExtra("title_nme",cat_list.get(position).getCname());
                        context.startActivity(occ_sub_intent);
                    }*/ else {
                        Intent sub_cat_intent = new Intent(context, SubcategoryActivity.class);
//                        sub_cat_intent.putExtra("Position", (position - 1) + "");
                        sub_cat_intent.putExtra("title_nme",cat_list.get(position).getCname());
                        sub_cat_intent.putExtra("cat_id",cat_list.get(position).getCat_id());
                        context.startActivity(sub_cat_intent);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cat_list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView cat_image;
        TextView cat_title;

        public Holder(final View itemView) {
            super(itemView);
            cat_image = itemView.findViewById(R.id.cat_image);
            cat_title = itemView.findViewById(R.id.cat_title);
        }

    }
}