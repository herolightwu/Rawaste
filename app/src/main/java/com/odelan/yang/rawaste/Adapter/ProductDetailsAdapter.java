package com.odelan.yang.rawaste.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.odelan.yang.rawaste.Model.Product;
import com.odelan.yang.rawaste.R;

import java.util.List;

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.ViewHolder> {
    Context context;
    List<String> urls;
    EventListener listener;

    int selected_index = -1;

    public ProductDetailsAdapter(Context context, List<String> urls, EventListener listener) {
        this.context = context;
        this.urls = urls;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_product_details, viewGroup, false);
        ProductDetailsAdapter.ViewHolder vh = new ProductDetailsAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        Glide.with(context).load(urls.get(position)).into(viewHolder.img_item);
        viewHolder.onSelect(selected_index == position);
        viewHolder.layout_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_index = position;
                if (listener != null) {
                    listener.onClickItem(position);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout_parent;
        ImageView img_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent = itemView.findViewById(R.id.layout_parent);
            img_item = itemView.findViewById(R.id.img_item);
        }

        public void onSelect(Boolean flag) {
            if (flag == true) {
                layout_parent.setBackgroundResource(R.mipmap.product_image_selected);
            } else {
                layout_parent.setBackgroundResource(R.mipmap.product_image);
            }
        }
    }

    public interface EventListener {
        void onClickItem(int index);
    }
}
