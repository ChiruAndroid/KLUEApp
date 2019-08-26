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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.volive.klueapp.activities.Anniversary;
import com.volive.klueapp.models.OccationsModel;
import com.volive.klueapp.R;

import java.util.ArrayList;
import java.util.List;

public class OccationsAdapter extends RecyclerView.Adapter<OccationsAdapter.OccationsHolder> implements Filterable{

    Context context;
    ArrayList<OccationsModel> occations_list;
    String ctgry_id;
    private List<OccationsModel> mFilteredList;

    public OccationsAdapter(FragmentActivity activity, ArrayList<OccationsModel> occationsModels_list, String ctg) {
        this.context = activity;
        this.occations_list = occationsModels_list;
        this.ctgry_id = ctg;
        this.mFilteredList = occationsModels_list;
    }

    @NonNull
    @Override
    public OccationsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item_view, parent, false);
        return new OccationsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OccationsHolder holder, final int position) {

        Glide.with(context)
                .load(mFilteredList.get(position).getSub_cat_image())
                .into(holder.category_image);
        holder.category_type.setText(mFilteredList.get(position).getSub_cat_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent sub_cat_pdt_intent = new Intent(context, Anniversary.class);
                 //   sub_cat_pdt_intent.putExtra("Position", position + "");
                    sub_cat_pdt_intent.putExtra("title_subcat",mFilteredList.get(position).getSub_cat_name());
                    sub_cat_pdt_intent.putExtra("id_subcat",mFilteredList.get(position).getSub_cat_id());
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
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = occations_list;
                } else {
                    List<OccationsModel> filteredList = new ArrayList<>();
                    for (OccationsModel occationsModel : occations_list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (occationsModel.getSub_cat_name().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(occationsModel);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<OccationsModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class OccationsHolder extends RecyclerView.ViewHolder {
        TextView category_type;
        ImageView category_image;
        CardView cardView;

        public OccationsHolder(View itemView) {
            super(itemView);
            category_type = itemView.findViewById(R.id.category_type);
            category_image = itemView.findViewById(R.id.category_image);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}