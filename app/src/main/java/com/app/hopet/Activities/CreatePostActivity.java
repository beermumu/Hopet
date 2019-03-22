package com.app.hopet.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hopet.Models.Animal;
import com.app.hopet.Utilities.UserManager;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.app.hopet.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreatePostActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private EditText topicEditText, descriptionEditText;
    private TextView locationTextView;
    private Spinner genderSpinner, breedSpinner, ageSpinner, animalTypeSpinner;
    private String selectGenderSpinner, selectBreedSpinner, selectAgeSpinner, selectAnimalTypeSpinner;
    private String animalSend, typeSend;
    private double latitude, longitude;
    private ImageView uploadImage1Button, uploadImage2Button, uploadImage3Button;
    private String uploadImage1URL, uploadImage2URL, uploadImage3URL;
    private String firebaseKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Create a post");
        setContentView(R.layout.activity_create_post);

        initWidgets();
        initIntent();
        initAnimalTypeSpinner();
        initBreedSpinner();
        initGenderSpinner();
        initAgeSpinner();
        initFirebase();
        initUploadImages();
    }

    private void initWidgets() {
        breedSpinner = findViewById(R.id.breedSpinner);
        animalTypeSpinner = findViewById(R.id.animalSpinner);
        genderSpinner = findViewById(R.id.genderSpinner);
        ageSpinner = findViewById(R.id.ageSpinner);
        topicEditText = findViewById(R.id.topicEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        locationTextView = findViewById(R.id.locationTextView);
    }

    private void initIntent() {
        Intent intent = getIntent();
        String send = intent.getStringExtra("sent");
        if (send.equals("take")) {
            typeSend = "Take";
        } else if (send.equals("give")) {
            typeSend = "Give";
        }
    }

    private void initAnimalTypeSpinner() {
        final String[] animalList = getResources().getStringArray(R.array.animal_type_list);
        final ArrayAdapter<String> animalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, animalList);

        animalSend = "Dog";
        animalTypeSpinner.setAdapter(animalAdapter);
        selectAnimalTypeSpinner = animalList[0];
        animalTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectAnimalTypeSpinner = animalList[position];
                animalSend = animalList[position];
                initBreedSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initBreedSpinner() {
        breedSpinner.setEnabled(true);
        if (animalSend.equals("Dog")) {
            initDogBreedSpinner();
        } else if (animalSend.equals("Cat")) {
            initCatBreedSpinner();
        } else if (animalSend.equals("Bird")){
            initBirdBreedSpinner();
        } else if (animalSend.equals("Rat")){
            initRatBreedSpinner();
        } else if(animalSend.equals("Other")) {
            breedSpinner.setEnabled(false);
            selectBreedSpinner = "-";
        }
    }

    private void initBirdBreedSpinner() {
        final String[] breedList = getResources().getStringArray(R.array.breed_bird_list);
        ArrayAdapter<String> breedAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, breedList);

        breedSpinner.setAdapter(breedAdapter);
        selectBreedSpinner = breedList[0];
        breedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectGenderSpinner = breedList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initRatBreedSpinner() {
        final String[] breedList = getResources().getStringArray(R.array.breed_rat_list);
        ArrayAdapter<String> breedAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, breedList);

        breedSpinner.setAdapter(breedAdapter);
        selectBreedSpinner = breedList[0];
        breedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectGenderSpinner = breedList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initCatBreedSpinner() {
        final String[] breedList = getResources().getStringArray(R.array.breed_cat_list);
        ArrayAdapter<String> breedAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, breedList);

        breedSpinner.setAdapter(breedAdapter);
        selectBreedSpinner = breedList[0];
        breedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectGenderSpinner = breedList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initDogBreedSpinner() {
        final String[] breedList = getResources().getStringArray(R.array.breed_dog_list);
        ArrayAdapter<String> breedAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, breedList);

        breedSpinner.setAdapter(breedAdapter);
        selectBreedSpinner = breedList[0];
        breedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectGenderSpinner = breedList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initGenderSpinner() {
        final String[] genderList = getResources().getStringArray(R.array.gender_list);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, genderList);
        genderSpinner.setAdapter(genderAdapter);
        selectGenderSpinner = genderList[0];
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectGenderSpinner = genderList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initAgeSpinner() {
        final String[] ageList = getResources().getStringArray(R.array.age_list);
        ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, ageList);
        ageSpinner.setAdapter(ageAdapter);
        selectAgeSpinner = ageList[0];
        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectAgeSpinner = ageList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Post");
        firebaseKey = databaseReference.push().getKey();
    }

    private void initUploadImages() {
        uploadImage1Button = findViewById(R.id.uploadPhoto1Button);
        uploadImage1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Insert Photo"), 2);
            }
        });

        uploadImage2Button = findViewById(R.id.uploadPhoto2Button);
        uploadImage2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Insert Photo"), 3);
            }
        });

        uploadImage3Button = findViewById(R.id.uploadPhoto3Button);
        uploadImage3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Insert Photo"), 4);
            }
        });
    }

    public void clickButton(View view) {
        if (view.getId() == R.id.map_button) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri;
        if (requestCode == 1 && resultCode == this.RESULT_OK) { //from map
            latitude = data.getDoubleExtra("latitude", 0);
            longitude = data.getDoubleExtra("longitude", 0);
            String address = data.getStringExtra("address");
            locationTextView.setText(address);
            Log.i("Back", "address: " + address);
        } else if (requestCode == 2 && resultCode == this.RESULT_OK) { //from photo one
            try {
                uri = data.getData();
                uploadPhoto(uri, "1.jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 3 && resultCode == this.RESULT_OK) { //from photo two
            try {
                uri = data.getData();
                uploadPhoto(uri, "2.jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 4 && resultCode == this.RESULT_OK) { //from photo three
            try {
                uri = data.getData();
                uploadPhoto(uri, "3.jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadPhoto(Uri uri, String fileName) {
        final String fileImageName = fileName;
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference storageReference = firebaseStorage.getReference().child("Post/" + firebaseKey + "/" + fileImageName);
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (fileImageName.equals("1.jpg")) {
                            uploadImage1URL = uri.toString();
                            downloadPhoto(uploadImage1URL, fileImageName);
                        } else if (fileImageName.equals("2.jpg")) {
                            uploadImage2URL = uri.toString();
                            downloadPhoto(uploadImage2URL, fileImageName);
                        } else if (fileImageName.equals("3.jpg")) {
                            uploadImage3URL = uri.toString();
                            downloadPhoto(uploadImage3URL, fileImageName);
                        }

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void downloadPhoto(String url, String fileName) {
        if (fileName.equals("1.jpg")) {
            ImageView imageView = findViewById(R.id.uploadPhoto1Button);
            Glide.with(this).load(url).into(imageView);
        } else if (fileName.equals("2.jpg")) {
            ImageView imageView = findViewById(R.id.uploadPhoto2Button);
            Glide.with(this).load(url).into(imageView);
        } else if (fileName.equals("3.jpg")) {
            ImageView imageView = findViewById(R.id.uploadPhoto3Button);
            Glide.with(this).load(url).into(imageView);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.post_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.post_done) {
            if (!topicEditText.getText().toString().equals("")) {
                Animal animal = new Animal(UserManager.getUser(), true, "["+ typeSend + "]"+ topicEditText.getText().toString(),selectAnimalTypeSpinner, selectBreedSpinner, selectGenderSpinner, selectAgeSpinner, descriptionEditText.getText().toString(), latitude, longitude, uploadImage1URL, uploadImage2URL, uploadImage3URL);
                databaseReference.child(typeSend).child(firebaseKey).setValue(animal);
                Toast.makeText(CreatePostActivity.this, "Post Created", Toast.LENGTH_LONG).show();
            }
        }
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return true;
    }
}

//                uri = data.getData();
//                Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri));
//                imageView.setImageBitmap(bitmap);