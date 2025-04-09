package com.eipna.mindbin.ui.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

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
}