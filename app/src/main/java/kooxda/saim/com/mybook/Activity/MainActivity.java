package kooxda.saim.com.mybook.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import kooxda.saim.com.mybook.R;

public class MainActivity extends AppCompatActivity {

    public static Toolbar toolbar;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    android.support.v7.app.ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeMainActivity);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Home");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            Toast.makeText(getApplicationContext(), "Drawer Open", Toast.LENGTH_LONG).show();
        }
    }


    public void NavigationItemClicked() {
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.btbMenuHome) {

                    drawerLayout.closeDrawers();
                } else if (item.getItemId() == R.id.btbMenuProfile) {

                    drawerLayout.closeDrawers();
                } else if (item.getItemId() == R.id.btbMenuCategory) {

                    drawerLayout.closeDrawers();
                } else if (item.getItemId() == R.id.btbMenuVideo) {

                    drawerLayout.closeDrawers();
                } else if (item.getItemId() == R.id.btbMenuAudio) {

                    drawerLayout.closeDrawers();
                }else if (item.getItemId() == R.id.btbMenuExit) {

                    drawerLayout.closeDrawers();
                }
                return false;
            }
        });
    }
}
