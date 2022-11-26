package com.volkan.quizappwithfirebase;

import static com.google.common.reflect.Reflection.getPackageName;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.volkan.quizappwithfirebase.databinding.FragmentProfilBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ProfilFragment extends Fragment {
    FragmentProfilBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    FirebaseFirestore firebaseFirestore;
    User user = new User();
    HashMap<String, Object> data;
    Uri imageUri;
    DatabaseReference reference;
    StorageReference storageReference;
    Context context;
    FirebaseDatabase database;

    public ProfilFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfilBinding.inflate(inflater, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore.collection("Users").document((FirebaseAuth.getInstance().getUid()))
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(User.class);
                        assert user != null;
                        binding.txtUser.setText(user.getName());
                        binding.txtPassword.setText(user.getPass());
                        binding.txtEPosta.setText(user.getEmail());
                    }
                });

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.txtEPosta.getText().toString();
                String name = binding.txtUser.getText().toString();
                String pass = binding.txtPassword.getText().toString();
                data = new HashMap<>();
                data.put("email", email);
                data.put("name", name);
                data.put("pass", pass);
                veriGüncelle(data, firebaseAuth.getUid());
                uploadImage();
                // firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));

            }

        });
        binding.btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        binding.imageProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 100);
            }
        });
        return binding.getRoot();
    }


    private void uploadImage() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
        Date now = new Date();
        String fileName = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        binding.imageProfil.setImageURI(null);
                        Toast.makeText(getContext(), "Resim Yüklendi", Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Başarısız", Toast.LENGTH_SHORT).show();

                    }
                });

    }


    private void veriGüncelle(HashMap<String, Object> hashMap, String uId) {
        firebaseFirestore.collection("Users").document(uId)
                .update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Kişi Güncellendi", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}