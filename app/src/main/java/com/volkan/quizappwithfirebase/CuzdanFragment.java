package com.volkan.quizappwithfirebase;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.volkan.quizappwithfirebase.databinding.FragmentCuzdanBinding;

import java.util.Objects;
import java.util.concurrent.Executor;

public class CuzdanFragment extends Fragment {
    FragmentCuzdanBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    User user;
WithdrawRequest  withdrawRequest;
    public CuzdanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCuzdanBinding.inflate(inflater, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").document((FirebaseAuth.getInstance().getUid()))
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                assert user != null;
                binding.currentCoins.setText(String.valueOf(user.getCoins()));
            }
        });
        binding.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getCoins()>50000){
                    String uId=FirebaseAuth.getInstance().getUid();
                    String eMail=binding.emailBox.getText().toString();

                    withdrawRequest=new WithdrawRequest(uId,eMail,user.getName());

                    firebaseFirestore.collection("withdraws").document(uId).set(withdrawRequest).addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(), "istek Başarıyla Gönderildi", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(getContext(), "Paranı  Çekmek için daha Fazla madeni para Topla", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return binding.getRoot();
    }
}