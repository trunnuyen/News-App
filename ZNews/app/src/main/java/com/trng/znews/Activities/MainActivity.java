package com.trng.znews.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.trng.znews.Adapters.CategoryFragmentPagerAdapter;
import com.trng.znews.R;
import com.trng.znews.Utils.Constants;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Find the view pager that will allow the user to swipe between fragments
        viewPager = findViewById(R.id.viewpager);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        // Set gravity for tab bar
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        NavigationView navigationView = findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        // Set the default fragment when starting the app
        onNavigationItemSelected(navigationView.getMenu().getItem(0).setChecked(true));

        // Set category fragment pager adapter
        CategoryFragmentPagerAdapter pagerAdapter =
                new CategoryFragmentPagerAdapter(this, getSupportFragmentManager());
        // Set the pager adapter onto the view pager
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.nav_home:
                viewPager.setCurrentItem(Constants.HOME);
                break;
            case R.id.nav_world:
                viewPager.setCurrentItem(Constants.WORLD);
                break;
            case R.id.nav_science:
                viewPager.setCurrentItem(Constants.SCIENCE);
                break;
            case R.id.nav_business:
                viewPager.setCurrentItem(Constants.BUSINESS);
                break;
            case R.id.nav_sport:
                viewPager.setCurrentItem(Constants.SPORT);
                break;
            case R.id.nav_environment:
                viewPager.setCurrentItem(Constants.ENVIRONMENT);
                break;
            case R.id.nav_society:
                viewPager.setCurrentItem(Constants.SOCIETY);
                break;
            case R.id.nav_fashion:
                viewPager.setCurrentItem(Constants.FASHION);
                break;
            case R.id.nav_culture:
                viewPager.setCurrentItem(Constants.CULTURE);
                break;
            case R.id.nav_search:
                viewPager.setCurrentItem(Constants.SEARCH);
                break;

        }
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
}