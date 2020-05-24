package com.jaago.jaago;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class ReadersCategory extends AppCompatActivity {


    public static boolean started=false;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readers_category);
    }

    public void openlogin(View view) {
        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            Intent i=new Intent(ReadersCategory.this,PostArticle.class);
            startActivity(i);
        }
        else {
            Intent i =new Intent(ReadersCategory.this,ChooseLogin.class);
            startActivity(i);
        }
        
    }
    public void tick(View view){
        view.setBackgroundColor(Color.GRAY);
    }
    public void tick1(View view){

    }
}
