package com.ahmedalaa.Honestly;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahmedalaa.Honestly.model.Msg;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MsgsAdapterHolder> {
    private final Context context;

    private List<Msg> items;

    MessagesAdapter(Context context) {
        this.items = new ArrayList<>();
        this.context = context;
    }

    private static Bitmap getBitmapFromView(View m) {
        m.setDrawingCacheEnabled(true);
        m.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        m.layout(0, 0, m.getMeasuredWidth(), m.getMeasuredHeight());
        m.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(m.getDrawingCache());
        m.setDrawingCacheEnabled(false);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        return b;
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
        String date = DateUtils.getRelativeTimeSpanString(item.getDate().getTime(),
                new GregorianCalendar().getTimeInMillis(),
                DateUtils.SECOND_IN_MILLIS).toString();
        holder.date.setText(date);
        holder.msgs.setText(item.getMsg());
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    class MsgsAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.msgs)
        TextView msgs;
        @BindView(R.id.react)
        ImageButton react;

        @BindView(R.id.replay)
        ImageButton replay;
        @BindView(R.id.share)
        ImageButton share;

        MsgsAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            replay.setOnClickListener(v -> {
                final EditText msgTxt = new EditText(context);
                msgTxt.setHint(R.string.enter_msg_hnt);
                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.replay_dialog_title))
                        .setView(msgTxt)
                        .setPositiveButton(context.getString(R.string.send_btn), (dialog, whichButton) -> {
                            if (msgTxt.getText().toString().isEmpty()) {
                                msgTxt.setError(context.getResources().getString(R.string.error_field_required))
                                ;
                                msgTxt.requestFocus();
                            } else {
                                Msg msg = items.get(getAdapterPosition());
                                FirebaseDatabase.getInstance().getReference().child("msgs").child(msg.getId())
                                        .child("replayMsg").setValue(msgTxt.getText().toString()).addOnCompleteListener(task -> {

                                });

                            }
                        })
                        .setNegativeButton(context.getString(R.string.cancel_btn), (dialog, whichButton) -> {

                        })
                        .show();
            });


            share.setOnClickListener(v -> {
                View m = LayoutInflater.from(context).inflate(R.layout.share_img, null);
                TextView textView = m.findViewById(R.id.msg);
                textView.setText(items.get(getAdapterPosition()).getMsg());

                Intent i = new Intent(Intent.ACTION_SEND);

                i.setType("image/*");

                i.putExtra(Intent.EXTRA_STREAM, getImageUri(context, getBitmapFromView(m)));
                try {
                    context.startActivity(Intent.createChooser(i, context.getString(R.string.share_feedback)));
                } catch (android.content.ActivityNotFoundException ex) {

                    ex.printStackTrace();
                }


            });
        }
    }
}
    

