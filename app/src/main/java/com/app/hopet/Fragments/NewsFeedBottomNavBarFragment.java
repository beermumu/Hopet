package com.app.hopet.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

import com.app.hopet.Models.Animal;
import com.app.hopet.R;
import com.app.hopet.Utilities.CustomListView;
import com.google.firebase.database.ValueEventListener;

public class NewsFeedBottomNavBarFragment extends Fragment {
    private FirebaseDatabase database;
    private CustomListView customListView ;
    private RecyclerView recyclerView;
    private ArrayList<Animal> animals;
    private ArrayList<String> key;

    public NewsFeedBottomNavBarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_news_feed, container, false);
        animals = new ArrayList<>();
        key = new ArrayList<>();
        customListView = new CustomListView(getContext(),animals,key);
        recyclerView = view.findViewById(R.id.newsfeed_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        database = FirebaseDatabase.getInstance();
        initDataFirebase();
        recyclerView.setAdapter(customListView);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    private void initDataFirebase() {
        final DatabaseReference databaseReference = database.getReference().child("Post").child("Data");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Animal addAnimal = dataSnapshot1.getValue(Animal.class);
                    if (addAnimal.isStatus()){
                        Log.i("puu","K: "+addAnimal.getTopic());
                        animals.add(addAnimal);
                        key.add(dataSnapshot1.getKey());
                    }
                }
                Collections.reverse(animals);
                Collections.reverse(key);
                customListView.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
