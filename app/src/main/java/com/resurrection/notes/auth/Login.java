package com.resurrection.notes.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.resurrection.notes.MainActivity;
import com.resurrection.notes.R;

public class Login extends AppCompatActivity {
    private EditText usernameInput,passInput;
    private Button loginBtn;
    private FirebaseAuth firebaseAuth;
    private String username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameInput.getText().toString();
                password = passInput.getText().toString();

                firebaseAuth.signInWithEmailAndPassword(username,password).addOnSuccessListener( new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(Login.this, "Giriş Başarılı ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "Giriş başarısız", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }void init(){
        usernameInput = findViewById(R.id.usernameInput);
        passInput = findViewById(R.id.passInput);
        loginBtn = findViewById(R.id.loginBtn);
        firebaseAuth = FirebaseAuth.getInstance();
    }
}