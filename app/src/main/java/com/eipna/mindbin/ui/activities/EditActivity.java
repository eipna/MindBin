package com.eipna.mindbin.ui.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.DatePattern;
import com.eipna.mindbin.data.note.Note;
import com.eipna.mindbin.data.note.NoteRepository;
import com.eipna.mindbin.data.note.NoteState;
import com.eipna.mindbin.databinding.ActivityEditBinding;
import com.eipna.mindbin.util.DateUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class EditActivity extends BaseActivity {

    private ActivityEditBinding binding;
    private NoteRepository noteRepository;
    private Note noteExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        noteExtra = getIntent().getParcelableExtra("selected_note");
        noteRepository = new NoteRepository(this);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (noteExtra != null) {
            binding.titleInput.setText(noteExtra.getTitle());
            binding.contentInput.setText(noteExtra.getContent());
            binding.currentDate.setText(DateUtil.getString(DatePattern.LONG_DAY_NAME.value, noteExtra.getDateCreated()));
        }
    }

    private void updateNote() {
        String title = Objects.requireNonNull(binding.titleInput.getText()).toString();
        String content = Objects.requireNonNull(binding.contentInput.getText()).toString();

        if (!title.equals(noteExtra.getTitle()) || !content.equals(noteExtra.getContent())) {
            Note editedNote = new Note();
            editedNote.setID(noteExtra.getID());
            editedNote.setTitle(title);
            editedNote.setContent(content);
            editedNote.setState(noteExtra.getState());

            noteRepository.edit(editedNote);
            setResult(RESULT_OK);
            finish();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_update, menu);

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
        if (item.getItemId() == android.R.id.home) return super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.save) updateNote();
        if (item.getItemId() == R.id.share) showShareIntent();

        if (item.getItemId() == R.id.unarchive || item.getItemId() == R.id.restore) updateNoteState(NoteState.NORMAL);
        if (item.getItemId() == R.id.archive) updateNoteState(NoteState.ARCHIVE);
        if (item.getItemId() == R.id.trash) updateNoteState(NoteState.TRASH);

        if (item.getItemId() == R.id.delete_forever) showDeleteDialog();
        return true;
    }

    private void updateNoteState(NoteState updatedState) {
        noteRepository.updateState(noteExtra.getID(), updatedState.value);
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
                    noteRepository.delete(noteExtra.getID());
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
}