package com.serbi.mindbin.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.serbi.mindbin.R;
import com.serbi.mindbin.activities.CreateNoteActivity;
import com.serbi.mindbin.adapters.NoteAdapter;
import com.serbi.mindbin.helpers.DatabaseHelper;
import com.serbi.mindbin.helpers.DateHelper;
import com.serbi.mindbin.models.NoteModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NotesFragment extends Fragment {

    private View view;
    private FloatingActionButton btn_add_note;
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private ArrayList<NoteModel> notesArrayList;
    private NoteAdapter adapter;
    private ImageView iv_no_notes, iv_clickable_sort;
    private TextView tv_no_notes;
    private SearchView searchView;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notes, container, false);

        initializeComponents();
        storeNoteData();

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchNotes(newText);
                return true;
            }
        });

        adapter = new NoteAdapter(getContext(), notesArrayList);
        recyclerView.setAdapter(adapter);

        if (preferences.getBoolean("isGridMode", false)) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);

            recyclerView.setLayoutManager(linearLayoutManager);
        }

        btn_add_note.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CreateNoteActivity.class));
        });

        iv_clickable_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu sortMenu = new PopupMenu(getContext(), iv_clickable_sort);

                sortMenu.getMenuInflater().inflate(R.menu.popup_sort, sortMenu.getMenu());
                sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.item_sortTitleASC) {
                            sortNotes(NoteModel.sortTitleByAsc);
                        }

                        if (item.getItemId() == R.id.item_sortTitleDESC) {
                            sortNotes(NoteModel.sortTitleByDesc);
                        }

                        if (item.getItemId() == R.id.item_sortDateCreationNewest) {
                            sortNotes(NoteModel.sortDateCreationByNewest);
                        }

                        if (item.getItemId() == R.id.item_sortDateCreationOldest) {
                            sortNotes(NoteModel.sortDateCreationByOldest);
                        }
                        return true;
                    }
                });
                sortMenu.show();
            }
        });
        return view;
    }

    private void sortNotes(Comparator<NoteModel> sort) {
        Collections.sort(notesArrayList, sort);
        adapter.notifyDataSetChanged();
    }

    private void initializeComponents() {
        btn_add_note = view.findViewById(R.id.btn_add_note);
        recyclerView = view.findViewById(R.id.recycler_view_note);
        databaseHelper = new DatabaseHelper(getContext());
        iv_no_notes = view.findViewById(R.id.iv_no_notes);
        tv_no_notes = view.findViewById(R.id.tv_no_notes);
        searchView = view.findViewById(R.id.searchview_notes);
        iv_clickable_sort = view.findViewById(R.id.tv_clickable_sort);
        notesArrayList = new ArrayList<>();
        preferences = getContext().getSharedPreferences("MODE", Context.MODE_PRIVATE);
    }

    private void searchNotes(String text) {
        ArrayList<NoteModel> searchedNotes = new ArrayList<>();
        for (NoteModel note : notesArrayList) {
            if (note.getTitle().toLowerCase().contains(text.toLowerCase())) {
                searchedNotes.add(note);
            }
        }
        adapter.setSearchList(searchedNotes);
    }

    private void storeNoteData() {
        Cursor cursor = databaseHelper.getNormalNotes();
        if (cursor.getCount() == 0) {
            iv_no_notes.setVisibility(View.VISIBLE);
            tv_no_notes.setVisibility(View.VISIBLE);
        } else {
            tv_no_notes.setVisibility(View.GONE);
            iv_no_notes.setVisibility(View.GONE);
            while (cursor.moveToNext()) {
                NoteModel noteModel = new NoteModel(cursor.getInt(0),
                        cursor.getString(1), getNoteCreationDateType(cursor.getString(2)),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5));
                noteModel.setOriginalDateCreation(cursor.getString(2));
                notesArrayList.add(noteModel);
            }
        }
    }

    private String getNoteCreationDateType(String date) {
        if (preferences.getBoolean("isSimpleDate", false)) {
            return DateHelper.convertSimpleToNormalDate(date);
        } else {
            return DateHelper.convertSimpleToDetailedDate(date);
        }
    }
}