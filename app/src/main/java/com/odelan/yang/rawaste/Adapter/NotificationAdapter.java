package com.odelan.yang.rawaste.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.odelan.yang.rawaste.Model.Notification;
import com.odelan.yang.rawaste.R;

import java.util.List;

public class NotificationAdapter
        extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    Context context;
    List<Notification> notifications;
    EventListener listener;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public NotificationAdapter(Context context, List<Notification> notifications, EventListener listener) {
        this.context = context;
        this.notifications = notifications;
        this.listener = listener;
        this.viewBinderHelper.setOpenOnlyOne(true);
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_notification, viewGroup, false);
        NotificationAdapter.ViewHolder vh = new NotificationAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        viewBinderHelper.bind(holder.swipe, String.valueOf(position));
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewBinderHelper.closeLayout(String.valueOf(position));
                if (listener != null) {
                    listener.onDelete(position);
                }
            }
        });
        holder.btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewBinderHelper.closeLayout(String.valueOf(position));
                if (listener != null) {
                    listener.onChat(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SwipeRevealLayout swipe;
        LinearLayout btn_delete;
        LinearLayout btn_chat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            swipe = itemView.findViewById(R.id.swipe);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            btn_chat = itemView.findViewById(R.id.btn_chat);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
        void onDelete(int index);
        void onChat(int index);
    }
}
