package com.eipna.mindbin.ui.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.note.Note;
import com.eipna.mindbin.data.note.NoteState;
import com.eipna.mindbin.databinding.ActivityUpdateNoteBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.shape.MaterialShapeDrawable;

import java.util.Objects;

public class UpdateNoteActivity extends BaseActivity {

    private ActivityUpdateNoteBinding binding;
    private Note noteExtra;

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

        noteExtra = getIntent().getParcelableExtra("selected_note");
        if (noteExtra != null) {
            binding.titleInput.setText(noteExtra.getTitle());
            binding.contentInput.setText(noteExtra.getContent());
        }
    }

    private void updateNote() {
        String noteTitleInput = Objects.requireNonNull(binding.titleInput.getText()).toString();
        String noteContentInput = Objects.requireNonNull(binding.contentInput.getText()).toString();

        if (!noteTitleInput.equals(noteExtra.getTitle()) || !noteContentInput.equals(noteExtra.getContent())) {
            Note updatedNote = new Note();
            updatedNote.setID(noteExtra.getID());
            updatedNote.setTitle(noteTitleInput);
            updatedNote.setContent(noteContentInput);
            updatedNote.setLastUpdated(System.currentTimeMillis());
            updatedNote.setState(noteExtra.getState());

            Intent updateIntent = new Intent();
            updateIntent.putExtra("updated_note", updatedNote);
            setResult(RESULT_OK, updateIntent);
            finish();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_update_note, menu);

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
        if (item.getItemId() == android.R.id.home) updateNote();
        if (item.getItemId() == R.id.share) showShareIntent();

        if (item.getItemId() == R.id.unarchive || item.getItemId() == R.id.restore) updateNoteState(NoteState.NORMAL);
        if (item.getItemId() == R.id.archive) updateNoteState(NoteState.ARCHIVE);
        if (item.getItemId() == R.id.trash) updateNoteState(NoteState.TRASH);

        if (item.getItemId() == R.id.delete_forever) showDeleteDialog();
        return true;
    }

    private void updateNoteState(NoteState updatedState) {
        Note updatedNote = new Note();
        updatedNote.setID(noteExtra.getID());
        updatedNote.setState(updatedState.value);

        Intent updateIntent = new Intent();
        updateIntent.putExtra("updated_note", updatedNote);
        setResult(RESULT_UPDATE_STATE, updateIntent);
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
                    Note deletedNote = new Note();
                    deletedNote.setID(noteExtra.getID());

                    Intent deleteIntent = new Intent();
                    deleteIntent.putExtra("deleted_note", deletedNote);
                    setResult(RESULT_DELETE, deleteIntent);
                    finish();
                });

        Dialog deleteDialog = builder.create();
        deleteDialog.show();
    }

    private void showShareIntent() {
        String noteContent = Objects.requireNonNull(binding.contentInput.getText()).toString();
        if (noteContent.isEmpty() || noteContent.equals(getString(R.string.empty_note_content))) {
            Toast.makeText(this, getString(R.string.toast_decline_share), Toast.LENGTH_SHORT).show();
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