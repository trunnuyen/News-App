package com.trng.znews.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.trng.znews.Adapters.CategoryFragmentPagerAdapter;
import com.trng.znews.R;
import com.trng.znews.Utils.Constants;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        viewPager = findViewById(R.id.viewpager);

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        NavigationView navigationView = findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        onNavigationItemSelected(navigationView.getMenu().getItem(0).setChecked(true));

        CategoryFragmentPagerAdapter pagerAdapter =
                new CategoryFragmentPagerAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);

        navigationView.setItemIconTintList(null);
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
            case R.id.nav_saved:
                viewPager.setCurrentItem(Constants.SAVED);
                break;
            case R.id.nav_weather:
                Intent weatherIntent = new Intent(this, WeatherActivity.class);
                startActivity(weatherIntent);
                break;
            case R.id.nav_user:
                Intent userIntent = new Intent(this, AccountActivity.class);
                startActivity(userIntent);

        }
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
}