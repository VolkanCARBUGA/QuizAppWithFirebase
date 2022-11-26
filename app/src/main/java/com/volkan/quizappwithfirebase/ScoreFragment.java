package com.volkan.quizappwithfirebase;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.volkan.quizappwithfirebase.databinding.FragmentScoreBinding;

import java.util.ArrayList;

public class ScoreFragment extends Fragment {
    FirebaseFirestore firebaseFirestore;
    FragmentScoreBinding binding;
    ArrayList<User> userArrayList;
    LeaderboardsAdapter leaderboardsAdapter;
    User user;

    public ScoreFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentScoreBinding.inflate(inflater, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        userArrayList = new ArrayList<>();
        leaderboardsAdapter = new LeaderboardsAdapter(getContext(), userArrayList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(leaderboardsAdapter);
        firebaseFirestore.collection("Users").orderBy("coins", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    user = snapshot.toObject(User.class);
                    userArrayList.add(user);
                }
                leaderboardsAdapter.notifyDataSetChanged();
            }
        });
        return binding.getRoot();
    }
}