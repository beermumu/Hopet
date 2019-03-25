package com.app.hopet.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hopet.Activities.LoginActivity;
import com.app.hopet.Activities.MainActivity;
import com.app.hopet.R;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.io.File;

public class SettingsBottomNavBarFragment extends Fragment {
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private TextView nameView, emailView;
    private Button yourTopicBtn, logoutBtn;
    private ImageView profilePic;


    public SettingsBottomNavBarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_settings, container, false);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        nameView = view.findViewById(R.id.settingShowName);
        emailView = view.findViewById(R.id.settingShowEmail);
        yourTopicBtn = view.findViewById(R.id.yourTopicBtn);
        logoutBtn = view.findViewById(R.id.logoutBtn);
        profilePic = view.findViewById(R.id.settingShowProfilePicture);
        initData();
        clickButton(view);

        Bundle parameters = new Bundle();
        parameters.putString("fields", "picture.type(large)");
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                            Log.i("chocobo",url);
                            Glide.with(getContext()).load(url).into(profilePic);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        request.setParameters(parameters);
        request.executeAsync();

        return view;
    }

    private void initData() {
        nameView.setText("User : " + firebaseUser.getDisplayName());
        emailView.setText("E-mail : " + firebaseUser.getEmail());
        Glide.with(getContext()).load(firebaseUser.getPhotoUrl().getPath()).into(profilePic);
    }


    private void clickButton(View view) {
        yourTopicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }


}
