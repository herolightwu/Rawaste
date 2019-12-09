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
import com.odelan.yang.rawaste.Model.Product;
import com.odelan.yang.rawaste.R;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    Context context;
    List<Product> products;
    EventListener listener;
    public FavoriteAdapter(Context context, List<Product> products, EventListener listener) {
        this.context = context;
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_favorite, viewGroup, false);
        FavoriteAdapter.ViewHolder vh = new FavoriteAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Glide.with(context).load(products.get(position).images.get(0)).into(viewHolder.img_product);
        viewHolder.txt_name.setText(products.get(position).name);
        viewHolder.txt_description.setText(products.get(position).description);
        viewHolder.txt_price.setText(String.valueOf((int)products.get(position).price));
        viewHolder.txt_country.setText(":    " + products.get(position).country_name);
        viewHolder.txt_city.setText(":    " + products.get(position).city);
        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickDelete(position);
            }
        });
        viewHolder.layout_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout_parent;
        ImageView img_product;
        TextView txt_name;
        TextView txt_description;
        TextView txt_price;
        TextView txt_city;
        TextView txt_country;
        ImageView btn_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent = itemView.findViewById(R.id.layout_parent);
            img_product = itemView.findViewById(R.id.img_product);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_description = itemView.findViewById(R.id.txt_description);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_city = itemView.findViewById(R.id.txt_city);
            txt_country = itemView.findViewById(R.id.txt_country);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
        void onClickDelete(int index);
    }
}
