package com.jaago.jaago;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostFeed extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String category;
    private ListView listView;
    private PostAdapter postAdapter;
    private ValueEventListener valueEventListener;
    private ProgressDialog progressDialog;
    private List<Post> postlist= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_feed);
        progressDialog = new ProgressDialog(PostFeed.this);
        progressDialog.setMessage("Loading");
        //progressDialog.setCancelable(false);
        //progressDialog.setIndeterminate(false);
        progressDialog.show();
        Bundle b = getIntent().getExtras();
        category = b.getString("Category");
        TextView textView = findViewById(R.id.title);
        textView.setText(category);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Posts").child(category);
        if(databaseReference!=null)
        attachDatabaseReadListener();
        else{
            progressDialog.dismiss();
            Toast.makeText(PostFeed.this,"There Is Nothing Here Yet",Toast.LENGTH_LONG).show();
        }
        listView = findViewById(R.id.listview);
        final List<Post> posts = new ArrayList<>();
        postAdapter = new PostAdapter(this, R.layout.item_post, posts);
        listView.setAdapter(postAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Post post= postlist.get(i);
                String Title=post.getTitle();
                String Desc=post.getDesc();
                String Article=post.getArticle();
                String Img=post.getImg();
                Intent ii = new Intent(PostFeed.this,PostRead.class);
                    ii.putExtra("Title",Title);
                    ii.putExtra("Desc",Desc);
                    ii.putExtra("Article",Article);
                    ii.putExtra("Img",Img);
                startActivity(ii);

            }
        });
    }

    public void attachDatabaseReadListener() {
        if (valueEventListener == null) {
            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot==null){
                        progressDialog.dismiss();
                        Toast.makeText(PostFeed.this,"There Is Nothing Here Yet",Toast.LENGTH_LONG).show();
                    }
                    for (DataSnapshot plots : dataSnapshot.getChildren()) {
                        Post post = plots.getValue(Post.class);
                        if(post==null){
                            progressDialog.dismiss();
                            Toast.makeText(PostFeed.this,"There Is Nothing Here Yet",Toast.LENGTH_LONG).show();
                        }
                        // if (friendlyMessage.getAvail().equals("True")) {
                        postlist.add(post);
                        postAdapter.add(post);
                        progressDialog.dismiss();
                        // plotNo.add(plots.getKey());
                        //}
                        //progressDialog.dismiss();
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            };
            databaseReference.addListenerForSingleValueEvent(valueEventListener);
        }
    }

    public void goback(View view) {
        Intent i = new Intent(this, ReadWrite.class);
        startActivity(i);
    }
}
