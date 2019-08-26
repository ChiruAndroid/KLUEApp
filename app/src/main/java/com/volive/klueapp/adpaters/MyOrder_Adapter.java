package com.volive.klueapp.adpaters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.volive.klueapp.activities.OrderDetails;
import com.volive.klueapp.models.OrdersModel;
import com.volive.klueapp.R;

import java.util.ArrayList;


public class MyOrder_Adapter extends RecyclerView.Adapter<MyOrder_Adapter.MyOrder_Holder> {

    public static ArrayList<OrdersModel> order_list;
    public static int orderPosition;
    Context context;
    String fragmentName;


    public MyOrder_Adapter(FragmentActivity fragmentActivity, ArrayList<OrdersModel> myOrders_list, String fragmentName) {
        this.context = fragmentActivity;
        this.order_list = myOrders_list;
        this.fragmentName = fragmentName;
    }

    @Override
    public MyOrder_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_view, parent, false);
        return new MyOrder_Holder(view);
    }

    @Override
    public void onBindViewHolder(final MyOrder_Holder holder, final int position) {


        holder.tv_price_total.setText(order_list.get(position).getSub_total());

        holder.orderCardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderPosition = position;
                Intent intent = new Intent(context, OrderDetails.class);
                intent.putExtra("my_orders", order_list.get(position).getOrder_id());
                intent.putExtra("order_id", order_list.get(position).getOrder_id());
                intent.putExtra("order_date", order_list.get(position).getOrder_date());
                intent.putExtra("estimate_date", order_list.get(position).getEstimate_date());
                intent.putExtra("currency_type", order_list.get(position).getCurrency_type());
                intent.putExtra("order_status",order_list.get(position).getOrder_status());
                intent.putExtra("payment_type",order_list.get(position).getPayment_type());
                context.startActivity(intent);
            }
        });
        holder.order_date.setText(order_list.get(position).getOrder_date());
        holder.tv_order_id.setText(order_list.get(position).getOrder_id());
        holder.estimate_date.setText(order_list.get(position).getEstimate_date());
        holder.product_qty.setText(order_list.get(position).getNo_of_items());
        holder.tv_price_type.setText(order_list.get(position).getCurrency_type());
        holder.item_status.setText(order_list.get(position).getOrder_status());

    }

    @Override
    public int getItemCount() {
        return order_list.size();
    }

    public class MyOrder_Holder extends RecyclerView.ViewHolder {
        TextView order_date, tv_order_id, item_status, estimate_date, tv_product_qty, product_qty, tv_total_price, tv_price_type, tv_price_total;
        CardView orderCardItem;
        public MyOrder_Holder(View itemView) {
            super(itemView);
            orderCardItem = itemView.findViewById(R.id.orderCardItem);
            order_date = itemView.findViewById(R.id.order_date);
            tv_order_id = itemView.findViewById(R.id.order_id);
            item_status = itemView.findViewById(R.id.item_status);
            estimate_date = itemView.findViewById(R.id.estimate_date);
            tv_product_qty = itemView.findViewById(R.id.tv_product_qty);
            product_qty = itemView.findViewById(R.id.product_qty);
            tv_total_price = itemView.findViewById(R.id.tv_total_price);
            tv_price_type = itemView.findViewById(R.id.tv_price_type);
            tv_price_total = itemView.findViewById(R.id.tv_price_total);
        }
    }

}
