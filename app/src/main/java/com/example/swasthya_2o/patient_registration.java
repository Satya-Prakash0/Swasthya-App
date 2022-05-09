package com.example.swasthya_2o;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class patient_registration extends AppCompatActivity {

    private CircleImageView patient_profile_img;
    private EditText patient_name,registrationIdNo,patient_registration_phoneNo,patient_email,patient_password;
    private Button patient_profileImgUpload,patientRegNow;
    private Uri resultUri;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);

        patient_profile_img=findViewById(R.id.patient_profile_image);    //Upload through url
        patient_name=findViewById(R.id.patient_name);
        registrationIdNo=findViewById(R.id.registration_IdNumber);
        patient_registration_phoneNo=findViewById(R.id.patient_registration_phoneNo);
        patient_email=findViewById(R.id.patient_email);
        patient_password=findViewById(R.id.patientPassword);
        patient_profileImgUpload=findViewById(R.id.patientImgUpload);   //Button
        patientRegNow=findViewById(R.id.patientRegNow);                 //Button

        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
       // databaseReference=FirebaseDatabase.getInstance();

        patient_profileImgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

        patientRegNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=patient_name.getText().toString().trim();
                final String registrationNo=registrationIdNo.getText().toString().trim();
                final String phoneNo=patient_registration_phoneNo.getText().toString().trim();
                final String email=patient_email.getText().toString().trim();
                final String password=patient_password.getText().toString();

                if(email.isEmpty() || password.isEmpty() || phoneNo.isEmpty() || registrationNo.isEmpty() ||name.isEmpty())
                    Toast.makeText(patient_registration.this, "Field is empty", Toast.LENGTH_SHORT).show();

                if(resultUri==null)
                    Toast.makeText(patient_registration.this, "Upload image", Toast.LENGTH_SHORT).show();

                else{
                     progressDialog.setMessage("Registration in progress...");
                     progressDialog.setCanceledOnTouchOutside(false);
                     progressDialog.show();

                     mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                           if(!task.isSuccessful()){
                               String error =task.getException().toString();
                               Toast.makeText(patient_registration.this, error, Toast.LENGTH_SHORT).show();
                           }
                           else{
                               String currentUserID=mAuth.getCurrentUser().getUid();
                               databaseReference=FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID);
                               HashMap userInfo=new HashMap();
                               userInfo.put("name",name);
                               userInfo.put("email",email);
                               userInfo.put("registrationNo",registrationNo);
                               userInfo.put("phoneNo",phoneNo);
                               userInfo.put("password",password);
                               userInfo.put("type","patient");

                               databaseReference.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                   @Override
                                   public void onComplete(@NonNull Task task) {
                                       if(task.isSuccessful()){
                                           Toast.makeText(patient_registration.this, "Details set Successful", Toast.LENGTH_SHORT).show();
                                       }
                                       else{
                                           Toast.makeText(patient_registration.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                       }
                                       finish();
                                       progressDialog.dismiss();
                                   }
                               });

                               if(resultUri!=null){
                                   final StorageReference filepath= FirebaseStorage.getInstance().getReference().child("profile picture")
                                           .child(currentUserID);
                                   Bitmap bitmap=null;
                                   try {
                                       bitmap= MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resultUri);
                                   }
                                   catch (IOException e){
                                       e.printStackTrace();
                                   }

                                   ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                                   bitmap.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);
                                   byte[] data=byteArrayOutputStream.toByteArray();

                                   UploadTask uploadTask =filepath.putBytes(data);

                                   uploadTask.addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                          finish();
                                          return;
                                       }
                                   });
                                   uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                       @Override
                                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                           if(taskSnapshot.getMetadata() !=null){
                                               Task<Uri> result=taskSnapshot.getStorage().getDownloadUrl();
                                               result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                   @Override
                                                   public void onSuccess(Uri uri) {
                                                       String imageUrl=uri.toString();
                                                       Map newImageMap=new HashMap();
                                                       newImageMap.put("profilePictureUrl",imageUrl);

                                                       databaseReference.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                           @Override
                                                           public void onComplete(@NonNull Task task) {
                                                               if(task.isSuccessful())
                                                               Toast.makeText(patient_registration.this, "Registation Successfull", Toast.LENGTH_SHORT).show();
                                                               else
                                                                   Toast.makeText(patient_registration.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                           }

                                                       });
                                                       finish();
                                                   }
                                               });
                                           }
                                       }
                                   });
                                   Intent intent =new Intent(patient_registration.this,MainActivity.class);
                                   startActivity(intent);
                                   finish();
                                   progressDialog.dismiss();
                               }

                           }
                         }
                     });

                }
            }

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK && requestCode==1 && data!=null){
            resultUri=data.getData();
            patient_profile_img.setImageURI(resultUri);
        }
    }
}