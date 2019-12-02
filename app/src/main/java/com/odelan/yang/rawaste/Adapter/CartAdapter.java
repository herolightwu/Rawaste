package com.odelan.yang.rawaste.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.rawaste.Model.Cart;
import com.odelan.yang.rawaste.Model.Product;
import com.odelan.yang.rawaste.Model.User;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.Interface;

import java.util.List;

import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    List<Product> carts;
    List<Integer> counts;
    EventListener listener;
    public CartAdapter(Context context, List<Product> carts, List<Integer> counts, EventListener listener) {
        this.context = context;
        this.carts = carts;
        this.counts = counts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_cart, viewGroup, false);
        CartAdapter.ViewHolder vh = new CartAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        Glide.with(context).load(carts.get(position).images.get(0)).into(viewHolder.img_product);
        viewHolder.txt_name.setText(carts.get(position).name);
        viewHolder.txt_price.setText(String.valueOf((int)carts.get(position).price));
        viewHolder.txt_count.setText(String.valueOf(counts.get(position)));
        User.checkUserWithID(carts.get(position).UID, new Interface.OnResult<User>() {
            @Override
            public void onSuccess(User result) {
                if (!result.photo.isEmpty()) {
                    Glide.with(context).load(result.photo).into(viewHolder.img_photo);
                }
                viewHolder.txt_username.setText(result.firstname + " " + result.lastname);
            }
            @Override
            public void onFailure(String error) {
            }
        });
        viewHolder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickRemove(position);
            }
        });
        viewHolder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickPlus(position);
            }
        });
        viewHolder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickMinus(position);
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
        return carts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout_parent;
        ShapedImageView img_product;
        TextView txt_name;
        CircleImageView img_photo;
        TextView txt_username;
        TextView txt_price;
        TextView btn_remove;
        Button btn_plus;
        Button btn_minus;
        TextView txt_count;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent = itemView.findViewById(R.id.layout_parent);
            img_product = itemView.findViewById(R.id.img_product);
            txt_name = itemView.findViewById(R.id.txt_name);
            img_photo = itemView.findViewById(R.id.img_photo);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_username = itemView.findViewById(R.id.txt_username);
            btn_remove = itemView.findViewById(R.id.btn_remove);
            btn_plus = itemView.findViewById(R.id.btn_plus);
            btn_minus = itemView.findViewById(R.id.btn_minus);
            txt_count = itemView.findViewById(R.id.txt_count);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
        void onClickRemove(int index);
        void onClickPlus(int index);
        void onClickMinus(int index);
    }
}
