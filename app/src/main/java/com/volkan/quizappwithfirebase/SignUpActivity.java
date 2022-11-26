package com.volkan.quizappwithfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.volkan.quizappwithfirebase.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    User user;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eMail, password, name, referenceCode;
                eMail = binding.edtEMail.getText().toString();
                password = binding.edtPassword.getText().toString();
                name = binding.edtName.getText().toString();
                user = new User(name, eMail, password );
                progressDialog.setTitle("Lütfen Bekleyiniz");
                progressDialog.setMessage("Hesap Oluşturuluyor");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(eMail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            String uId = task.getResult().getUser().getUid();
                            firebaseFirestore.collection("Users")
                                    .document(uId).set(user).addOnCompleteListener( new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                                finish();
                                            } else {
                                                Toast.makeText(SignUpActivity.this, "Hata oluştu", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Hata Oluştu", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
       binding.btnLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
               startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
           }
       });
    }
}