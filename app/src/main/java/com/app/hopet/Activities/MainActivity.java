package com.app.hopet.Activities;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.app.hopet.Fragments.NewsFeedBottomNavBarFragment;
import com.app.hopet.Fragments.PostBottomNavBarFragment;
import com.app.hopet.Fragments.SearchBottomNavBarFragment;
import com.app.hopet.Fragments.SettingsBottomNavBarFragment;
import com.app.hopet.R;
import com.app.hopet.Utilities.BottomNavigationViewHelper;



public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private NewsFeedBottomNavBarFragment newsFeedBottomNavBarFragment;
    private PostBottomNavBarFragment postBottomNavBarFragment;
    private SearchBottomNavBarFragment searchBottomNavBarFragment;
    private SettingsBottomNavBarFragment settingsBottomNavBarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //to Main

        initBottomNavigationView();

    }

    private void initBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.removeNavigationShiftMode(bottomNavigationView);

        newsFeedBottomNavBarFragment = new NewsFeedBottomNavBarFragment();
        postBottomNavBarFragment = new PostBottomNavBarFragment();
        searchBottomNavBarFragment = new SearchBottomNavBarFragment();
        settingsBottomNavBarFragment = new SettingsBottomNavBarFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_main, newsFeedBottomNavBarFragment, newsFeedBottomNavBarFragment.getTag());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navigation_news_feed) {
            openFragment(newsFeedBottomNavBarFragment);
            getSupportActionBar().setTitle("News Feed");
        } else if (id == R.id.navigation_add_topic) {
            openFragment(postBottomNavBarFragment);
            getSupportActionBar().setTitle("Post");
        } else if (id == R.id.navigation_search) {
            openFragment(searchBottomNavBarFragment);
            getSupportActionBar().setTitle("Search");
        }  else if (id == R.id.navigation_settings) {
            openFragment(settingsBottomNavBarFragment);
            getSupportActionBar().setTitle("Settings");
        }
        return true;
    }

    private void openFragment(final Fragment fragment)   {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.activity_main, fragment);
        transaction.commit();
    }
}
