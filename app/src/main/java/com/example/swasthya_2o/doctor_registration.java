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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class doctor_registration extends AppCompatActivity {

    private CircleImageView dr_profile_image;
    private EditText dr_name,dr_IdNumber,dr_phoneNo,dr_email,dr_password;
    private Spinner availabilitySpinner,departmentSpinner,specializationSpinner;
    private Button dr_regBtn,dr_ImgUpload;
    private Uri resultUri;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        dr_profile_image=findViewById(R.id.dr_profile_image);
        dr_name=findViewById(R.id.dr_name);
        dr_IdNumber=findViewById(R.id.Dr_IdNumber);
        dr_phoneNo=findViewById(R.id.dr_phoneNo);
        dr_email=findViewById(R.id.Dr_email);
        dr_password=findViewById(R.id.drPassword);

        availabilitySpinner=findViewById(R.id.availabilitySpinner);
        departmentSpinner=findViewById(R.id.departmentSpinner);
        specializationSpinner=findViewById(R.id.specializationSpinner);

        dr_regBtn=findViewById(R.id.DrRegBtn);
        dr_ImgUpload=findViewById(R.id.drImgUpload);

        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();

        dr_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

        dr_regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=dr_name.getText().toString().trim();
                final String registrationNo=dr_IdNumber.getText().toString().trim();
                final String phoneNo=dr_phoneNo.getText().toString().trim();
                final String email=dr_email.getText().toString().trim();
                final String password=dr_password.getText().toString();

                final String avlTime=availabilitySpinner.getSelectedItem().toString();
                final String department=departmentSpinner.getSelectedItem().toString();
                final String specialization=specializationSpinner.getSelectedItem().toString();

                if(email.isEmpty() || password.isEmpty() || phoneNo.isEmpty() || registrationNo.isEmpty() ||name.isEmpty() )
                    Toast.makeText(doctor_registration.this, "Field is empty", Toast.LENGTH_SHORT).show();
                if( avlTime.equalsIgnoreCase("Select your availability time") ||department.equalsIgnoreCase("Select department")
                        ||specialization.equalsIgnoreCase("Select specialization"))
                    Toast.makeText(doctor_registration.this, "Select the options", Toast.LENGTH_SHORT).show();
                if(resultUri==null)
                    Toast.makeText(doctor_registration.this, "Upload image", Toast.LENGTH_SHORT).show();

                else{
                    progressDialog.setMessage("Registration in progress...");
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();

                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                String error =task.getException().toString();
                                Toast.makeText(doctor_registration.this, error, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String currentUserID=mAuth.getCurrentUser().getUid();
                                databaseReference= FirebaseDatabase.getInstance().getReference().child("doctor").child(currentUserID);
                                HashMap userInfo=new HashMap();
                                userInfo.put("name",name);
                                userInfo.put("email",email);
                                userInfo.put("registrationNo",registrationNo);
                                userInfo.put("phoneNo",phoneNo);
                                userInfo.put("password",password);
                                userInfo.put("availableTime",avlTime);
                                userInfo.put("Specialization",specialization);
                                userInfo.put("Department",department);
                                userInfo.put("type","Doctor");

                                databaseReference.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(doctor_registration.this, "Details set Successful", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(doctor_registration.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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
                                                                    Toast.makeText(doctor_registration.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                                else
                                                                    Toast.makeText(doctor_registration.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                            }

                                                        });
                                                        finish();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                    Intent intent =new Intent(doctor_registration.this,MainActivity.class);
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
            dr_profile_image.setImageURI(resultUri);
        }
    }
}