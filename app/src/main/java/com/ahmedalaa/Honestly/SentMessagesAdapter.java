package com.ahmedalaa.Honestly;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahmedalaa.Honestly.model.Msg;
import com.ahmedalaa.Honestly.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SentMessagesAdapter extends RecyclerView.Adapter<SentMessagesAdapter.MsgsAdapterHolder> {
    private final Context context;
    private List<Msg> items;

    SentMessagesAdapter(Context context) {
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
        Query user = FirebaseDatabase.getInstance().getReference().child("users").child(item.getReceiverID());
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.getValue(User.class);
                holder.nameTxt.setText(user1.getName());
                if (!user1.getPhotoURL().isEmpty())
                    Picasso.with(context).load(user1.getPhotoURL()).into(holder.userImg);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                i.putExtra("receiverID", items.get(getAdapterPosition()).getReceiverID());
                context.startActivity(i);


            });
        }
    }
}
    

