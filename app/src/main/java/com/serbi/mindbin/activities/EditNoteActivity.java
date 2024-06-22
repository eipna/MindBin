package com.serbi.mindbin.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.serbi.mindbin.R;
import com.serbi.mindbin.constants.NoteStatus;
import com.serbi.mindbin.helpers.DatabaseHelper;
import com.serbi.mindbin.helpers.DateHelper;

public class EditNoteActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tv_note_date_creation_edit;
    private EditText et_note_title_edit, et_note_content_edit;

    private DatabaseHelper databaseHelper;
    private String note_title, note_creationDate, note_content, note_status, note_isFavorite;
    private int note_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeComponents();
        setupToolbar();
        getSelectedNoteData();

        et_note_title_edit.setText(note_title);
        et_note_content_edit.setText(note_content);
        tv_note_date_creation_edit.setText(note_creationDate);
    }

    private void updateNote() {
        String updatedNoteTitle = et_note_title_edit.getText().toString();
        String updatedNoteContent = et_note_content_edit.getText().toString();
        databaseHelper.editNote(
                note_id, updatedNoteTitle, updatedNoteContent,
                note_status
        );

        startActivity(new Intent(EditNoteActivity.this, MainActivity.class));
        finish();
    }

    private void getSelectedNoteData() {
        note_id = getIntent().getIntExtra("id", -1);
        note_title = getIntent().getStringExtra("title");
        note_creationDate = getIntent().getStringExtra("creation_date");
        note_content = getIntent().getStringExtra("content");
        note_status = getIntent().getStringExtra("status");
        note_isFavorite = getIntent().getStringExtra("isFavorite");
    }

    private void initializeComponents() {
        toolbar = findViewById(R.id.toolbar_edit_note);
        tv_note_date_creation_edit = findViewById(R.id.tv_note_current_date_edit);
        et_note_title_edit = findViewById(R.id.et_note_title_edit);
        et_note_content_edit = findViewById(R.id.et_note_content_edit);
        databaseHelper = new DatabaseHelper(this);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.previous_arrow_backward_svgrepo_com);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (note_isFavorite.equalsIgnoreCase("yes")) {
            getMenuInflater().inflate(R.menu.toolbar_note_isfavorite, menu);
        }

        if (note_isFavorite.equalsIgnoreCase("no") && note_status.equals(NoteStatus.NORMAL.toString())) {
            getMenuInflater().inflate(R.menu.toolbar_edit_menu, menu);
        }

        if (note_status.equals(NoteStatus.ARCHIVED.toString())) {
            getMenuInflater().inflate(R.menu.toolbar_edit_menu_archive, menu);
        }

        if (note_status.equals(NoteStatus.DELETED.toString())) {
            getMenuInflater().inflate(R.menu.toolbar_edit_menu_trash, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            updateNote();
            finish();
        }

        if (item.getItemId() == R.id.item_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, note_content);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }

        if (item.getItemId() == R.id.item_addToFavorites) {
            if (note_isFavorite.equalsIgnoreCase("no")) {
                databaseHelper.addToFavorites(note_id);
            }

            if (note_isFavorite.equalsIgnoreCase("yes")) {
                databaseHelper.removeFromFavorites(note_id);
            }
        }

        if (item.getItemId() == R.id.item_addToFavorites_inFavorites) {
            databaseHelper.removeFromFavorites(note_id);
            startActivity(new Intent(EditNoteActivity.this, MainActivity.class));
            finish();
        }

        if (item.getItemId() == R.id.item_archive) {
            databaseHelper.updateNoteStatus(note_id, NoteStatus.ARCHIVED);
            startActivity(new Intent(EditNoteActivity.this, MainActivity.class));
            finish();
        }

        if (item.getItemId() == R.id.item_trash) {
            databaseHelper.removeFromFavorites(note_id);
            databaseHelper.updateNoteStatus(note_id, NoteStatus.DELETED);
            startActivity(new Intent(EditNoteActivity.this, MainActivity.class));
            finish();
        }

        if (item.getItemId() == R.id.item_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditNoteActivity.this);
            builder.setTitle("Delete Note");
            builder.setMessage("This will permanently delete this note.");

            builder.setPositiveButton("Yes", (dialog, which) -> {
                databaseHelper.deleteNote(note_id);
                startActivity(new Intent(EditNoteActivity.this, MainActivity.class));
                finish();
            });
            builder.setNegativeButton("No", (dialog, which) -> {});

            builder.create().show();
        }

        if (item.getItemId() == R.id.item_unarchive || item.getItemId() == R.id.item_restore) {
            databaseHelper.revertNoteStatus(note_id);
            startActivity(new Intent(EditNoteActivity.this, MainActivity.class));
            finish();
        }
        return true;
    }
}