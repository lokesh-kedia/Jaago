package com.jaago.jaago;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {
    private Context mContext;

    public PostAdapter(Context context, int resource, List<Post> objects) {
        super(context, resource, objects);
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_post, parent, false);
        }
        TextView titletextview = convertView.findViewById(R.id.titletextview);
        TextView desctextview = convertView.findViewById(R.id.desctextview);
        ImageView imageView = convertView.findViewById(R.id.imageview);
        final Post post=getItem(position);
        boolean isPhoto = post.getImg() != null;
        if (isPhoto) {

            imageView.setVisibility(View.VISIBLE);
            Glide.with(imageView.getContext())
                    .load(post.getImg())
                    .into(imageView);
        }
        titletextview.setText(post.getTitle());
        desctextview.setText(post.getDesc());
        return convertView;

    }
}
