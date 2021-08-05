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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.resurrection.notes.DatabaseHelper;
import com.resurrection.notes.MainActivity;
import com.resurrection.notes.R;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    private EditText usernameInput, passInput;
    private Button registerBtn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private HashMap<String,String> userData;
    private Button swicthLoginActivtyBtn;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = usernameInput.getText().toString();
                String pass = passInput.getText().toString();
                firebaseAuth.createUserWithEmailAndPassword(userName,pass).addOnSuccessListener(Register.this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(Register.this, "kayıt tamamlanadı", Toast.LENGTH_SHORT).show();
                        firebaseUser = firebaseAuth.getCurrentUser();
                        userData = new HashMap<>();
                        userData.put("email",userName);
                        userData.put("pass",pass);
                        databaseReference.child("users").child(firebaseUser.getUid()).child("userInfo").setValue(userData).addOnSuccessListener(Register.this, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Register.this, "veri tabanı oluşturuldu", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(Register.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Register.this, "veri tabanı oluşturulamadı", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }).addOnFailureListener(Register.this, new OnFailureListener() {
                    @Override
                    public void onFailure( Exception e) {
                        Toast.makeText(Register.this, "Kayıt tamamlanamadı", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        swicthLoginActivtyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Login.class));
            }
        });



    }

    @Override
    protected void onStart() {
        autoSignIn();
        super.onStart();
    }

    void init(){
        swicthLoginActivtyBtn = findViewById(R.id.swicthLoginActivtyBtn);
        usernameInput = findViewById(R.id.usernameInput);
        passInput = findViewById(R.id.passInput);
        registerBtn = findViewById(R.id.loginBtn);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseHelper = new DatabaseHelper(this);

    }
    private void autoSignIn(){
        FirebaseAuth firebaseAuth ;
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            startActivity(new Intent(Register.this, MainActivity.class));
        }
    }


}