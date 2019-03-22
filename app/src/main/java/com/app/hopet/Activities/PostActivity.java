package com.app.hopet.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hopet.Models.Animal;
import com.app.hopet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bumptech.glide.Glide;

public class PostActivity extends AppCompatActivity {

    private String firebaseKey;
    private TextView topicText, animalTypeText, breedText, genderText, ageText, descriptionText;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private double latitude, longitude, currentLatitude, currentLongtitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initWidgets();
        initIntent();
        initData();
    }

    private void initIntent() {
        Intent intent = getIntent();
        firebaseKey = intent.getStringExtra("key");
    }

    private void initWidgets() {
        topicText = findViewById(R.id.showTopic);
        animalTypeText = findViewById(R.id.showAnimalType);
        breedText = findViewById(R.id.showBreed);
        genderText = findViewById(R.id.showGender);
        ageText = findViewById(R.id.showAge);
        descriptionText = findViewById(R.id.showDescription);
    }

    private void initData() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Post").child("Give");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.getKey().equals(firebaseKey)) {
                        Animal animal = dataSnapshot1.getValue(Animal.class);
                        topicText.setText(animal.getTopic());
                        animalTypeText.setText(animal.getType());
                        breedText.setText(animal.getBreed());
                        genderText.setText(animal.getGender());
                        ageText.setText(animal.getAge());
                        descriptionText.setText(animal.getDescription());
                        latitude = animal.getLatitude();
                        longitude = animal.getLongitude();
                        Glide.with(PostActivity.this).load(animal.getPhotoOne()).into((ImageView) findViewById(R.id.showImage));
                        Glide.with(PostActivity.this).load(animal.getPhotoTwo()).into((ImageView) findViewById(R.id.showImage2));
                        Glide.with(PostActivity.this).load(animal.getPhotoThree()).into((ImageView) findViewById(R.id.showImage3));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference = database.getReference().child("Post").child("Take");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.getKey().equals(firebaseKey)) {
                        Animal animal = dataSnapshot1.getValue(Animal.class);
                        topicText.setText(animal.getTopic());
                        animalTypeText.setText(animal.getType());
                        breedText.setText(animal.getBreed());
                        genderText.setText(animal.getGender());
                        ageText.setText(animal.getAge());
                        descriptionText.setText(animal.getDescription());
                        latitude = animal.getLatitude();
                        longitude = animal.getLongitude();
                        Glide.with(PostActivity.this).load(animal.getPhotoOne()).into((ImageView) findViewById(R.id.showImage));
                        Glide.with(PostActivity.this).load(animal.getPhotoTwo()).into((ImageView) findViewById(R.id.showImage2));
                        Glide.with(PostActivity.this).load(animal.getPhotoThree()).into((ImageView) findViewById(R.id.showImage3));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void clickButton(View view) {
        if (view.getId()==R.id.showDirectMap){
            String uri = "http://maps.google.com/maps/dir/?api=1&daddr=" + latitude + "," + longitude;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        }
    }
}
