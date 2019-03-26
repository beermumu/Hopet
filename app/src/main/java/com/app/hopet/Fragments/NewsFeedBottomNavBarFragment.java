package com.app.hopet.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.hopet.Models.User;
import com.app.hopet.Utilities.UserManager;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;

import com.app.hopet.Models.Animal;
import com.app.hopet.R;
import com.app.hopet.Utilities.CustomListView;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

public class NewsFeedBottomNavBarFragment extends Fragment {
    private FirebaseDatabase database;
    private CustomListView customListView ;
    private ListView listView;
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
        customListView = new CustomListView(getContext(),0,animals,key);
        database = FirebaseDatabase.getInstance();
        initDataFirebase();
        listView = view.findViewById(R.id.newsfeed_listview);
        listView.setAdapter(customListView);

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
