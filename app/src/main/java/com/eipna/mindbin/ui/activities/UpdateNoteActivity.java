package com.eipna.mindbin.ui.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.eipna.mindbin.data.Database;
import com.eipna.mindbin.databinding.ActivityUpdateNoteBinding;
import com.google.android.material.shape.MaterialShapeDrawable;

public class UpdateNoteActivity extends AppCompatActivity {

    private ActivityUpdateNoteBinding binding;

    private int selectedNoteID;
    private String selectedNoteTitle;
    private String selectedNoteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUpdateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Drawable drawable = MaterialShapeDrawable.createWithElevationOverlay(this);
        binding.appBar.setStatusBarForeground(drawable);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        selectedNoteID = getIntent().getIntExtra(Database.COLUMN_NOTE_ID, -1);
        selectedNoteTitle = getIntent().getStringExtra(Database.COLUMN_NOTE_TITLE);
        selectedNoteContent = getIntent().getStringExtra(Database.COLUMN_NOTE_CONTENT);

        binding.titleInput.setText(selectedNoteTitle);
        binding.contentInput.setText(selectedNoteContent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}