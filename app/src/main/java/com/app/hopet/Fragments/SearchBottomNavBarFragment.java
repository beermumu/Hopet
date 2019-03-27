package com.app.hopet.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;


import com.app.hopet.Models.Animal;
import com.app.hopet.R;
import com.app.hopet.Utilities.CustomListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class SearchBottomNavBarFragment extends Fragment {
    private SearchView searchView;
    private Spinner searchFunctionSpinner, searchSpinner, searchBreedSpinner;
    private String searchFunction = "None";
    private String animal = "None";
    private String searchText = "";
    private String selectFunction, selectSearch, selectBreed;
    private FirebaseDatabase database;
    private CustomListView customListView;
    private ArrayList<Animal> animals;
    private ArrayList<String> key;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_search, container, false);
        searchFunctionSpinner = view.findViewById(R.id.searchFunctionSpinner);
        searchSpinner = view.findViewById(R.id.searchSpinner);
        searchBreedSpinner = view.findViewById(R.id.searchBreedSpinner);
        searchView = view.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Here");

        initFunctionSpinner();


        animals = new ArrayList<>();
        key = new ArrayList<>();
        customListView = new CustomListView(getContext(), 0, animals, key);
        database = FirebaseDatabase.getInstance();
        initClickSearch();
        listView = view.findViewById(R.id.search_listview);
        listView.setAdapter(customListView);


        return view;

    }

    private void initFunctionSpinner() {
        final String[] functionList = getResources().getStringArray(R.array.function_list);
        final ArrayAdapter<String> animalAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, functionList);

        searchFunctionSpinner.setAdapter(animalAdapter);
        selectFunction = functionList[0];
        searchFunctionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectFunction = functionList[position];
                initFunctionCheckSpinner();
                initSearchData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initFunctionCheckSpinner() {
        searchBreedSpinner.setEnabled(false);
        searchSpinner.setEnabled(true);
        if (selectFunction.equals("TopicType")) {
            initTopicTypeSpinner();
            initNoneBreedSpinner();
        } else if (selectFunction.equals("PetType")) {
            searchBreedSpinner.setEnabled(true);
            initPetTypeSpinner();
            initNoneBreedSpinner();
        } else if (selectFunction.equals("Gender")) {
            initGenderSpinner();
            initNoneBreedSpinner();
        } else if (selectFunction.equals("Age")) {
            initAgeSpinner();
            initNoneBreedSpinner();
        } else if (selectFunction.equals("None")) {
            initNoneSearchSpinner();
            initNoneBreedSpinner();
            searchSpinner.setEnabled(false);
        }
    }

    private void initTopicTypeSpinner() {
        final String[] topicList = getResources().getStringArray(R.array.topic_type_list);
        final ArrayAdapter<String> animalAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, topicList);

        searchSpinner.setAdapter(animalAdapter);
        selectSearch = topicList[0];
        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectSearch = topicList[position];
                initSearchData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initPetTypeSpinner() {
        final String[] animalList = getResources().getStringArray(R.array.animal_type_list);
        final ArrayAdapter<String> animalAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, animalList);

        searchSpinner.setAdapter(animalAdapter);
        selectSearch = animalList[0];
        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectSearch = animalList[position];
                animal = animalList[position];
                selectBreed = "None";
                initBreedSpinner();
                initSearchData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void initBreedSpinner() {
        initNoneBreedSpinner();
        searchBreedSpinner.setEnabled(true);
        if (animal.equals("Dog")) {
            initDogBreedSpinner();
        } else if (animal.equals("Cat")) {
            initCatBreedSpinner();
        } else if (animal.equals("Bird")) {
            initBirdBreedSpinner();
        } else if (animal.equals("Rat")) {
            initRatBreedSpinner();
        } else if (animal.equals("Other")) {
            searchBreedSpinner.setEnabled(false);
            initNoneBreedSpinner();
        } else if (animal.equals("None")) {
            initNoneSearchSpinner();
            initNoneBreedSpinner();
        }
    }

    private void initNoneSearchSpinner() {
        String[] searchList = new String[1];
        searchList[0] = "None";
        ArrayAdapter<String> breedAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, searchList);

        searchSpinner.setAdapter(breedAdapter);
        selectSearch = "None";
        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectSearch = "None";
                initSearchData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initNoneBreedSpinner() {
        String[] breedList = new String[1];
        breedList[0] = "None";
        ArrayAdapter<String> breedAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, breedList);

        searchBreedSpinner.setAdapter(breedAdapter);
        selectBreed = "None";
        searchBreedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectBreed = "None";
                initSearchData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initBirdBreedSpinner() {
        final String[] breedList = getResources().getStringArray(R.array.search_breed_bird_list);
        ArrayAdapter<String> breedAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, breedList);

        searchBreedSpinner.setAdapter(breedAdapter);
        selectBreed = breedList[0];
        searchBreedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectBreed = breedList[position];
                initSearchData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initRatBreedSpinner() {
        final String[] breedList = getResources().getStringArray(R.array.search_breed_rat_list);
        ArrayAdapter<String> breedAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, breedList);

        searchBreedSpinner.setAdapter(breedAdapter);
        selectBreed = breedList[0];
        searchBreedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectBreed = breedList[position];
                initSearchData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initCatBreedSpinner() {
        final String[] breedList = getResources().getStringArray(R.array.search_breed_cat_list);
        ArrayAdapter<String> breedAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, breedList);

        searchBreedSpinner.setAdapter(breedAdapter);
        selectBreed = breedList[0];
        searchBreedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectBreed = breedList[position];
                initSearchData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initDogBreedSpinner() {
        final String[] breedList = getResources().getStringArray(R.array.search_breed_dog_list);
        ArrayAdapter<String> breedAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, breedList);

        searchBreedSpinner.setAdapter(breedAdapter);
        selectBreed = breedList[0];
        searchBreedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectBreed = breedList[position];
                initSearchData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initGenderSpinner() {
        final String[] genderList = getResources().getStringArray(R.array.gender_list);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, genderList);
        searchSpinner.setAdapter(genderAdapter);
        selectSearch = genderList[0];
        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectSearch = genderList[position];
                initSearchData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initAgeSpinner() {
        final String[] ageList = getResources().getStringArray(R.array.age_list);
        ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, ageList);
        searchSpinner.setAdapter(ageAdapter);
        selectSearch = ageList[0];
        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectSearch = ageList[position];
                initSearchData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initClickSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchText = query;
                Log.i("kikapu", searchText);
                initSearchData();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText;
                initSearchData();
                return false;
            }

        });
    }


    private void initSearchData() {
        final DatabaseReference databaseReference = database.getReference().child("Post").child("Data");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                animals.clear();
                key.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Animal addAnimal = dataSnapshot1.getValue(Animal.class);
                    if(addAnimal.getTopic().toLowerCase().contains(searchText.toLowerCase()) || addAnimal.getType().toLowerCase().contains(searchText.toLowerCase()) || addAnimal.getBreed().toLowerCase().contains(searchText.toLowerCase())|| addAnimal.getGender().toLowerCase().equals(searchText.toLowerCase())){

                        if (!selectFunction.equals("None")){
                            if (selectFunction.equals("TopicType")){
                                if (addAnimal.getTopicType().equals(selectSearch)) {
                                    if (addAnimal.isStatus()){
                                        animals.add(addAnimal);
                                        key.add(dataSnapshot1.getKey());
                                    }
                                }
                            }if (selectFunction.equals("Gender")){
                                if (addAnimal.getGender().equals(selectSearch)) {
                                    if (addAnimal.isStatus()){
                                        animals.add(addAnimal);
                                        key.add(dataSnapshot1.getKey());
                                    }
                                }
                            }if (selectFunction.equals("Age")){
                                if (addAnimal.getAge().equals(selectSearch)) {
                                    if (addAnimal.isStatus()){
                                        animals.add(addAnimal);
                                        key.add(dataSnapshot1.getKey());
                                    }
                                }
                            }if (selectFunction.equals("PetType")){
                                if (addAnimal.getType().equals(selectSearch)) {
                                    if(!selectBreed.equals("None")){
                                        if (addAnimal.getBreed().equals(selectBreed)){
                                            if (addAnimal.isStatus()){
                                                animals.add(addAnimal);
                                                key.add(dataSnapshot1.getKey());
                                            }
                                        }
                                    }else {
                                        if (addAnimal.isStatus()){
                                            animals.add(addAnimal);
                                            key.add(dataSnapshot1.getKey());
                                        }
                                    }

                                }
                            }
                        }else {
                            if (addAnimal.isStatus()){
                                animals.add(addAnimal);
                                key.add(dataSnapshot1.getKey());
                            }
                        }
                    }
                    Collections.reverse(animals);
                    Collections.reverse(key);
                    customListView.notifyDataSetChanged();
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
