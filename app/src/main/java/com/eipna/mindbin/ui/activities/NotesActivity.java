package com.eipna.mindbin.ui.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.Database;
import com.eipna.mindbin.data.folder.Folder;
import com.eipna.mindbin.databinding.ActivityNotesBinding;

public class NotesActivity extends AppCompatActivity {

    private ActivityNotesBinding binding;

    private String UUIDExtra;
    private String nameExtra;
    private String descriptionExtra;
    private int isPinnedExtra;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotesBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        UUIDExtra = getIntent().getStringExtra(Database.COLUMN_FOLDER_ID);
        nameExtra = getIntent().getStringExtra(Database.COLUMN_FOLDER_NAME);
        descriptionExtra = getIntent().getStringExtra(Database.COLUMN_FOLDER_DESCRIPTION);
        isPinnedExtra = getIntent().getIntExtra(Database.COLUMN_FOLDER_PINNED, Folder.NOT_PINNED);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.toolbar.setTitle(nameExtra);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_folder, menu);
        if (isPinnedExtra == Folder.IS_PINNED) {
            menu.findItem(R.id.pin).setIcon(R.drawable.ic_filled_pin);
            menu.findItem(R.id.pin).setTitle(getString(R.string.menu_unpin));
        } else {
            menu.findItem(R.id.pin).setIcon(R.drawable.ic_outlined_pin);
            menu.findItem(R.id.pin).setTitle(getString(R.string.menu_pin));
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isPinnedExtra == Folder.IS_PINNED) {
            menu.findItem(R.id.pin).setIcon(R.drawable.ic_filled_pin);
            menu.findItem(R.id.pin).setTitle(getString(R.string.menu_unpin));
        } else {
            menu.findItem(R.id.pin).setIcon(R.drawable.ic_outlined_pin);
            menu.findItem(R.id.pin).setTitle(getString(R.string.menu_pin));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) return super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.pin) togglePin();
        return true;
    }

    private void togglePin() {
        isPinnedExtra = (isPinnedExtra == Folder.IS_PINNED) ? Folder.NOT_PINNED : Folder.IS_PINNED;
        invalidateOptionsMenu();
    }
}