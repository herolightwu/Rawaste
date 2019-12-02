package com.odelan.yang.rawaste.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.rawaste.Activity.BaseActivity;
import com.odelan.yang.rawaste.Model.Contact;
import com.odelan.yang.rawaste.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    Context context;
    List<Contact> contacts;
    EventListener listener;
    public ContactAdapter(Context context, List<Contact> contacts, EventListener listener) {
        this.context = context;
        this.contacts = contacts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_message, viewGroup, false);
        ContactAdapter.ViewHolder vh = new ContactAdapter.ViewHolder(v);
        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        if (!contacts.get(position).user.photo.isEmpty()) {
            Glide.with(context).load(contacts.get(position).user.photo).into(viewHolder.img_photo);
        } else {
            viewHolder.img_photo.setImageResource(R.mipmap.avata);
        }
        viewHolder.txt_name.setText(contacts.get(position).user.firstname + " " + contacts.get(position).user.lastname);
        viewHolder.txt_message.setText(contacts.get(position).message);
        Long delta = System.currentTimeMillis() / 1000 - contacts.get(position).timestamp;
        if (BaseActivity.deltaTimeString(delta).isEmpty()) {
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(contacts.get(position).timestamp * 1000L);
            String date = DateFormat.format("dd-MM-yyyy", cal).toString();
            viewHolder.txt_time.setText(date);
        } else {
            viewHolder.txt_time.setText(BaseActivity.deltaTimeString(delta));
        }
        if (contacts.get(position).unread_count > 0) {
            viewHolder.layout_unread.setVisibility(View.VISIBLE);
            viewHolder.txt_unread.setText(String.valueOf(contacts.get(position).unread_count));
        } else {
            viewHolder.layout_unread.setVisibility(View.INVISIBLE);
        }
        viewHolder.layout_parent.setOnClickListener(v -> {
            if (listener != null) listener.onClickItem(position);
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_parent;
        CircleImageView img_photo;
        TextView txt_name;
        TextView txt_message;
        TextView txt_time;
        TextView txt_unread;
        RelativeLayout layout_unread;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent       =     itemView.findViewById(R.id.layout_parent);
            img_photo           =     itemView.findViewById(R.id.img_photo);
            txt_name            =     itemView.findViewById(R.id.txt_name);
            txt_message         =     itemView.findViewById(R.id.txt_message);
            txt_time            =     itemView.findViewById(R.id.txt_time);
            txt_unread          =     itemView.findViewById(R.id.txt_unread);
            layout_unread       =     itemView.findViewById(R.id.layout_unread);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
    }
}
