package com.app.hopet.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hopet.Activities.CreatePostActivity;
import com.app.hopet.Activities.LoginActivity;
import com.app.hopet.Activities.TopicActivity;
import com.app.hopet.Models.Animal;
import com.app.hopet.R;
import com.app.hopet.Utilities.TopicListView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
                            Glide.with(getContext()).load(url).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.not_found_image)).into(profilePic);
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
                Toast.makeText(getContext(), getContext().getString(R.string.logout_success), Toast.LENGTH_LONG).show();
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

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.account_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.account_menu) {

            final DatabaseReference databaseReference = database.getReference().child("Post").child("Data");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long[] count = new long[12];
                    long[] countBreed = new long[11];
                    long[] countAge = new long[9];
                    String mostType = "";
                    String mostBreed = "";
                    String mostGender = "";
                    String mostAge = "";
                    String mostTopicType = "";
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Animal addAnimal = dataSnapshot1.getValue(Animal.class);
                        //check topic
                        count[0]++;
                        if (addAnimal.isStatus()) {
                            //check avaliable topic
                            count[1]++;
                        }
                        if (!addAnimal.isStatus()) {
                            //check not avaliable topic
                            count[11]++;
                        }
                        //check type
                        if (addAnimal.getType().equals("Dog")) {
                            count[2]++;
                            if (count[2] > count[3] && count[2] > count[4] && count[2] > count[5] && count[2] > count[6]) {
                                mostType = "Dog";
                            }
                        }
                        if (addAnimal.getType().equals("Cat")) {
                            count[3]++;
                            if (count[3] > count[2] && count[3] > count[4] && count[3] > count[5] && count[3] > count[6]) {
                                mostType = "Cat";
                            }
                        }
                        if (addAnimal.getType().equals("Bird")) {
                            count[4]++;
                            if (count[4] > count[3] && count[4] > count[2] && count[4] > count[5] && count[4] > count[6]) {
                                mostType = "Bird";
                            }
                        }
                        if (addAnimal.getType().equals("Rat")) {
                            count[5]++;
                            if (count[5] > count[3] && count[5] > count[4] && count[5] > count[2] && count[5] > count[6]) {
                                mostType = "Rat";
                            }
                        }
                        if (addAnimal.getType().equals("Other")) {
                            count[6]++;
                            if (count[6] > count[3] && count[6] > count[4] && count[6] > count[5] && count[6] > count[2]) {
                                mostType = "Other";
                            }
                        }
                        //check gender
                        if (addAnimal.getGender().equals("Male")) {
                            count[7]++;
                            if (count[7] > count[8]) {
                                mostGender = "Male";
                            }
                        }
                        if (addAnimal.getGender().equals("Femail")) {
                            count[8]++;
                            if (count[8] > count[7]) {
                                mostGender = "Female";
                            }
                        }
                        //check topicType
                        if (addAnimal.getTopicType().equals("Give")) {
                            count[9]++;
                            if (count[9] > count[10]) {
                                mostTopicType = "Give";
                            }
                        }
                        if (addAnimal.getTopicType().equals("Take")) {
                            count[10]++;
                            if (count[10] > count[9]) {
                                mostTopicType = "Take";
                            }
                        }
                        //check age
                        if (addAnimal.getAge().equals("1-3 Month")) {
                            countAge[0]++;
                            if (countAge[0] > countAge[1] && countAge[0] > countAge[2] && countAge[0] > countAge[3] && countAge[0] > countAge[4] && countAge[0] > countAge[5] && countAge[0] > countAge[6] && countAge[0] > countAge[7] && countAge[0] > countAge[8]) {
                                mostAge = "1-3 Month";
                            }
                        }
                        if (addAnimal.getAge().equals("4-6 Month")) {
                            countAge[1]++;
                            if (countAge[1] > countAge[0] && countAge[1] > countAge[2] && countAge[1] > countAge[3] && countAge[1] > countAge[4] && countAge[1] > countAge[5] && countAge[1] > countAge[6] && countAge[1] > countAge[7] && countAge[1] > countAge[8]) {
                                mostAge = "4-6 Month";
                            }
                        }
                        if (addAnimal.getAge().equals("7-9 Month")) {
                            countAge[2]++;
                            if (countAge[2] > countAge[1] && countAge[2] > countAge[0] && countAge[2] > countAge[3] && countAge[2] > countAge[4] && countAge[2] > countAge[5] && countAge[2] > countAge[6] && countAge[2] > countAge[7] && countAge[2] > countAge[8]) {
                                mostAge = "7-9 Month";
                            }
                        }
                        if (addAnimal.getAge().equals("10-12 Month")) {
                            countAge[3]++;
                            if (countAge[3] > countAge[1] && countAge[3] > countAge[2] && countAge[3] > countAge[0] && countAge[3] > countAge[4] && countAge[3] > countAge[5] && countAge[3] > countAge[6] && countAge[3] > countAge[7] && countAge[3] > countAge[8]) {
                                mostAge = "10-12 Month";
                            }
                        }
                        if (addAnimal.getAge().equals("1-3 Year")) {
                            countAge[4]++;
                            if (countAge[4] > countAge[1] && countAge[4] > countAge[2] && countAge[4] > countAge[3] && countAge[4] > countAge[0] && countAge[4] > countAge[5] && countAge[4] > countAge[6] && countAge[4] > countAge[7] && countAge[4] > countAge[8]) {
                                mostAge = "1-3 Year";
                            }
                        }
                        if (addAnimal.getAge().equals("4-6 Year")) {
                            countAge[5]++;
                            if (countAge[5] > countAge[1] && countAge[5] > countAge[2] && countAge[5] > countAge[3] && countAge[5] > countAge[4] && countAge[5] > countAge[0] && countAge[5] > countAge[6] && countAge[5] > countAge[7] && countAge[5] > countAge[8]) {
                                mostAge = "4-6 Year";
                            }
                        }
                        if (addAnimal.getAge().equals("7-9 Year")) {
                            countAge[6]++;
                            if (countAge[6] > countAge[1] && countAge[6] > countAge[2] && countAge[6] > countAge[3] && countAge[6] > countAge[4] && countAge[6] > countAge[5] && countAge[6] > countAge[0] && countAge[6] > countAge[7] && countAge[6] > countAge[8]) {
                                mostAge = "7-9 Year";
                            }
                        }
                        if (addAnimal.getAge().equals("10-12 Year")) {
                            countAge[7]++;
                            if (countAge[7] > countAge[1] && countAge[7] > countAge[2] && countAge[7] > countAge[3] && countAge[7] > countAge[4] && countAge[7] > countAge[5] && countAge[7] > countAge[6] && countAge[7] > countAge[0] && countAge[7] > countAge[8]) {
                                mostAge = "10-12 Year";
                            }
                        }
                        if (addAnimal.getAge().equals("More than 12")) {
                            countAge[8]++;
                            if (countAge[8] > countAge[1] && countAge[8] > countAge[2] && countAge[8] > countAge[3] && countAge[8] > countAge[4] && countAge[8] > countAge[5] && countAge[8] > countAge[6] && countAge[8] > countAge[7] && countAge[8] > countAge[0]) {
                                mostAge = "More than 12";
                            }
                        }
                    }
                    //check breed
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Animal addAnimal = dataSnapshot1.getValue(Animal.class);

                        if (mostType.equals("Dog")) {
                            if (addAnimal.getType().equals("Dog")) {
                                if (addAnimal.getBreed().equals("Other")) {
                                    countBreed[0]++;
                                    if (countBreed[0] > countBreed[1] && countBreed[0] > countBreed[2] && countBreed[0] > countBreed[3] && countBreed[0] > countBreed[4] && countBreed[0] > countBreed[5] && countBreed[0] > countBreed[6] && countBreed[0] > countBreed[7] && countBreed[0] > countBreed[8] && countBreed[0] > countBreed[9] && countBreed[0] > countBreed[10]) {
                                        mostBreed = "Other";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Labrador")) {
                                    countBreed[1]++;
                                    if (countBreed[1] > countBreed[0] && countBreed[1] > countBreed[2] && countBreed[1] > countBreed[3] && countBreed[1] > countBreed[4] && countBreed[1] > countBreed[5] && countBreed[1] > countBreed[6] && countBreed[1] > countBreed[7] && countBreed[1] > countBreed[8] && countBreed[1] > countBreed[9] && countBreed[1] > countBreed[10]) {
                                        mostBreed = "Labrador";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Golden")) {
                                    countBreed[2]++;
                                    if (countBreed[2] > countBreed[1] && countBreed[2] > countBreed[0] && countBreed[2] > countBreed[3] && countBreed[2] > countBreed[4] && countBreed[2] > countBreed[5] && countBreed[2] > countBreed[6] && countBreed[2] > countBreed[7] && countBreed[2] > countBreed[8] && countBreed[2] > countBreed[9] && countBreed[2] > countBreed[10]) {
                                        mostBreed = "Golden";
                                    }
                                }
                                if (addAnimal.getBreed().equals("German Shepherd")) {
                                    countBreed[3]++;
                                    if (countBreed[3] > countBreed[1] && countBreed[3] > countBreed[2] && countBreed[3] > countBreed[0] && countBreed[3] > countBreed[4] && countBreed[3] > countBreed[5] && countBreed[3] > countBreed[6] && countBreed[3] > countBreed[7] && countBreed[3] > countBreed[8] && countBreed[3] > countBreed[9] && countBreed[3] > countBreed[10]) {
                                        mostBreed = "German Shepherd";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Bulldogs")) {
                                    countBreed[4]++;
                                    if (countBreed[4] > countBreed[1] && countBreed[4] > countBreed[2] && countBreed[4] > countBreed[3] && countBreed[4] > countBreed[0] && countBreed[4] > countBreed[5] && countBreed[4] > countBreed[6] && countBreed[4] > countBreed[7] && countBreed[4] > countBreed[8] && countBreed[4] > countBreed[9] && countBreed[4] > countBreed[10]) {
                                        mostBreed = "Bulldogs";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Beagles")) {
                                    countBreed[5]++;
                                    if (countBreed[5] > countBreed[1] && countBreed[5] > countBreed[2] && countBreed[5] > countBreed[3] && countBreed[5] > countBreed[4] && countBreed[5] > countBreed[0] && countBreed[5] > countBreed[6] && countBreed[5] > countBreed[7] && countBreed[5] > countBreed[8] && countBreed[5] > countBreed[9] && countBreed[5] > countBreed[10]) {
                                        mostBreed = "Beagles";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Poodles")) {
                                    countBreed[6]++;
                                    if (countBreed[6] > countBreed[1] && countBreed[6] > countBreed[2] && countBreed[6] > countBreed[3] && countBreed[6] > countBreed[4] && countBreed[6] > countBreed[5] && countBreed[6] > countBreed[0] && countBreed[6] > countBreed[7] && countBreed[6] > countBreed[8] && countBreed[6] > countBreed[9] && countBreed[6] > countBreed[10]) {
                                        mostBreed = "Poodles";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Pug")) {
                                    countBreed[7]++;
                                    if (countBreed[7] > countBreed[1] && countBreed[7] > countBreed[2] && countBreed[7] > countBreed[3] && countBreed[7] > countBreed[4] && countBreed[7] > countBreed[5] && countBreed[7] > countBreed[6] && countBreed[7] > countBreed[0] && countBreed[7] > countBreed[8] && countBreed[7] > countBreed[9] && countBreed[7] > countBreed[10]) {
                                        mostBreed = "Pug";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Shih Tzu")) {
                                    countBreed[8]++;
                                    if (countBreed[8] > countBreed[1] && countBreed[8] > countBreed[2] && countBreed[8] > countBreed[3] && countBreed[8] > countBreed[4] && countBreed[8] > countBreed[5] && countBreed[8] > countBreed[6] && countBreed[8] > countBreed[7] && countBreed[8] > countBreed[0] && countBreed[8] > countBreed[9] && countBreed[8] > countBreed[10]) {
                                        mostBreed = "Shih Tzu";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Corgis")) {
                                    countBreed[9]++;
                                    if (countBreed[9] > countBreed[1] && countBreed[9] > countBreed[2] && countBreed[9] > countBreed[3] && countBreed[9] > countBreed[4] && countBreed[9] > countBreed[5] && countBreed[9] > countBreed[6] && countBreed[9] > countBreed[7] && countBreed[9] > countBreed[8] && countBreed[9] > countBreed[0] && countBreed[9] > countBreed[10]) {
                                        mostBreed = "Corgis";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Siberian Huskies")) {
                                    countBreed[10]++;
                                    if (countBreed[10] > countBreed[1] && countBreed[10] > countBreed[2] && countBreed[10] > countBreed[3] && countBreed[10] > countBreed[4] && countBreed[10] > countBreed[5] && countBreed[10] > countBreed[6] && countBreed[10] > countBreed[7] && countBreed[10] > countBreed[8] && countBreed[10] > countBreed[9] && countBreed[10] > countBreed[0]) {
                                        mostBreed = "Siberian Huskies";
                                    }
                                }
                            }
                        } else if (mostType.equals("Cat")) {
                            if (addAnimal.getType().equals("Cat")) {
                                if (addAnimal.getBreed().equals("Other")) {
                                    countBreed[0]++;
                                    if (countBreed[0] > countBreed[1] && countBreed[0] > countBreed[2] && countBreed[0] > countBreed[3] && countBreed[0] > countBreed[4] && countBreed[0] > countBreed[5] && countBreed[0] > countBreed[6] && countBreed[0] > countBreed[7] && countBreed[0] > countBreed[8] && countBreed[0] > countBreed[9] && countBreed[0] > countBreed[10]) {
                                        mostBreed = "Other";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Siamese")) {
                                    countBreed[1]++;
                                    if (countBreed[1] > countBreed[0] && countBreed[1] > countBreed[2] && countBreed[1] > countBreed[3] && countBreed[1] > countBreed[4] && countBreed[1] > countBreed[5] && countBreed[1] > countBreed[6] && countBreed[1] > countBreed[7] && countBreed[1] > countBreed[8] && countBreed[1] > countBreed[9] && countBreed[1] > countBreed[10]) {
                                        mostBreed = "Siamese";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Persian")) {
                                    countBreed[2]++;
                                    if (countBreed[2] > countBreed[1] && countBreed[2] > countBreed[0] && countBreed[2] > countBreed[3] && countBreed[2] > countBreed[4] && countBreed[2] > countBreed[5] && countBreed[2] > countBreed[6] && countBreed[2] > countBreed[7] && countBreed[2] > countBreed[8] && countBreed[2] > countBreed[9] && countBreed[2] > countBreed[10]) {
                                        mostBreed = "Persian";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Maine Coon")) {
                                    countBreed[3]++;
                                    if (countBreed[3] > countBreed[1] && countBreed[3] > countBreed[2] && countBreed[3] > countBreed[0] && countBreed[3] > countBreed[4] && countBreed[3] > countBreed[5] && countBreed[3] > countBreed[6] && countBreed[3] > countBreed[7] && countBreed[3] > countBreed[8] && countBreed[3] > countBreed[9] && countBreed[3] > countBreed[10]) {
                                        mostBreed = "Maine Coon";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Ragdoll")) {
                                    countBreed[4]++;
                                    if (countBreed[4] > countBreed[1] && countBreed[4] > countBreed[2] && countBreed[4] > countBreed[3] && countBreed[4] > countBreed[0] && countBreed[4] > countBreed[5] && countBreed[4] > countBreed[6] && countBreed[4] > countBreed[7] && countBreed[4] > countBreed[8] && countBreed[4] > countBreed[9] && countBreed[4] > countBreed[10]) {
                                        mostBreed = "Ragdoll";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Bengal")) {
                                    countBreed[5]++;
                                    if (countBreed[5] > countBreed[1] && countBreed[5] > countBreed[2] && countBreed[5] > countBreed[3] && countBreed[5] > countBreed[4] && countBreed[5] > countBreed[0] && countBreed[5] > countBreed[6] && countBreed[5] > countBreed[7] && countBreed[5] > countBreed[8] && countBreed[5] > countBreed[9] && countBreed[5] > countBreed[10]) {
                                        mostBreed = "Bengal";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Abyssinian")) {
                                    countBreed[6]++;
                                    if (countBreed[6] > countBreed[1] && countBreed[6] > countBreed[2] && countBreed[6] > countBreed[3] && countBreed[6] > countBreed[4] && countBreed[6] > countBreed[5] && countBreed[6] > countBreed[0] && countBreed[6] > countBreed[7] && countBreed[6] > countBreed[8] && countBreed[6] > countBreed[9] && countBreed[6] > countBreed[10]) {
                                        mostBreed = "Abyssinian";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Birman")) {
                                    countBreed[7]++;
                                    if (countBreed[7] > countBreed[1] && countBreed[7] > countBreed[2] && countBreed[7] > countBreed[3] && countBreed[7] > countBreed[4] && countBreed[7] > countBreed[5] && countBreed[7] > countBreed[6] && countBreed[7] > countBreed[0] && countBreed[7] > countBreed[8] && countBreed[7] > countBreed[9] && countBreed[7] > countBreed[10]) {
                                        mostBreed = "Birman";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Oriental Shorthair")) {
                                    countBreed[8]++;
                                    if (countBreed[8] > countBreed[1] && countBreed[8] > countBreed[2] && countBreed[8] > countBreed[3] && countBreed[8] > countBreed[4] && countBreed[8] > countBreed[5] && countBreed[8] > countBreed[6] && countBreed[8] > countBreed[7] && countBreed[8] > countBreed[0] && countBreed[8] > countBreed[9] && countBreed[8] > countBreed[10]) {
                                        mostBreed = "Oriental Shorthair";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Sphynx")) {
                                    countBreed[9]++;
                                    if (countBreed[9] > countBreed[1] && countBreed[9] > countBreed[2] && countBreed[9] > countBreed[3] && countBreed[9] > countBreed[4] && countBreed[9] > countBreed[5] && countBreed[9] > countBreed[6] && countBreed[9] > countBreed[7] && countBreed[9] > countBreed[8] && countBreed[9] > countBreed[0] && countBreed[9] > countBreed[10]) {
                                        mostBreed = "Sphynx";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Himalayan")) {
                                    countBreed[10]++;
                                    if (countBreed[10] > countBreed[1] && countBreed[10] > countBreed[2] && countBreed[10] > countBreed[3] && countBreed[10] > countBreed[4] && countBreed[10] > countBreed[5] && countBreed[10] > countBreed[6] && countBreed[10] > countBreed[7] && countBreed[10] > countBreed[8] && countBreed[10] > countBreed[9] && countBreed[10] > countBreed[0]) {
                                        mostBreed = "Himalayan";
                                    }
                                }
                            }
                        } else if (mostType.equals("Bird")) {
                            if (addAnimal.getType().equals("Bird")) {
                                if (addAnimal.getBreed().equals("Other")) {
                                    countBreed[0]++;
                                    if (countBreed[0] > countBreed[1] && countBreed[0] > countBreed[2] && countBreed[0] > countBreed[3] && countBreed[0] > countBreed[4] && countBreed[0] > countBreed[5] && countBreed[0] > countBreed[6]) {
                                        mostBreed = "Other";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Budgies")) {
                                    countBreed[1]++;
                                    if (countBreed[1] > countBreed[0] && countBreed[1] > countBreed[2] && countBreed[1] > countBreed[3] && countBreed[1] > countBreed[4] && countBreed[1] > countBreed[5] && countBreed[1] > countBreed[6]) {
                                        mostBreed = "Budgies";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Cockatiels")) {
                                    countBreed[2]++;
                                    if (countBreed[2] > countBreed[1] && countBreed[2] > countBreed[0] && countBreed[2] > countBreed[3] && countBreed[2] > countBreed[4] && countBreed[2] > countBreed[5] && countBreed[2] > countBreed[6]) {
                                        mostBreed = "Cockatiels";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Finches and Canaries")) {
                                    countBreed[3]++;
                                    if (countBreed[3] > countBreed[1] && countBreed[3] > countBreed[2] && countBreed[3] > countBreed[0] && countBreed[3] > countBreed[4] && countBreed[3] > countBreed[5] && countBreed[3] > countBreed[6]) {
                                        mostBreed = "Finches and Canaries";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Parrot")) {
                                    countBreed[4]++;
                                    if (countBreed[4] > countBreed[1] && countBreed[4] > countBreed[2] && countBreed[4] > countBreed[3] && countBreed[4] > countBreed[0] && countBreed[4] > countBreed[5] && countBreed[4] > countBreed[6]) {
                                        mostBreed = "Parrot";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Lovebirds")) {
                                    countBreed[5]++;
                                    if (countBreed[5] > countBreed[1] && countBreed[5] > countBreed[2] && countBreed[5] > countBreed[3] && countBreed[5] > countBreed[4] && countBreed[5] > countBreed[0] && countBreed[5] > countBreed[6]) {
                                        mostBreed = "Lovebirds";
                                    }
                                }
                                if (addAnimal.getBreed().equals("African Greys")) {
                                    countBreed[6]++;
                                    if (countBreed[6] > countBreed[1] && countBreed[6] > countBreed[2] && countBreed[6] > countBreed[3] && countBreed[6] > countBreed[4] && countBreed[6] > countBreed[5] && countBreed[6] > countBreed[0]) {
                                        mostBreed = "African Greys";
                                    }
                                }
                            }
                        } else if (mostType.equals("Rat")) {
                            if (addAnimal.getType().equals("Rat")) {
                                if (addAnimal.getBreed().equals("Other")) {
                                    countBreed[0]++;
                                    if (countBreed[0] > countBreed[1] && countBreed[0] > countBreed[2] && countBreed[0] > countBreed[3] && countBreed[0] > countBreed[4] && countBreed[0] > countBreed[5] && countBreed[0] > countBreed[6] && countBreed[0] > countBreed[7] && countBreed[0] > countBreed[8]) {
                                        mostBreed = "Other";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Self")) {
                                    countBreed[1]++;
                                    if (countBreed[1] > countBreed[0] && countBreed[1] > countBreed[2] && countBreed[1] > countBreed[3] && countBreed[1] > countBreed[4] && countBreed[1] > countBreed[5] && countBreed[1] > countBreed[6] && countBreed[1] > countBreed[7] && countBreed[1] > countBreed[8]) {
                                        mostBreed = "Self";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Berkshire")) {
                                    countBreed[2]++;
                                    if (countBreed[2] > countBreed[1] && countBreed[2] > countBreed[0] && countBreed[2] > countBreed[3] && countBreed[2] > countBreed[4] && countBreed[2] > countBreed[5] && countBreed[2] > countBreed[6] && countBreed[2] > countBreed[7] && countBreed[2] > countBreed[8]) {
                                        mostBreed = "Berkshire";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Blazed")) {
                                    countBreed[3]++;
                                    if (countBreed[3] > countBreed[1] && countBreed[3] > countBreed[2] && countBreed[3] > countBreed[0] && countBreed[3] > countBreed[4] && countBreed[3] > countBreed[5] && countBreed[3] > countBreed[6] && countBreed[3] > countBreed[7] && countBreed[3] > countBreed[8]) {
                                        mostBreed = "Blazed";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Capped")) {
                                    countBreed[4]++;
                                    if (countBreed[4] > countBreed[1] && countBreed[4] > countBreed[2] && countBreed[4] > countBreed[3] && countBreed[4] > countBreed[0] && countBreed[4] > countBreed[5] && countBreed[4] > countBreed[6] && countBreed[4] > countBreed[7] && countBreed[4] > countBreed[8]) {
                                        mostBreed = "Capped";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Hooded")) {
                                    countBreed[5]++;
                                    if (countBreed[5] > countBreed[1] && countBreed[5] > countBreed[2] && countBreed[5] > countBreed[3] && countBreed[5] > countBreed[4] && countBreed[5] > countBreed[0] && countBreed[5] > countBreed[6] && countBreed[5] > countBreed[7] && countBreed[5] > countBreed[8]) {
                                        mostBreed = "Hooded";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Irish")) {
                                    countBreed[6]++;
                                    if (countBreed[6] > countBreed[1] && countBreed[6] > countBreed[2] && countBreed[6] > countBreed[3] && countBreed[6] > countBreed[4] && countBreed[6] > countBreed[5] && countBreed[6] > countBreed[0] && countBreed[6] > countBreed[7] && countBreed[6] > countBreed[8]) {
                                        mostBreed = "Irish";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Masked")) {
                                    countBreed[7]++;
                                    if (countBreed[7] > countBreed[1] && countBreed[7] > countBreed[2] && countBreed[7] > countBreed[3] && countBreed[7] > countBreed[4] && countBreed[7] > countBreed[5] && countBreed[7] > countBreed[6] && countBreed[7] > countBreed[0] && countBreed[7] > countBreed[8]) {
                                        mostBreed = "Masked";
                                    }
                                }
                                if (addAnimal.getBreed().equals("Husky")) {
                                    countBreed[8]++;
                                    if (countBreed[8] > countBreed[1] && countBreed[8] > countBreed[2] && countBreed[8] > countBreed[3] && countBreed[8] > countBreed[4] && countBreed[8] > countBreed[5] && countBreed[8] > countBreed[6] && countBreed[8] > countBreed[7] && countBreed[8] > countBreed[0]) {
                                        mostBreed = "Husky";
                                    }
                                }
                            }
                        } else {
                            mostBreed = "Other";
                        }
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setTitle(getContext().getString(R.string.show_stat));
                    builder.setMessage(getContext().getString(R.string.all_topic) + count[0] + "\n" + getContext().getString(R.string.topic_ava) + count[1] + "\n" + getContext().getString(R.string.topic_not_ava) + count[11] + "\n" + getContext().getString(R.string.most_animal_type) + mostType + "\n" + getContext().getString(R.string.most_breed) + mostBreed + "\n" + getContext().getString(R.string.most_gender) + mostGender + "\n" + getContext().getString(R.string.most_age) + mostAge + "\n" + getContext().getString(R.string.most_topic_type) + mostTopicType);
                    builder.setNegativeButton(getContext().getString(R.string.close), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
        return true;
    }

}
