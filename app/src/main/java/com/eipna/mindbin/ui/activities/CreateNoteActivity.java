package com.eipna.mindbin.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.DatePattern;
import com.eipna.mindbin.data.folder.FolderRepository;
import com.eipna.mindbin.data.note.Note;
import com.eipna.mindbin.data.note.NoteRepository;
import com.eipna.mindbin.data.note.NoteState;
import com.eipna.mindbin.databinding.ActivityCreateNoteBinding;
import com.eipna.mindbin.util.DateUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;
import java.util.UUID;

public class CreateNoteActivity extends BaseActivity {

    private ActivityCreateNoteBinding binding;
    private NoteRepository noteRepository;
    private FolderRepository folderRepository;
    private String[] folderNames;
    private int selectedFolder = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCreateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        noteRepository = new NoteRepository(this);
        folderRepository = new FolderRepository(this);
        folderNames = folderRepository.getNames();

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (Intent.ACTION_SEND.equals(getIntent().getAction()) && getIntent().getType() != null) {
            if (getIntent().getType().equals("text/plain")) {
                handleSharedText(getIntent());
            }
        }

        binding.folder.setVisibility(View.GONE);
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
        if (item.getItemId() == R.id.folder) showSelectFolderDialog();
        return true;
    }

    private void showSelectFolderDialog() {
        if (folderNames.length == 1) {
            Toast.makeText(this, "No folders to show", Toast.LENGTH_SHORT).show();
            return;
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.Theme_MindBin_AlertDialog)
                .setTitle("Select folder")
                .setSingleChoiceItems(folderNames, selectedFolder, (dialog, which) -> selectedFolder = which)
                .setNegativeButton("Close", null)
                .setPositiveButton("Select", (dialog, which) -> {
                    if (selectedFolder == 0) {
                        binding.folder.setVisibility(View.GONE);
                    } else {
                        binding.folder.setText(folderNames[selectedFolder]);
                        binding.folder.setVisibility(View.VISIBLE);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
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
        createdNote.setFolderUUID((selectedFolder == 0) ? Note.NO_FOLDER : folderRepository.getID(folderNames[selectedFolder]));

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