package com.serbi.mindbin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.serbi.mindbin.R;
import com.serbi.mindbin.helpers.DatabaseHelper;
import com.serbi.mindbin.helpers.DateHelper;

public class EditNote extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tv_note_date_creation_edit;
    private EditText et_note_title_edit, et_note_content_edit;
    private FloatingActionButton btn_edit_note;

    private DatabaseHelper databaseHelper;
    private String note_title, note_creationDate, note_content, note_status;
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
}