package com.odelan.yang.rawaste.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.rawaste.Activity.BaseActivity;
import com.odelan.yang.rawaste.Model.Chat;
import com.odelan.yang.rawaste.Model.User;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.Constants;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Chat> chats;
    User user;

    public MessageAdapter(Context context, User user, List<Chat> chats) {
        this.context = context;
        this.chats = chats;
        this.user = user;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        if (i == Constants.CHAT_MY) {
            View v = LayoutInflater.from(viewGroup.getContext()) .inflate(R.layout.item_message_my, viewGroup, false);
            return new MessageAdapter.MyViewHolder(v);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()) .inflate(R.layout.item_message_other, viewGroup, false);
            return new MessageAdapter.OtherViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof MyViewHolder) {
            ((MyViewHolder) viewHolder).txt_message.setText(chats.get(position).message);
            Long delta = System.currentTimeMillis() / 1000 - chats.get(position).timestamp;
            if (BaseActivity.deltaTimeString(delta).isEmpty()) {
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                cal.setTimeInMillis(chats.get(position).timestamp * 1000L);
                String date = DateFormat.format("dd-MM-yyyy", cal).toString();
                ((MyViewHolder) viewHolder).txt_time.setText(date);
            } else {
                ((MyViewHolder) viewHolder).txt_time.setText(BaseActivity.deltaTimeString(delta));
            }
        } else if (viewHolder instanceof OtherViewHolder) {
            if (!user.photo.isEmpty()) {
                Glide.with(context).load(user.photo).into(((OtherViewHolder)viewHolder).img_avata);
            } else {
                ((OtherViewHolder)viewHolder).img_avata.setImageResource(R.mipmap.avata);
            }
            ((OtherViewHolder)viewHolder).txt_name.setText(user.firstname + " " + user.lastname);
            ((OtherViewHolder) viewHolder).txt_message.setText(chats.get(position).message);
            Long delta = System.currentTimeMillis() / 1000 - chats.get(position).timestamp;
            if (BaseActivity.deltaTimeString(delta).isEmpty()) {
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                cal.setTimeInMillis(chats.get(position).timestamp * 1000L);
                String date = DateFormat.format("dd-MM-yyyy", cal).toString();
                ((OtherViewHolder) viewHolder).txt_time.setText(date);
            } else {
                ((OtherViewHolder) viewHolder).txt_time.setText(BaseActivity.deltaTimeString(delta));
            }
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_message;
        TextView txt_time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_message = itemView.findViewById(R.id.txt_message);
            txt_time = itemView.findViewById(R.id.txt_time);
        }
    }

    public class OtherViewHolder extends RecyclerView.ViewHolder {
        TextView txt_message;
        TextView txt_time;
        CircleImageView img_avata;
        TextView txt_name;
        public OtherViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_message = itemView.findViewById(R.id.txt_message);
            txt_time = itemView.findViewById(R.id.txt_time);
            img_avata = itemView.findViewById(R.id.img_avata);
            txt_name = itemView.findViewById(R.id.txt_name);
        }
    }
}
