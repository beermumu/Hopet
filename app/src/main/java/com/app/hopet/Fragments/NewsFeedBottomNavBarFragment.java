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
import com.app.hopet.utilities.CustomListView;

public class NewsFeedBottomNavBarFragment extends Fragment {
    private FirebaseDatabase database;
    private CustomListView customListView;
    private ListView listView;
    private ArrayList<Animal> animals;

    public NewsFeedBottomNavBarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_news_feed, container, false);

        animals = new ArrayList<>();

        customListView = new CustomListView(getContext(),0,animals);

        database = FirebaseDatabase.getInstance();

        initTakeDogFirebase();
        initGiveDogFirebase();
        initTakeCatFirebase();
        initGiveCatFirebase();

        listView = view.findViewById(R.id.newsfeed_listview);
        listView.setAdapter(customListView);


        return view;
    }

    private void initTakeDogFirebase() {
        DatabaseReference databaseReference = database.getReference().child("Post").child("Take").child("Dog");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Animal addAnimal = dataSnapshot.getValue(Animal.class);
                Log.i("poom","K: "+addAnimal.getTopic());
                if (addAnimal != null) {
                    animals.add(addAnimal);
                    customListView.notifyDataSetChanged();
                }

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

    private void initGiveDogFirebase() {
        DatabaseReference databaseReference = database.getReference().child("Post").child("Give").child("Dog");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Animal addAnimal = dataSnapshot.getValue(Animal.class);
                Log.i("poom","K: "+addAnimal.getTopic());
                if (addAnimal != null) {
                    animals.add(addAnimal);
                    customListView.notifyDataSetChanged();
                }

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

    private void initTakeCatFirebase() {
        DatabaseReference databaseReference = database.getReference().child("Post").child("Take").child("Cat");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Animal addAnimal = dataSnapshot.getValue(Animal.class);
                Log.i("poom","K: "+addAnimal.getTopic());
                if (addAnimal != null) {
                    animals.add(addAnimal);
                    customListView.notifyDataSetChanged();
                }

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

    private void initGiveCatFirebase() {
        DatabaseReference databaseReference = database.getReference().child("Post").child("Give").child("Cat");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Animal addAnimal = dataSnapshot.getValue(Animal.class);
                Log.i("poom","K: "+addAnimal.getTopic());
                if (addAnimal != null) {
                    animals.add(addAnimal);
                    customListView.notifyDataSetChanged();
                }

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
