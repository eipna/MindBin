package com.eipna.mindbin.ui.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.eipna.mindbin.data.MindBinDatabase;
import com.eipna.mindbin.data.note.NoteState;
import com.eipna.mindbin.databinding.ActivityUpdateNoteBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.shape.MaterialShapeDrawable;

import java.util.Objects;

public class UpdateNoteActivity extends BaseActivity {

    private ActivityUpdateNoteBinding binding;

    private int noteIDExtra;
    private String noteTitleExtra;
    private String noteContentExtra;
    private long noteDateCreatedExtra;
    private long noteLastUpdatedExtra;
    private int noteStateExtra;

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

        noteIDExtra = getIntent().getIntExtra(MindBinDatabase.COLUMN_NOTE_ID, -1);
        noteTitleExtra = getIntent().getStringExtra(MindBinDatabase.COLUMN_NOTE_TITLE);
        noteContentExtra = getIntent().getStringExtra(MindBinDatabase.COLUMN_NOTE_CONTENT);
        noteDateCreatedExtra = getIntent().getLongExtra(MindBinDatabase.COLUMN_NOTE_DATE_CREATED, -1);
        noteLastUpdatedExtra = getIntent().getLongExtra(MindBinDatabase.COLUMN_NOTE_LAST_UPDATED, -1);
        noteStateExtra = getIntent().getIntExtra(MindBinDatabase.COLUMN_NOTE_STATE, -2);

        binding.titleInput.setText(noteTitleExtra);
        binding.contentInput.setText(noteContentExtra);
    }

    private void updateNote() {
        String noteTitleInput = Objects.requireNonNull(binding.titleInput.getText()).toString();
        String noteContentInput = Objects.requireNonNull(binding.contentInput.getText()).toString();

        Intent resultIntent = new Intent();
        resultIntent.putExtra(MindBinDatabase.COLUMN_NOTE_ID, noteIDExtra);
        resultIntent.putExtra(MindBinDatabase.COLUMN_NOTE_TITLE, noteTitleInput);
        resultIntent.putExtra(MindBinDatabase.COLUMN_NOTE_CONTENT, noteContentInput);
        resultIntent.putExtra(MindBinDatabase.COLUMN_NOTE_LAST_UPDATED, System.currentTimeMillis());
        resultIntent.putExtra(MindBinDatabase.COLUMN_NOTE_STATE, noteStateExtra);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.update_note, menu);

        if (noteStateExtra == NoteState.NORMAL.value) {
            menu.findItem(R.id.unarchive).setVisible(false);
            menu.findItem(R.id.restore).setVisible(false);
            menu.findItem(R.id.delete_forever).setVisible(false);
        } else if (noteStateExtra == NoteState.ARCHIVE.value) {
            menu.findItem(R.id.archive).setVisible(false);
            menu.findItem(R.id.restore).setVisible(false);
            menu.findItem(R.id.delete_forever).setVisible(false);
        } else if (noteStateExtra == NoteState.TRASH.value) {
            menu.findItem(R.id.trash).setVisible(false);
            menu.findItem(R.id.unarchive).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) updateNote();
        if (item.getItemId() == R.id.share) showShareIntent();

        if (item.getItemId() == R.id.archive) {
            noteStateExtra = NoteState.ARCHIVE.value;
            updateNote();
        }

        if (item.getItemId() == R.id.trash) {
            noteStateExtra = NoteState.TRASH.value;
            updateNote();
        }

        if (item.getItemId() == R.id.unarchive || item.getItemId() == R.id.restore) {
            noteStateExtra = NoteState.NORMAL.value;
            updateNote();
        }

        if (item.getItemId() == R.id.delete_forever) showDeleteDialog();
        return true;
    }

    private void showDeleteDialog() {
        @SuppressLint("UseCompatLoadingForDrawables")
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle(getResources().getString(R.string.dialog_note_delete_title))
                .setMessage(getResources().getString(R.string.dialog_note_delete_message))
                .setIcon(getResources().getDrawable(R.drawable.ic_warning_filled, getTheme()))
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    Intent deleteIntent = new Intent();
                    deleteIntent.putExtra(MindBinDatabase.COLUMN_NOTE_ID, noteIDExtra);
                    setResult(RESULT_DELETE, deleteIntent);
                    finish();
                });

        Dialog deleteDialog = builder.create();
        deleteDialog.show();
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