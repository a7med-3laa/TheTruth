package com.ahmedalaa.thetruth;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahmedalaa.thetruth.model.Msg;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ahmed on 30/01/2018.
 */
public class MsgsAdapter extends RecyclerView.Adapter<MsgsAdapter.MsgsAdapterHolder> {
    private final Context context;
    private List<Msg> items;

    public MsgsAdapter(Context context) {
        this.items = new ArrayList<>();
        this.context = context;
    }

    public void add(List<Msg> msgs) {
        items = msgs;
        notifyDataSetChanged();
    }

    @Override
    public MsgsAdapterHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.msg_item, parent, false);
        return new MsgsAdapterHolder(v);
    }

    @Override
    public void onBindViewHolder(MsgsAdapterHolder holder, int position) {
        Msg item = items.get(position);
        holder.date.setText(item.getDate());
        holder.msgs.setText(item.getMsg());
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    class MsgsAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.msgs)
        TextView msgs;
        @BindView(R.id.react)
        ImageButton react;


        public MsgsAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
    

