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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegistrationPage extends AppCompatActivity {

    EditText txtUserName,txtPass,txtEmail,txtPhone;
    Button btnSubmit,btnFetch;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        txtEmail = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtPass);
        txtPhone = findViewById(R.id.txtPhone);
        txtUserName = findViewById(R.id.txtName);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnFetch = findViewById(R.id.btnFetch);
        getSupportActionBar().setHomeButtonEnabled(true);
         db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(),txtPass.getText().toString())
                        .addOnCompleteListener(RegistrationPage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                
                                Map<String, Object> user = new HashMap<>();
                                user.put("Email",txtEmail.getText().toString());
                                user.put("Password", txtPass.getText().toString());
                                user.put("UserId", txtUserName.getText().toString());
                                user.put("Mobile",txtPhone.getText().toString());

//                                CollectionReference dbCourses = db.collection("users");
//                                dbCourses.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                    @Override
//                                    public void onSuccess(DocumentReference documentReference) {
//                                        // after the data addition is successful
//                                        // we are displaying a success toast message.
//                                        Toast.makeText(RegistrationPage.this, "Your Course has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(RegistrationPage.this,LoginPage.class));
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        // this method is called when the data addition process is failed.
//                                        // displaying a toast message when data addition is failed.
//                                        Toast.makeText(RegistrationPage.this, "Fail to add course \n" + e, Toast.LENGTH_SHORT).show();
//                                        System.out.println("====="+e.getMessage());
//                                    }
//                                });

                                db.collection("users")
                                        .add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(RegistrationPage.this, "Data Store Successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(RegistrationPage.this,LoginPage.class));
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegistrationPage.this,"Data Not Stored.",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
            }

        });

        // Fetch data from firestore database
        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        Log.d(TAG, document.getId() + " => " + document.getData().get("Email"));
//                                        Toast.makeText(RegistrationPage.this, ""+document.getId() + " => " + document.getData(), Toast.LENGTH_SHORT).show();
                                        txtEmail.setText(""+document.getData().get("Email"));
                                        txtPass.setText(""+document.getData().get("Password"));
                                        txtPhone.setText(""+document.getData().get("Mobile"));
                                        txtUserName.setText(""+document.getData().get("UserId"));
                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });
    }
}