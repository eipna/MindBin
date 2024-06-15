package com.serbi.mindbin.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.serbi.mindbin.R;
import com.serbi.mindbin.fragments.Archive;
import com.serbi.mindbin.fragments.Notes;
import com.serbi.mindbin.fragments.Settings;
import com.serbi.mindbin.fragments.Trash;

public class Main extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeComponents();
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Notes()).commit();
        navigationView.setCheckedItem(R.id.notes);

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.notes) {
                toolbar.setTitle("Notes");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Notes()).commit();
            }

            if (item.getItemId() == R.id.archive) {
                toolbar.setTitle("Archive");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Archive()).commit();
            }

            if (item.getItemId() == R.id.trash) {
                toolbar.setTitle("Trash");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Trash()).commit();
            }

            if (item.getItemId() == R.id.settings) {
                toolbar.setTitle("Settings");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Settings()).commit();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void initializeComponents() {
        drawerLayout = findViewById(R.id.main);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation_view);

        toggle = new ActionBarDrawerToggle(
                Main.this, drawerLayout, toolbar,
                R.string.open_navigation_view, R.string.close_navigation_view
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_view_menu, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }
}