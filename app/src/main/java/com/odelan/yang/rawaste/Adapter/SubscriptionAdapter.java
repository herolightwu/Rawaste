package com.odelan.yang.rawaste.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.rawaste.Model.User;
import com.odelan.yang.rawaste.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder> {
    Context context;
    List<User> users;
    EventListener listener;
    public SubscriptionAdapter(Context context, List<User> users, EventListener listener) {
        this.context = context;
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubscriptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_subscription, viewGroup, false);
        SubscriptionAdapter.ViewHolder vh = new SubscriptionAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        if (!users.get(position).photo.isEmpty()) {
            Glide.with(context).load(users.get(position).photo).into(viewHolder.img_photo);
        }
        viewHolder.txt_name.setText(users.get(position).firstname + " " + users.get(position).lastname);
        viewHolder.btn_unsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickUnsubscribe(position);
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
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_parent;
        CircleImageView img_photo;
        TextView txt_name;
        Button btn_unsubscribe;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent = itemView.findViewById(R.id.layout_parent);
            img_photo = itemView.findViewById(R.id.img_photo);
            txt_name = itemView.findViewById(R.id.txt_name);
            btn_unsubscribe = itemView.findViewById(R.id.btn_unsubscribe);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
        void onClickUnsubscribe(int index);
    }
}
