package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class ImageStoreFirebase_Activity extends AppCompatActivity {

    Button btnUpload,btnCamera;
    ImageView imgView;
    FirebaseDatabase fd;
    Bitmap bitmap;
    Uri uri;
    FirebaseStorage firebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_store_firebase);
        btnUpload = findViewById(R.id.btnUpload);
        btnCamera = findViewById(R.id.btnCamera);
        imgView = findViewById(R.id.imgView);

        fd = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog
                        = new ProgressDialog(ImageStoreFirebase_Activity.this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                StorageReference storageRef = firebaseStorage.getReference().child("images/"
                        + UUID.randomUUID().toString());
                storageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                fd.getReference().child("image").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ImageStoreFirebase_Activity.this, "Image Upload", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ImageStoreFirebase_Activity.this, "Uploading Fail.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        // Get the device permission and open the camera.
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(ActivityCompat.checkSelfPermission(ImageStoreFirebase_Activity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
               {
                   ActivityCompat.requestPermissions(ImageStoreFirebase_Activity.this,new String[]{Manifest.permission.CAMERA},1);
               }
               else
               {
                   Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                   startActivityForResult(camera_intent, 111);
               }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==111)
        {
//            uri = (Uri) data.getExtras().get("data");
            bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(ImageStoreFirebase_Activity.this.getContentResolver(), bitmap, "Title", null);
            uri =  Uri.parse(path);
            imgView.setImageBitmap(bitmap);
        }
    }

}