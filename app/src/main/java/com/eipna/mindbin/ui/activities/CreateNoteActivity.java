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
import com.eipna.mindbin.data.MindBinDatabase;
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

        binding.contentInput.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(binding.contentInput, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
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

        Intent resultIntent = new Intent();
        resultIntent.putExtra(MindBinDatabase.COLUMN_NOTE_TITLE, title);
        resultIntent.putExtra(MindBinDatabase.COLUMN_NOTE_CONTENT, content);
        resultIntent.putExtra(MindBinDatabase.COLUMN_NOTE_STATE, NoteState.NORMAL.value);
        resultIntent.putExtra(MindBinDatabase.COLUMN_NOTE_DATE_CREATED, System.currentTimeMillis());
        resultIntent.putExtra(MindBinDatabase.COLUMN_NOTE_LAST_UPDATED, System.currentTimeMillis());
        setResult(RESULT_OK, resultIntent);
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