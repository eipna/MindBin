package com.serbi.mindbin.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.serbi.mindbin.R;
import com.serbi.mindbin.constants.NoteStatus;
import com.serbi.mindbin.fragments.Favorites;
import com.serbi.mindbin.fragments.Notes;
import com.serbi.mindbin.helpers.DatabaseHelper;
import com.serbi.mindbin.helpers.DateHelper;

public class EditNote extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tv_note_date_creation_edit;
    private EditText et_note_title_edit, et_note_content_edit;
    private FloatingActionButton btn_edit_note;

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

        btn_edit_note.setOnClickListener(v -> updateNote());
    }

    private void updateNote() {
        String updatedNoteTitle = et_note_title_edit.getText().toString();
        String updatedNoteContent = et_note_content_edit.getText().toString();
        String updatedNoteCreationDate = DateHelper.getCurrentSimpleDate();
        databaseHelper.editNote(
                note_id, updatedNoteTitle, updatedNoteContent,
                note_status, updatedNoteCreationDate
        );

        startActivity(new Intent(EditNote.this, Main.class));
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
        btn_edit_note = findViewById(R.id.btn_edit_note);
        databaseHelper = new DatabaseHelper(this);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
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
            finish();
        }

        if (item.getItemId() == R.id.item_addToFavorites) {
            if (note_isFavorite.equalsIgnoreCase("no")) {
                toolbar.getMenu().findItem(R.id.item_addToFavorites).setIcon(R.drawable.baseline_star_24);
                databaseHelper.addToFavorites(note_id);
            }

            if (note_isFavorite.equalsIgnoreCase("yes")) {
                toolbar.getMenu().findItem(R.id.item_addToFavorites).setIcon(R.drawable.baseline_star_border_24);
                databaseHelper.removeFromFavorites(note_id);
            }
        }

        if (item.getItemId() == R.id.item_addToFavorites_inFavorites) {
            toolbar.getMenu().findItem(R.id.item_addToFavorites_inFavorites).setIcon(R.drawable.baseline_star_border_24);
            databaseHelper.removeFromFavorites(note_id);
            startActivity(new Intent(EditNote.this, Main.class));
            finish();
        }

        if (item.getItemId() == R.id.item_archive) {
            databaseHelper.updateNoteStatus(note_id, NoteStatus.ARCHIVED);
            startActivity(new Intent(EditNote.this, Main.class));
            finish();
        }

        if (item.getItemId() == R.id.item_trash) {
            databaseHelper.updateNoteStatus(note_id, NoteStatus.DELETED);
            startActivity(new Intent(EditNote.this, Main.class));
            finish();
        }

        if (item.getItemId() == R.id.item_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditNote.this);
            builder.setTitle("Delete Note");
            builder.setMessage("This will permanently delete this note.");

            builder.setPositiveButton("Yes", (dialog, which) -> {
                databaseHelper.deleteNote(note_id);
                startActivity(new Intent(EditNote.this, Main.class));
                finish();
            });
            builder.setNegativeButton("No", (dialog, which) -> {});

            builder.create().show();
        }

        if (item.getItemId() == R.id.item_unarchive || item.getItemId() == R.id.item_restore) {
            databaseHelper.revertNoteStatus(note_id);
            startActivity(new Intent(EditNote.this, Main.class));
            finish();
        }
        return true;
    }
}