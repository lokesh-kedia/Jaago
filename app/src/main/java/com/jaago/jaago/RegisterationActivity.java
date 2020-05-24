package com.jaago.jaago;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class  RegisterationActivity extends AppCompatActivity {
    private Button mRegistration;
    private EditText mEmail,mPassword,nName;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        firebaseAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent i =new Intent(RegisterationActivity.this,ChooseLogin.class);
                }
            }
        };
        mAuth=FirebaseAuth.getInstance();
        mRegistration=findViewById(R.id.registration);
        mEmail=findViewById(R.id.email);
        mPassword=findViewById(R.id.password);
        nName=findViewById(R.id.name);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }

    public void registration(View view) {
        final String name= nName.getText().toString();
        final String email= mEmail.getText().toString();
        final String password = mPassword.getText().toString();
        if(!name.equals("")  && !email.equals("")  && !password.equals("") ) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterationActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(RegisterationActivity.this, "Sign In Error ", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RegisterationActivity.this, "Registered", Toast.LENGTH_LONG).show();
                        String userId = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                        Map userInfo = new HashMap<>();
                        userInfo.put("email", email);
                        userInfo.put("name", name);
                        userInfo.put("profileImageUrl", "default");
                        currentUserDb.updateChildren(userInfo);
                        Intent i = new Intent(RegisterationActivity.this, ReadWrite.class);
                        startActivity(i);
                    }
                }
            });
        }
    }
}
