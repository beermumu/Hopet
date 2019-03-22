package com.app.hopet.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import com.app.hopet.Models.Animal;
import com.app.hopet.R;
import com.app.hopet.Utilities.CustomListView;

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
        initTakeFirebase();
        initGiveFirebase();
        listView = view.findViewById(R.id.newsfeed_listview);
        listView.setAdapter(customListView);

        return view;
    }

    private void initTakeFirebase() {
        final DatabaseReference databaseReference = database.getReference().child("Post").child("Take");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Animal addAnimal = dataSnapshot.getValue(Animal.class);
                Log.i("puu","K: "+addAnimal.getTopic());
                animals.add(addAnimal);
                key.add(dataSnapshot.getKey());
                customListView.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void initGiveFirebase() {
        final DatabaseReference databaseReference = database.getReference().child("Post").child("Give");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Animal addAnimal = dataSnapshot.getValue(Animal.class);
                Log.i("puu","K: "+addAnimal.getTopic());
                animals.add(addAnimal);
                key.add(dataSnapshot.getKey());
                customListView.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
