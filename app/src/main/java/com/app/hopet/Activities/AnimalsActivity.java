package com.app.hopet.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.hopet.R;

public class AnimalsActivity extends AppCompatActivity {
    private String typeSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Choose your animal");
        setContentView(R.layout.activity_animals);
        initIntent();
    }

    private void initIntent() {
        Intent intent = getIntent();
        String send = intent.getStringExtra("sent");
        if (send.equals("take")) {
            typeSend = "take";
        } else if (send.equals("give")) {
            typeSend = "give";
        }
    }

    public void clickButton(View view) {
        Intent intent = new Intent(getApplicationContext(), CreatePostActivity.class);
        if (view.getId() == R.id.dog_button) {
            intent.putExtra("sent", typeSend+"Dog");
            startActivityForResult(intent,0);
        }
        if (view.getId() == R.id.cat_button) {
            intent.putExtra("sent", typeSend+"Cat");
            startActivityForResult(intent,0);
        }
    }



}