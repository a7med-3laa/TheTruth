package com.ahmedalaa.thetruth;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahmedalaa.thetruth.model.Msg;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ahmed on 30/01/2018.
 */
public class OutMsgsAdapter extends RecyclerView.Adapter<OutMsgsAdapter.MsgsAdapterHolder> {
    private final Context context;
    private List<Msg> items;

    public OutMsgsAdapter(Context context) {
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
                .inflate(R.layout.msg_item2, parent, false);
        return new MsgsAdapterHolder(v);
    }

    @Override
    public void onBindViewHolder(MsgsAdapterHolder holder, int position) {
        Msg item = items.get(position);
        holder.msgs.setText(item.getMsg());
        holder.msgs2.setText(item.getReplayMsg());
        holder.nameTxt.setText(item.getReciverName());
        Picasso.with(context).load(item.getReciverPic()).into(holder.userImg);

    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }


    class MsgsAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_img)
        CircleImageView userImg;
        @BindView(R.id.name_txt)
        TextView nameTxt;
        @BindView(R.id.msgs)
        TextView msgs;
        @BindView(R.id.msgs2)
        TextView msgs2;

        MsgsAdapterHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(v -> {
                Intent i = new Intent(context, SendFeedBackActivity.class);
                i.putExtra("ID", items.get(getAdapterPosition()).getReciverID());
                context.startActivity(i);


            });
        }
    }
}
    

