package com.jaago.jaago;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PostArticle extends AppCompatActivity {
    EditText title, article, desc;
    Spinner category;
    String title1, article1, category1 , desc1;
    ImageView imageView;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    DatabaseReference currentUserDb,currentUserDb1;
    String[] categories = {"Tragedy", "Science", "Fantasy", "Mythology", "Adventure", "Mystery", "Fiction", "Horrer"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_article);
        title = findViewById(R.id.title);
        article = findViewById(R.id.article);
        category = findViewById(R.id.category);
        desc = findViewById(R.id.desc);
        imageView = findViewById(R.id.img);
        /*category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                category1=categories[i];
            }
        });*/


            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            category.setAdapter(aa);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category1=categories[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void submit(View view) {
        title1 = title.getText().toString();
        article1 = article.getText().toString();
        //category1 = category.getText().toString();
        desc1 = desc.getText().toString();
        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentUserDb = FirebaseDatabase.getInstance().getReference().child("Posts").child(category1);
        String key = currentUserDb.push().getKey();
        currentUserDb = currentUserDb.child(key);
        Map userInfo = new HashMap<>();
        userInfo.put("Title", title1);
        userInfo.put("Article", article1);
        userInfo.put("Desc", desc1);
        currentUserDb.updateChildren(userInfo);
        uploadImage(imageView);
        currentUserDb1 = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("uploads").child(category1).child(key);
        Map userInfo1 = new HashMap<>();
        userInfo1.put("Title", title1);
        userInfo1.put("Article", article1);
        userInfo1.put("Desc", desc1);
        currentUserDb1.updateChildren(userInfo1);
        title.setText("");
        //category.setText("");
        desc.setText("");
        article.setText("");
        Toast.makeText(this,"Article Uploaded Successfully",Toast.LENGTH_LONG).show();
    }

    public void opengallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void uploadImage(View view) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("posts/" + category1 + "/" +  UUID.randomUUID().toString());
            UploadTask uploadTask=ref.putFile(filePath);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri url = task.getResult();
                        progressDialog.dismiss();
                        currentUserDb.child("Img").setValue(url.toString());
                        currentUserDb1.child("Img").setValue(url.toString());
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
                   /* .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                            currentUserDb.child("Img").setValue(url);
                            currentUserDb1.child("Img").setValue(url);
                            Toast.makeText(PostArticle.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(PostArticle.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });*/
        }
    }
}
