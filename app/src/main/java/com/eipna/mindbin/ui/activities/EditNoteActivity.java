package com.eipna.mindbin.ui.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.DatePattern;
import com.eipna.mindbin.data.folder.Folder;
import com.eipna.mindbin.data.folder.FolderRepository;
import com.eipna.mindbin.data.note.Note;
import com.eipna.mindbin.data.note.NoteRepository;
import com.eipna.mindbin.data.note.NoteState;
import com.eipna.mindbin.databinding.ActivityEditNoteBinding;
import com.eipna.mindbin.util.DateUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Objects;

public class EditNoteActivity extends BaseActivity {

    private ActivityEditNoteBinding binding;
    private NoteRepository noteRepository;
    private FolderRepository folderRepository;
    private ArrayList<Folder> folders;
    private Note noteExtra;
    private String[] folderNames;
    private int selectedFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityEditNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        noteExtra = getIntent().getParcelableExtra("selected_note");
        noteRepository = new NoteRepository(this);

        folderRepository = new FolderRepository(this);
        folders = new ArrayList<>(folderRepository.get());
        folderNames = folderRepository.getNames();
        selectedFolder = getSelectedFolderIndex();

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.folder.setVisibility(selectedFolder == 0 ? View.GONE : View.VISIBLE);
        binding.folder.setText(folderNames[selectedFolder]);

        if (noteExtra != null) {
            binding.titleInput.setText(noteExtra.getTitle());
            binding.contentInput.setText(noteExtra.getContent());
            binding.currentDate.setText(DateUtil.getString(DatePattern.LONG_DAY_NAME.PATTERN, noteExtra.getDateCreated()));
        }
    }

    private int getSelectedFolderIndex() {
        for (int i = 0; i < folders.size(); i++) {
            if (folders.get(i).getUUID().equals(noteExtra.getFolderUUID())) {
                return (i + 1);
            }
        }
        return 0;
    }

    private void updateNote() {
        String title = Objects.requireNonNull(binding.titleInput.getText()).toString();
        String content = Objects.requireNonNull(binding.contentInput.getText()).toString();

        Note editedNote = new Note();
        editedNote.setUUID(noteExtra.getUUID());
        editedNote.setTitle(title);
        editedNote.setContent(content);
        editedNote.setState(noteExtra.getState());
        editedNote.setFolderUUID((selectedFolder == 0) ? Note.NO_FOLDER : folderRepository.getID(folderNames[selectedFolder]));

        noteRepository.edit(editedNote);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_note, menu);

        if (noteExtra.getState() == NoteState.NORMAL.value) {
            menu.findItem(R.id.unarchive).setVisible(false);
            menu.findItem(R.id.restore).setVisible(false);
            menu.findItem(R.id.delete_forever).setVisible(false);
        } else if (noteExtra.getState() == NoteState.ARCHIVE.value) {
            menu.findItem(R.id.archive).setVisible(false);
            menu.findItem(R.id.restore).setVisible(false);
            menu.findItem(R.id.delete_forever).setVisible(false);
        } else if (noteExtra.getState() == NoteState.TRASH.value) {
            menu.findItem(R.id.trash).setVisible(false);
            menu.findItem(R.id.unarchive).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        if (item.getItemId() == R.id.save) updateNote();
        if (item.getItemId() == R.id.share) showShareIntent();

        if (item.getItemId() == R.id.unarchive || item.getItemId() == R.id.restore) updateNoteState(NoteState.NORMAL);
        if (item.getItemId() == R.id.archive) updateNoteState(NoteState.ARCHIVE);
        if (item.getItemId() == R.id.trash) updateNoteState(NoteState.TRASH);

        if (item.getItemId() == R.id.delete_forever) showDeleteDialog();
        if (item.getItemId() == R.id.folder) showSelectFolderDialog();
        return true;
    }

    private void updateNoteState(NoteState updatedState) {
        noteRepository.updateState(noteExtra.getUUID(), updatedState.value);
        setResult(RESULT_OK);
        finish();
    }

    private void showDeleteDialog() {
        @SuppressLint("UseCompatLoadingForDrawables")
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialog_note_delete_title)
                .setMessage(R.string.dialog_note_delete_message)
                .setIcon(getResources().getDrawable(R.drawable.ic_warning_filled, getTheme()))
                .setNegativeButton(R.string.dialog_button_close, null)
                .setPositiveButton(R.string.dialog_button_delete, (dialogInterface, i) -> {
                    noteRepository.delete(noteExtra.getUUID());
                    setResult(RESULT_OK);
                    finish();
                });

        Dialog deleteDialog = builder.create();
        deleteDialog.show();
    }

    private void showShareIntent() {
        String content = Objects.requireNonNull(binding.contentInput.getText()).toString();
        if (content.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_decline_share), Toast.LENGTH_SHORT).show();
        } else {
            Intent sendIntent = new Intent();
            sendIntent.putExtra(Intent.EXTRA_TEXT, content);
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
}