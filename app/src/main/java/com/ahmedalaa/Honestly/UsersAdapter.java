package com.ahmedalaa.Honestly;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahmedalaa.Honestly.model.User;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ahmed on 30/01/2018.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserAdapterHolder> {
    private final Context context;
    private List<User> items;

    UsersAdapter(Context context) {
        items = new ArrayList<>();
        this.context = context;
    }

    public void add(List<User> users) {
        items = users;
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public UserAdapterHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserAdapterHolder(v);
    }

    @Override
    public void onBindViewHolder(UserAdapterHolder holder, int position) {
        User item = items.get(position);
        holder.userName.setText(item.getName());
        holder.userName2.setText(item.getEmail());
        if (!item.getPhotoURL().isEmpty())
            Picasso.with(context).load(item.getPhotoURL()).into(holder.userImg);


    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    class UserAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_img)
        CircleImageView userImg;
        @BindView(R.id.user_name)
        TextView userName;
        @BindView(R.id.user_name2)
        TextView userName2;

        UserAdapterHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(v -> {
                Intent i = new Intent(context, SendFeedBackActivity.class);
                i.putExtra("user", Parcels.wrap(items.get(getAdapterPosition())));
                context.startActivity(i);

            });
        }
    }
}
    

