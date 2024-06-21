package com.serbi.mindbin.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.serbi.mindbin.R;
import com.serbi.mindbin.constants.NoteStatus;
import com.serbi.mindbin.helpers.DatabaseHelper;
import com.serbi.mindbin.helpers.DateHelper;

public class CreateNoteActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private MaterialToolbar toolbar;
    private TextView tv_note_current_date;
    private EditText et_note_title, et_note_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeComponents();
        setFocusOnField();
        setToolbar();

        tv_note_current_date.setText(DateHelper.getCurrentDetailedDate());
    }

    private void setFocusOnField() {
        et_note_title.requestFocus();

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(et_note_title, InputMethodManager.SHOW_IMPLICIT);
    }

    private void saveNote() {
        String noteTitle = et_note_title.getText().toString();
        String noteContent = et_note_content.getText().toString();
        String noteDateCreation = DateHelper.getCurrentSimpleDate();
        String noteStatus = NoteStatus.NORMAL.toString();

        if (noteTitle.isEmpty()) {
            noteTitle = "Empty note";
        }

        if (noteContent.isEmpty()) {
            noteContent = "Empty content";
        }

        databaseHelper.createNote(noteTitle, noteContent, noteStatus, noteDateCreation, "no");
        startActivity(new Intent(CreateNoteActivity.this, MainActivity.class));
        finish();
    }

    private void initializeComponents() {
        tv_note_current_date = findViewById(R.id.tv_note_current_date);
        et_note_title = findViewById(R.id.et_note_title);
        et_note_content = findViewById(R.id.et_note_content);
        toolbar = findViewById(R.id.toolbar_create_note);
        databaseHelper = new DatabaseHelper(CreateNoteActivity.this);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.previous_arrow_backward_svgrepo_com);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveNote();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}