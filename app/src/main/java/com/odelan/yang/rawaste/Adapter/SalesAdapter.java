package com.odelan.yang.rawaste.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.rawaste.Model.Order;
import com.odelan.yang.rawaste.Model.Product;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.Interface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ViewHolder> {
    Context context;
    List<Order> orders;
    EventListener listener;
    public SalesAdapter(Context context, List<Order> orders, EventListener listener) {
        this.context = context;
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SalesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_sales, viewGroup, false);
        SalesAdapter.ViewHolder vh = new SalesAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Order order = orders.get(position);
        Product.getProductWithID(order.PID, new Interface.OnResult<Product>() {
            @Override
            public void onSuccess(Product result) {
                Glide.with(context).load(result.images.get(0)).into(viewHolder.img_product);
                viewHolder.txt_product_name.setText(result.name);
                viewHolder.txt_product_description.setText(result.description);
            }
            @Override
            public void onFailure(String error) {
            }
        });
        Date myDate = new Date(order.timestamp * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        viewHolder.txt_order_date.setText(dateFormat.format(myDate));
        viewHolder.txt_order_number.setText(order.ID);
        String price = String.format("%.2f", order.total);
        viewHolder.txt_price.setText(price.substring(0, price.indexOf(".")));
        viewHolder.txt_price_decimal.setText(price.substring(price.indexOf(".") + 1));

        viewHolder.layout_parent.setOnClickListener(v -> {
            if (listener != null) listener.onClickItem(position);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout_parent;
        ImageView img_product;
        TextView txt_product_name;
        TextView txt_product_description;
        TextView txt_order_date;
        TextView txt_order_number;
        TextView txt_price;
        TextView txt_price_decimal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent = itemView.findViewById(R.id.layout_parent);
            img_product = itemView.findViewById(R.id.img_product);
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            txt_product_description = itemView.findViewById(R.id.txt_product_description);
            txt_order_date = itemView.findViewById(R.id.txt_order_date);
            txt_order_number = itemView.findViewById(R.id.txt_order_number);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_price_decimal = itemView.findViewById(R.id.txt_price_decimal);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
    }
}
