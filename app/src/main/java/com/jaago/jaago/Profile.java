package com.jaago.jaago;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
       // String img= mAuth.getCurrentUser().getPhotoUrl().toString();
        String name =mAuth.getCurrentUser().getDisplayName();

        ImageButton imageButton=findViewById(R.id.userimg);
        TextView textView = findViewById(R.id.username);
        textView.setText(name);
       /* boolean isPhoto = img != null;
        if (isPhoto) {
            imageButton.setVisibility(View.VISIBLE);
            Glide.with(imageButton.getContext())
                    .load(img)
                    .into(imageButton);
        }*/
    }

    public void opensettings(View view) {
        Intent i = new Intent(Profile.this,Settings.class);
        startActivity(i);
    }
}
