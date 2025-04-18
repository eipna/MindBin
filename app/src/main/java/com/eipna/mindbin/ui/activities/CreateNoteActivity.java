package com.eipna.mindbin.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.DatePattern;
import com.eipna.mindbin.data.note.Note;
import com.eipna.mindbin.data.note.NoteRepository;
import com.eipna.mindbin.data.note.NoteState;
import com.eipna.mindbin.databinding.ActivityCreateNoteBinding;
import com.eipna.mindbin.util.DateUtil;

import java.util.Objects;
import java.util.UUID;

public class CreateNoteActivity extends BaseActivity {

    private ActivityCreateNoteBinding binding;
    private NoteRepository noteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCreateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        noteRepository = new NoteRepository(this);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (Intent.ACTION_SEND.equals(getIntent().getAction()) && getIntent().getType() != null) {
            if (getIntent().getType().equals("text/plain")) {
                handleSharedText(getIntent());
            }
        }

        binding.currentDate.setText(DateUtil.getString(DatePattern.LONG_DAY_NAME.PATTERN, System.currentTimeMillis()));

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
        if (item.getItemId() == android.R.id.home) return super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.save) createNewNote();
        return true;
    }

    private void createNewNote() {
        String title = Objects.requireNonNull(binding.titleInput.getText()).toString();
        String content = Objects.requireNonNull(binding.contentInput.getText()).toString();

        Note createdNote = new Note();
        createdNote.setUUID(UUID.randomUUID().toString());
        createdNote.setTitle(title);
        createdNote.setContent(content);
        createdNote.setState(NoteState.NORMAL.value);
        createdNote.setDateCreated(System.currentTimeMillis());
        createdNote.setFolderUUID(Note.NO_FOLDER);

        noteRepository.create(createdNote);
        if (Intent.ACTION_SEND.equals(getIntent().getAction()) && getIntent().getType() != null) {
            startActivity(new Intent(CreateNoteActivity.this, MainActivity.class));
            finish();
        } else {
            setResult(RESULT_OK);
            finish();
        }
    }
}