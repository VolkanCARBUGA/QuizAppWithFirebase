package com.volkan.quizappwithfirebase;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.volkan.quizappwithfirebase.databinding.FragmentHomeBinding;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
ArrayList<CategoryModel> categoryModels;
CategoryAdapter categoryAdapter;
FirebaseFirestore firebaseFirestore;
CategoryModel  model;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
FragmentHomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater,container,false);
        firebaseFirestore=FirebaseFirestore.getInstance();
        categoryModels=new ArrayList<>();
        categoryAdapter=new CategoryAdapter(getContext(),categoryModels);
        firebaseFirestore.collection("categories").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                categoryModels.clear();
                for (DocumentSnapshot snapshot:value.getDocuments()){
                    model=snapshot.toObject(CategoryModel.class);
                    model.setCategoryId(snapshot.getId());
                    categoryModels.add(model);
                }
                categoryAdapter.notifyDataSetChanged();
            }
        });
        binding.categoryList.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.categoryList.setAdapter(categoryAdapter);
        return binding.getRoot();
    }
}