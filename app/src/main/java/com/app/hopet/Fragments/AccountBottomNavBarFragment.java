package com.app.hopet.Fragments;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hopet.Activities.LoginActivity;
import com.app.hopet.Models.Animal;
import com.app.hopet.R;
import com.app.hopet.Utilities.TopicListView;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class AccountBottomNavBarFragment extends Fragment {
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    private TextView nameView, emailView;
    private Button logoutBtn;
    private ImageView profilePic;
    private ArrayList<Animal> animals;
    private ArrayList<String> key;
    private TopicListView topicListView;
    private String url;


    public AccountBottomNavBarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_account, container, false);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        nameView = view.findViewById(R.id.accountShowName);
        emailView = view.findViewById(R.id.accountShowEmail);
        logoutBtn = view.findViewById(R.id.logoutBtn);
        profilePic = view.findViewById(R.id.accountShowProfilePicture);
        initData();
        clickButton(view);


        animals = new ArrayList<>();
        key = new ArrayList<>();
        topicListView = new TopicListView(getContext(), animals, key);
        recyclerView = view.findViewById(R.id.yourTopicView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        database = FirebaseDatabase.getInstance();
        initDataFirebase();
        recyclerView.setAdapter(topicListView);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.setItemAnimator(new DefaultItemAnimator());




        return view;
    }

    private void initData() {
        nameView.setText(firebaseUser.getDisplayName());
        emailView.setText("E-mail : " + firebaseUser.getEmail());

        Bundle parameters = new Bundle();
        parameters.putString("fields", "picture.type(large)");
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                            Log.i("chocobo", url);
                            Glide.with(getContext()).load(url).into(profilePic);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void clickButton(View view) {
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initDataFirebase() {
        final DatabaseReference databaseReference = database.getReference().child("Post").child("Data");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Animal addAnimal = dataSnapshot1.getValue(Animal.class);
                    if (addAnimal.isStatus() && addAnimal.getUser().getEmail().equals(firebaseUser.getEmail())) {
                        Log.i("puu", "K: " + addAnimal.getTopic());
                        animals.add(addAnimal);
                        key.add(dataSnapshot1.getKey());
                    }
                }
                Collections.reverse(animals);
                Collections.reverse(key);
                topicListView.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
