package com.volive.klueapp.adpaters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.volive.klueapp.activities.MainActivity;
import com.volive.klueapp.activities.OccationsSubCategory;
import com.volive.klueapp.activities.SubcategoryActivity;
import com.volive.klueapp.models.Category_Model;
import com.volive.klueapp.R;

import java.util.ArrayList;

public class nav_menu_item_adapter extends RecyclerView.Adapter<nav_menu_item_adapter.Menu_Holder> {

    Context context;
    ArrayList<Category_Model> menu_list;

    public nav_menu_item_adapter(Context context, ArrayList<Category_Model> menu_cat_list) {
        this.context=context;
        this.menu_list=menu_cat_list;
    }

    @NonNull
    @Override
    public Menu_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.list_item_view,parent,false);
        return new Menu_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Menu_Holder holder, final int position) {

        holder.menu_item_name.setText(menu_list.get(position).getCname());

        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof MainActivity) {

                    if (menu_list.get(position).getCat_id().equalsIgnoreCase("6")) {
                        Intent occ_sub_intent = new Intent(context, OccationsSubCategory.class);
                        occ_sub_intent.putExtra("cat_id", menu_list.get(position).getCat_id());
                        occ_sub_intent.putExtra("title_nme", menu_list.get(position).getCname());
                        context.startActivity(occ_sub_intent);
                    } else {
                        Intent sub_cat_intent = new Intent(context, SubcategoryActivity.class);
                        sub_cat_intent.putExtra("Position", (position) + "");
                        sub_cat_intent.putExtra("title_nme", menu_list.get(position).getCname());
                        sub_cat_intent.putExtra("cat_id", menu_list.get(position).getCat_id());
                        context.startActivity(sub_cat_intent);
                    }
                    MainActivity.mDrawerLayout.closeDrawers();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return menu_list.size();
    }

    public class Menu_Holder extends RecyclerView.ViewHolder{

        TextView menu_item_name;
        ImageView menu_item_img;
        LinearLayout layout_item;
        public Menu_Holder(View itemView) {
            super(itemView);

            menu_item_name=itemView.findViewById(R.id.menu_item_name);
            menu_item_img=itemView.findViewById(R.id.menu_item_img);
            layout_item=itemView.findViewById(R.id.layout_item);


        }
    }

}
