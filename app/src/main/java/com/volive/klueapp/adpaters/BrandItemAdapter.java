package com.volive.klueapp.adpaters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.volive.klueapp.activities.Brand;
import com.volive.klueapp.models.BrandModel;
import com.volive.klueapp.R;

import java.util.ArrayList;

public class BrandItemAdapter extends RecyclerView.Adapter<BrandItemAdapter.BrandHolder> {
    Context context;
    ArrayList<BrandModel> brand_list;
    public static int brandPosition;

    public BrandItemAdapter(Brand brand, ArrayList<BrandModel> brand_list_array) {
        this.context=brand;
        this.brand_list=brand_list_array;
    }

    @NonNull
    @Override
    public BrandHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.brand_item_view,parent,false);
        return new BrandHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandHolder holder, final int position) {
        final BrandModel brandModel = brand_list.get(position);
        holder.txt_name.setText(brandModel.getName());

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    brandModel.setChecked(true);
                }
                else {
                    brandModel.setChecked(false);
                }
            }
        });
    }

    public String getSelectedItem() {
        String checkeditems = "";
        for (int i = 0; i <= brand_list.size()-1; i++) {
            BrandModel model = brand_list.get(i);
            if (model.getChecked()) {
                if (checkeditems.length()>0) {
                    checkeditems = checkeditems + "," + model.getId();
                } else {
                    checkeditems = model.getId();
                }
            }
        }
        return checkeditems;
    }

    @Override
    public int getItemCount() {
        return brand_list.size();
    }

    public class BrandHolder extends RecyclerView.ViewHolder{
        LinearLayout layout;
        TextView txt_name;
        CheckBox checkbox;
        public BrandHolder(View itemView) {
            super(itemView);
            txt_name=itemView.findViewById(R.id.brand_name);
            layout=itemView.findViewById(R.id.layout_brand);
            checkbox=itemView.findViewById(R.id.brand_select_checkbox);
        }
    }
}
