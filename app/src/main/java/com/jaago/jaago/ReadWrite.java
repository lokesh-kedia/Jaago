package com.jaago.jaago;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ReadWrite extends AppCompatActivity {
    private int th=0;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref=new SharedPref(this);
        if (sharedPref.loadNightModeState()==true) {

            th=0;
            //setTheme(R.style.darktheme);
        } else {
            //setTheme(R.style.AppTheme);
            th=1;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_write);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.book1);


        Switch swich = findViewById(R.id.temptxt);
        if(sharedPref.loadNightModeState()==true)
            swich.setChecked(true);
        else
            swich.setChecked(false);
        swich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    sharedPref.setNightModeState(true);
                    restartApp();
                    th=0;
                }
                else {
                    sharedPref.setNightModeState(false);
                    restartApp();
                    th=1;
                }
            }
        });
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
        }
        else {
            Intent i =new Intent(ReadWrite.this,ChooseLogin.class);
            startActivity(i);
        }


    }
    public void restartApp() {
        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }

    public void opensearch(View view) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        TextView textView = findViewById(R.id.temptxt);
        textView.setVisibility(View.GONE);
        ImageButton imageButton = findViewById(R.id.searchicon);
        imageButton.setVisibility(View.GONE);
        LinearLayout linearLayout = findViewById(R.id.search);
        linearLayout.setVisibility(View.VISIBLE);


    }

    public void opentool(View view) {
        LinearLayout linearLayout = findViewById(R.id.search);
        linearLayout.setVisibility(View.GONE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.temptxt);
        textView.setVisibility(View.VISIBLE);
        ImageButton imageButton = findViewById(R.id.searchicon);
        imageButton.setVisibility(View.VISIBLE);
    }
    public void dosearch(View view) {
        EditText editText = findViewById(R.id.searchtxt);
        String s = String.valueOf(editText.getText());
        //JSON_URL = "https://newsapi.org/v2/everything?q=" + s + "&apiKey=eb524ac737c44081923e4bd0366af2b8";
        //loadNewsList();

    }
    public void tick(View view) {
        String cat = null;
        switch (view.getId()){
            case R.id.RelTragedy:
                cat="Tragedy";
                break;
            case R.id.RelScience:
                cat="Science";
                break;
            case R.id.RelFantasy:
                cat="Fantasy";
                break;
            case R.id.RelMyth:
                cat="Mythology";
                break;
            case R.id.RelAdvn:
                cat="Adventure";
                break;
            case R.id.RelMystry:
                cat="Mystery";
                break;
            case R.id.RelFiction:
                cat="Fiction";
                break;
            case R.id.RelHorrer:
                cat="Horrer";
                break;
        }
        Intent i = new Intent(ReadWrite.this,PostFeed.class);
       // Bundle bundle= ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        i.putExtra("Category",cat);
        //getApplication().startActivity(i,bundle);
        startActivity(i);

    }

    public void postarticle(View view) {
        Intent i = new Intent(this,PostArticle.class);
        startActivity(i);
    }

    public void openhome(View view) {
        Intent i= new Intent(getApplication(),ReadWrite.class);
        startActivity(i);
    }

    public void openlib(View view) {
        Intent i= new Intent(getApplication(),ReadWrite.class);
        startActivity(i);
    }

    public void openprofile(View view) {
        Intent i= new Intent(getApplication(),Profile.class);
        startActivity(i);
    }
}
