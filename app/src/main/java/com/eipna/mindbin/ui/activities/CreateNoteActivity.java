package com.eipna.mindbin.ui.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.DatePattern;
import com.eipna.mindbin.data.note.Note;
import com.eipna.mindbin.data.note.NoteState;
import com.eipna.mindbin.databinding.ActivityCreateNoteBinding;
import com.eipna.mindbin.util.DateUtil;
import com.google.android.material.shape.MaterialShapeDrawable;

import java.util.Objects;

public class CreateNoteActivity extends BaseActivity {

    private ActivityCreateNoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCreateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Drawable drawable = MaterialShapeDrawable.createWithElevationOverlay(this);
        binding.appBar.setStatusBarForeground(drawable);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (Intent.ACTION_SEND.equals(getIntent().getAction()) && getIntent().getType() != null) {
            if (getIntent().getType().equals("text/plain")) {
                handleSharedText(getIntent());
            }
        }

        binding.contentInput.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(binding.contentInput, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void handleSharedText(Intent sharedIntent) {
        String sharedText = sharedIntent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            binding.contentInput.setText(sharedText);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) createNewNote();
        if (item.getItemId() == R.id.share) showShareIntent();
        return true;
    }

    private void createNewNote() {
        String titleFromInput = Objects.requireNonNull(binding.titleInput.getText()).toString();
        String contentFromInput = Objects.requireNonNull(binding.contentInput.getText()).toString();
        String emptyTitlePlaceholder = String.format("Note %s", DateUtil.getStringByPattern(DatePattern.MM_DD_YYYY, System.currentTimeMillis()));

        String title = (titleFromInput.isEmpty()) ? emptyTitlePlaceholder : titleFromInput;
        String content = (contentFromInput.isEmpty()) ? getString(R.string.empty_note_content) : contentFromInput;

        Note createdNote = new Note();
        createdNote.setTitle(title);
        createdNote.setContent(content);
        createdNote.setState(NoteState.NORMAL.value);
        createdNote.setDateCreated(System.currentTimeMillis());
        createdNote.setLastUpdated(System.currentTimeMillis());

        Intent createIntent = new Intent();
        createIntent.putExtra("created_note", createdNote);
        setResult(RESULT_OK, createIntent);
        finish();
    }

    private void showShareIntent() {
        String noteContent = Objects.requireNonNull(binding.contentInput.getText()).toString();
        if (noteContent.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_decline_share), Toast.LENGTH_SHORT).show();
        } else {
           Intent sendIntent = new Intent();
           sendIntent.setAction(Intent.ACTION_SEND);
           sendIntent.putExtra(Intent.EXTRA_TEXT, noteContent);
           sendIntent.setType("text/plain");

           Intent shareIntent = Intent.createChooser(sendIntent, null);
           startActivity(shareIntent);
        }
    }
}