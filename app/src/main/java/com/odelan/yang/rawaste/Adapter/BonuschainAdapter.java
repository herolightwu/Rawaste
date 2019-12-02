package com.odelan.yang.rawaste.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.odelan.yang.rawaste.Model.Bonus;
import com.odelan.yang.rawaste.Model.Order;
import com.odelan.yang.rawaste.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BonuschainAdapter extends RecyclerView.Adapter<BonuschainAdapter.ViewHolder> {
    Context context;
    List<Bonus> bonuses;
    EventListener listener;
    public BonuschainAdapter(Context context, List<Bonus> bonuses, EventListener listener) {
        this.context = context;
        this.bonuses = bonuses;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BonuschainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_bonuschain, viewGroup, false);
        BonuschainAdapter.ViewHolder vh = new BonuschainAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Bonus bonus = bonuses.get(position);
        Date myDate = new Date(bonus.timestamp * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        viewHolder.txt_order_date.setText(dateFormat.format(myDate));
        viewHolder.txt_order_number.setText(bonus.OID);

        viewHolder.layout_parent.setOnClickListener(v -> {
            if (listener != null) listener.onClickItem(position);
        });
    }

    @Override
    public int getItemCount() {
        return bonuses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout_parent;
        TextView txt_order_date;
        TextView txt_order_number;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent = itemView.findViewById(R.id.layout_parent);
            txt_order_date = itemView.findViewById(R.id.txt_order_date);
            txt_order_number = itemView.findViewById(R.id.txt_order_number);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
    }
}
