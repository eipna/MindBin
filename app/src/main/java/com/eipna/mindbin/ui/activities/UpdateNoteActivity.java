package com.eipna.mindbin.ui.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.MindBinDatabase;
import com.eipna.mindbin.databinding.ActivityUpdateNoteBinding;
import com.eipna.mindbin.util.SharedPreferenceUtil;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.shape.MaterialShapeDrawable;

import java.util.Objects;

public class UpdateNoteActivity extends AppCompatActivity {

    private ActivityUpdateNoteBinding binding;

    private int noteIDExtra;
    private String noteTitleExtra;
    private String noteContentExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferenceUtil sharedPreferenceUtil = new SharedPreferenceUtil(this);
        boolean isDynamicColorsAvailable = sharedPreferenceUtil.getBoolean("dynamic_colors", false);
        if (isDynamicColorsAvailable) DynamicColors.applyToActivityIfAvailable(this);

        EdgeToEdge.enable(this);
        binding = ActivityUpdateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Drawable drawable = MaterialShapeDrawable.createWithElevationOverlay(this);
        binding.appBar.setStatusBarForeground(drawable);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        noteIDExtra = getIntent().getIntExtra(MindBinDatabase.COLUMN_NOTE_ID, -1);
        noteTitleExtra = getIntent().getStringExtra(MindBinDatabase.COLUMN_NOTE_TITLE);
        noteContentExtra = getIntent().getStringExtra(MindBinDatabase.COLUMN_NOTE_CONTENT);

        binding.titleInput.setText(noteTitleExtra);
        binding.contentInput.setText(noteContentExtra);
    }

    private void updateNote() {
        String noteTitleInput = Objects.requireNonNull(binding.titleInput.getText()).toString();
        String noteContentInput = Objects.requireNonNull(binding.contentInput.getText()).toString();

        if (!noteTitleInput.equals(noteTitleExtra) || !noteContentInput.equals(noteContentExtra)) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(MindBinDatabase.COLUMN_NOTE_ID, noteIDExtra);
            resultIntent.putExtra(MindBinDatabase.COLUMN_NOTE_TITLE, noteTitleInput);
            resultIntent.putExtra(MindBinDatabase.COLUMN_NOTE_CONTENT, noteContentInput);
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.update_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) updateNote();
        if (item.getItemId() == R.id.share) showShareIntent();
        return true;
    }

    private void showShareIntent() {
        String noteContent = Objects.requireNonNull(binding.contentInput.getText()).toString();
        if (noteContent.isEmpty()) {
            Toast.makeText(this, "Nothing to share here", Toast.LENGTH_SHORT).show();
        } else {
            Intent sendIntent = new Intent();
            sendIntent.putExtra(Intent.EXTRA_TEXT, noteContent);
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}