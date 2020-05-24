package com.jaago.jaago;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class PostRead extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_read);
        Bundle b = getIntent().getExtras();
        String Title=b.getString("Title");
        String Desc=b.getString("Desc");
        String Article=b.getString("Article");
        String Img=b.getString("Img");
        TextView title = findViewById(R.id.title);
        TextView article = findViewById(R.id.article);
        ImageView imageView = findViewById(R.id.postimg);
        title.setText(Title);
        article.setText(Article);
        boolean isPhoto =Img != null;
        if (isPhoto) {

            imageView.setVisibility(View.VISIBLE);
            Glide.with(imageView.getContext())
                    .load(Img)
                    .into(imageView);
        }
    }
    public void goback(View view) {
        Intent i = new Intent(this, ReadWrite.class);
        startActivity(i);
    }
}
