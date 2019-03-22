package com.app.hopet.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hopet.Models.Animal;
import com.app.hopet.Models.Comment;
import com.app.hopet.R;
import com.app.hopet.Utilities.CommentListView;
import com.app.hopet.Utilities.DateTime;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {

    private String firebaseKey;
    private TextView topicText, animalTypeText, breedText, genderText, ageText, descriptionText;
    private EditText commentBox;
    private DatabaseReference databaseReference , commentReference;
    private FirebaseDatabase database;
    private double latitude, longitude;
    private ArrayList<Comment> comment;
    private CommentListView commentListView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initWidgets();
        initIntent();
        initData();
        comment = new ArrayList<>();
        initComment();
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
        commentBox = findViewById(R.id.comment_box);
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

    private void initComment(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },3000);

        recyclerView = findViewById(R.id.comment_recycle_view);
        Log.i("kakak","POOM "+recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(PostActivity.this));
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PostActivity.this));

        commentReference = FirebaseDatabase.getInstance().getReference().child("Post").child("Comment");
        commentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comment.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()){
                    Comment commentClass = postSnapShot.getValue(Comment.class);
                    if (commentClass.getKey().equals(firebaseKey)){
                        comment.add(commentClass);
                        Log.i("kakak",commentClass.getText());
                    }

                }
                Log.i("kakak",comment.size()+" Size");
                commentListView = new CommentListView(PostActivity.this,comment);
                recyclerView.setAdapter(commentListView);
                recyclerView.getAdapter().notifyDataSetChanged();
                recyclerView.setItemAnimator(new DefaultItemAnimator());
//                commentListView.notifyDataSetChanged();
                Toast.makeText(PostActivity.this, "comment loaded...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void clickButton(View view) {
        switch (view.getId()){
            case R.id.showDirectMap:
                String uri = "http://maps.google.com/maps/dir/?api=1&daddr=" + latitude + "," + longitude;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
                break;
            case R.id.sentButton:
                if (!commentBox.getText().toString().equals("")) {
                    String firebaseCommentKey = databaseReference.push().getKey();
                    Comment comment = new Comment(firebaseKey, "beer", commentBox.getText().toString(), DateTime.getDate());
                    commentBox.setText("");
                    database = FirebaseDatabase.getInstance();
                    database.getReference().child("Post").child("Comment").child(firebaseCommentKey).setValue(comment);
                }
                break;
            default:
                break;
        }
    }

}
