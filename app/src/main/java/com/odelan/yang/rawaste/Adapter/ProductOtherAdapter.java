package com.odelan.yang.rawaste.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.odelan.yang.rawaste.Model.Announcement;
import com.odelan.yang.rawaste.Model.Product;
import com.odelan.yang.rawaste.R;

import java.util.List;

public class ProductOtherAdapter extends RecyclerView.Adapter<ProductOtherAdapter.ViewHolder> {
    Context context;
    List<Product> products;
    EventListener listener;
    public ProductOtherAdapter(Context context, List<Product> products, EventListener listener) {
        this.context = context;
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductOtherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_product_other, viewGroup, false);
        ProductOtherAdapter.ViewHolder vh = new ProductOtherAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.layout_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
//        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout_parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent = itemView.findViewById(R.id.layout_parent);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
    }
}
