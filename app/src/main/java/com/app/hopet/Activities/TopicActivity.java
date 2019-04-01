package com.app.hopet.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.app.hopet.R;
import com.app.hopet.Utilities.DateTime;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class TopicActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private String firebaseKey;

    private EditText topicEditText, descriptionEditText;
    private TextView locationTextView;
    private Spinner genderSpinner, breedSpinner, ageSpinner, animalTypeSpinner;
    private ImageView uploadImage1Button, uploadImage2Button, uploadImage3Button;


    private String selectGenderSpinner, selectBreedSpinner, selectAgeSpinner, selectAnimalTypeSpinner ,typeSend,selectDesc,selectTopic;
    private double latitude, longitude;
    private String uploadImage1URL, uploadImage2URL, uploadImage3URL;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(TopicActivity.this.getString(R.string.edit));
        Intent intent = getIntent();
        firebaseKey = intent.getStringExtra("key");
        setContentView(R.layout.activity_create_post);

        initWidgets();
        getBaseData();

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

    private void getBaseData() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Post").child("Data");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.getKey().equals(firebaseKey)) {
                        Animal animal = dataSnapshot1.getValue(Animal.class);
                        topicEditText.setText(animal.getTopic());
                        selectTopic = topicEditText.getText().toString();
                        selectAnimalTypeSpinner = animal.getType();
                        selectBreedSpinner = animal.getBreed();
                        selectGenderSpinner = animal.getGender();
                        selectAgeSpinner = animal.getAge();
                        descriptionEditText.setText(animal.getDescription());
                        selectDesc = descriptionEditText.getText().toString();
                        latitude = animal.getLatitude();
                        longitude = animal.getLongitude();
                        typeSend = animal.getTopicType();
                        if (latitude!=0 && longitude!=0){
                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(TopicActivity.this, Locale.getDefault());
                            try {
                                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                locationTextView.setText(addresses.get(0).getAddressLine(0)+addresses.get(0).getLocality()+addresses.get(0).getAdminArea()+addresses.get(0).getCountryName()+addresses.get(0).getPostalCode()+addresses.get(0).getPostalCode());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        Glide.with(getApplicationContext()).load(animal.getPhotoOne()).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.not_found_image)).into((ImageView) findViewById(R.id.uploadPhoto1Button));
                        Glide.with(getApplicationContext()).load(animal.getPhotoTwo()).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.not_found_image)).into((ImageView) findViewById(R.id.uploadPhoto2Button));
                        Glide.with(getApplicationContext()).load(animal.getPhotoThree()).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.not_found_image)).into((ImageView) findViewById(R.id.uploadPhoto3Button));

                        uploadImage1URL = animal.getPhotoOne();
                        uploadImage2URL = animal.getPhotoTwo();
                        uploadImage3URL = animal.getPhotoThree();

                        initAnimalTypeSpinner();
                        initGenderSpinner();
                        initAgeSpinner();
                        initUploadImages();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private int getIndexList(String search, String[] list) {
        int i;
        for (i = 0; i < list.length; i++) {
            if (search.equals(list[i])) {
                return i;
            }
        }
        return -1;
    }

    private void initAnimalTypeSpinner() {
        final String[] animalList = getResources().getStringArray(R.array.animal_type_list);
        final ArrayAdapter<String> animalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, animalList);


        animalTypeSpinner.setAdapter(animalAdapter);
        animalTypeSpinner.setSelection(getIndexList(selectAnimalTypeSpinner,animalList));
        animalTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectAnimalTypeSpinner = animalList[position];
                initBreedSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initBreedSpinner() {
        breedSpinner.setEnabled(true);
        if (selectAnimalTypeSpinner.equals("Dog")) {
            initDogBreedSpinner();
        } else if (selectAnimalTypeSpinner.equals("Cat")) {
            initCatBreedSpinner();
        } else if (selectAnimalTypeSpinner.equals("Bird")) {
            initBirdBreedSpinner();
        } else if (selectAnimalTypeSpinner.equals("Rat")) {
            initRatBreedSpinner();
        } else if (selectAnimalTypeSpinner.equals("Other")) {
            breedSpinner.setEnabled(false);
            selectBreedSpinner = "-";
        }
    }

    private void initBirdBreedSpinner() {
        final String[] breedList = getResources().getStringArray(R.array.breed_bird_list);
        ArrayAdapter<String> breedAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, breedList);

        breedSpinner.setAdapter(breedAdapter);
        breedSpinner.setSelection(getIndexList(selectBreedSpinner,breedList));
        breedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectBreedSpinner = breedList[position];
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
        breedSpinner.setSelection(getIndexList(selectBreedSpinner,breedList));
        breedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectBreedSpinner = breedList[position];
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
        breedSpinner.setSelection(getIndexList(selectBreedSpinner,breedList));
        breedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectBreedSpinner = breedList[position];
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
        breedSpinner.setSelection(getIndexList(selectBreedSpinner,breedList));
        breedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectBreedSpinner = breedList[position];
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
        genderSpinner.setSelection(getIndexList(selectGenderSpinner,genderList));
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
        ageSpinner.setSelection(getIndexList(selectAgeSpinner,ageList));
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

    private void initUploadImages() {
        uploadImage1Button = findViewById(R.id.uploadPhoto1Button);
        uploadImage1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(2);
            }
        });

        uploadImage2Button = findViewById(R.id.uploadPhoto2Button);
        uploadImage2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(3);
            }
        });

        uploadImage3Button = findViewById(R.id.uploadPhoto3Button);
        uploadImage3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(4);
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
            Glide.with(this).load(url).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.not_found_image)).into(imageView);
        } else if (fileName.equals("2.jpg")) {
            ImageView imageView = findViewById(R.id.uploadPhoto2Button);
            Glide.with(this).load(url).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.not_found_image)).into(imageView);
        } else if (fileName.equals("3.jpg")) {
            ImageView imageView = findViewById(R.id.uploadPhoto3Button);
            Glide.with(this).load(url).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.not_found_image)).into(imageView);
        }

    }

    public void changeStatus(String firebaseKey){
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Post").child("Data");
        databaseReference.child(firebaseKey).child("status").setValue(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_done) {
            if (!topicEditText.getText().toString().equals("")) {
                databaseReference.child(firebaseKey).child("age").setValue(selectAgeSpinner);
                databaseReference.child(firebaseKey).child("breed").setValue(selectBreedSpinner);
                databaseReference.child(firebaseKey).child("dateTime").setValue(DateTime.getDate());
                databaseReference.child(firebaseKey).child("gender").setValue(selectGenderSpinner);
                databaseReference.child(firebaseKey).child("type").setValue(selectAnimalTypeSpinner);
                databaseReference.child(firebaseKey).child("latitude").setValue(latitude);
                databaseReference.child(firebaseKey).child("longitude").setValue(longitude);
                databaseReference.child(firebaseKey).child("status").setValue(true);
                databaseReference.child(firebaseKey).child("description").setValue(descriptionEditText.getText().toString());
                databaseReference.child(firebaseKey).child("topic").setValue("["+ typeSend + "]"+ topicEditText.getText().toString());

                databaseReference.child(firebaseKey).child("photoOne").setValue(uploadImage1URL);
                databaseReference.child(firebaseKey).child("photoTwo").setValue(uploadImage2URL);
                databaseReference.child(firebaseKey).child("photoThree").setValue(uploadImage3URL);

                Toast.makeText(TopicActivity.this, TopicActivity.this.getString(R.string.edit_success), Toast.LENGTH_LONG).show();
            }
        }
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return true;
    }

    private void selectImage(int rc){
        final CharSequence[] item = {TopicActivity.this.getString(R.string.choose_lib) , TopicActivity.this.getString(R.string.delete)};

        AlertDialog.Builder builder = new AlertDialog.Builder(TopicActivity.this);
        builder.setTitle(TopicActivity.this.getString(R.string.edit_photo));
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (item[which].equals(TopicActivity.this.getString(R.string.choose_lib))){
                    galleryIntent(rc);
                }else if (item[which].equals(TopicActivity.this.getString(R.string.delete))){
                    if (rc == 2){
                        databaseReference.child(firebaseKey).child("photoOne").setValue("");
                    }else if (rc == 3){
                        databaseReference.child(firebaseKey).child("photoTwo").setValue("");
                    }else if (rc == 4){
                        databaseReference.child(firebaseKey).child("photoThree").setValue("");
                    }
                }
            }
        });
        builder.show();
    }
    private void galleryIntent(int rc){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Insert Photo"), rc);
    }

}

