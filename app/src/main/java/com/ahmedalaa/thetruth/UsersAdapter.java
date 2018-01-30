package com.ahmedalaa.thetruth;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmedalaa.thetruth.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ahmed on 30/01/2018.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MsgsAdapterHolder> {
    private final Context context;
    private List<User> items;

    public UsersAdapter(Context context) {

        this.context = context;
    }

    public void add(List<User> users) {
        items = users;
        notifyDataSetChanged();
    }

    @Override
    public MsgsAdapterHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new MsgsAdapterHolder(v);
    }

    @Override
    public void onBindViewHolder(MsgsAdapterHolder holder, int position) {
        User item = items.get(position);
        holder.userName.setText(item.getName());
        holder.userName2.setText(item.getUserName());
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
        ImageView userImg;
        @BindView(R.id.user_name)
        TextView userName;
        @BindView(R.id.user_name2)
        TextView userName2;

        MsgsAdapterHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(v -> {
                Intent i = new Intent(context, SendFeedBackActivity.class);
                i.putExtra("ID", items.get(getAdapterPosition()).getID());
                context.startActivity(i);

            });
        }
    }
}
    

