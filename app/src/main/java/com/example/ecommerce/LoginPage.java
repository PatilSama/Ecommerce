package com.example.ecommerce;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    EditText txtName,txtPass;
    Button btnSubmit,btnLogin;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(LoginPage.this,DashboardPage.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        txtName = findViewById(R.id.txtName);
        txtPass = findViewById(R.id.txtPass);
        btnSubmit = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(txtName.getText().toString(),txtPass.getText().toString())
                        .addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Toast.makeText(LoginPage.this, ""+task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        if(task.isSuccessful())
                        {
                            Toast.makeText(LoginPage.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(LoginPage.this, "User Already Exit", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
        // Sing with firebase using Email and Password.
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(txtName.getText().toString(),txtPass.getText().toString())
                        .addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    startActivity(new Intent(LoginPage.this,DashboardPage.class));
                                }
                                else
                                {
                                    Toast.makeText(LoginPage.this, "Login Fail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}