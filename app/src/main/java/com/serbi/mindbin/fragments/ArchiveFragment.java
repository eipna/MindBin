package com.serbi.mindbin.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.serbi.mindbin.R;
import com.serbi.mindbin.adapters.NoteAdapter;
import com.serbi.mindbin.helpers.DatabaseHelper;
import com.serbi.mindbin.helpers.DateHelper;
import com.serbi.mindbin.models.NoteModel;

import java.util.ArrayList;

public class ArchiveFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private NoteAdapter adapter;
    private ArrayList<NoteModel> notesArrayList;
    private ImageView iv_no_notes_archive;
    private TextView tv_no_notes_archive;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_archive, container, false);
        initializeComponents();
        storeNoteData();

        adapter = new NoteAdapter(getContext(), notesArrayList);
        recyclerView.setAdapter(adapter);

        SharedPreferences preferences = getContext().getSharedPreferences("MODE", Context.MODE_PRIVATE);

        if (preferences.getBoolean("isGridMode", false)) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        return view;
    }

    private void initializeComponents() {
        recyclerView = view.findViewById(R.id.recycler_view_note_archive);
        databaseHelper = new DatabaseHelper(getContext());
        iv_no_notes_archive = view.findViewById(R.id.iv_no_notes_archive);
        tv_no_notes_archive = view.findViewById(R.id.tv_no_notes_archive);
        notesArrayList = new ArrayList<>();
    }

    private void storeNoteData() {
        Cursor cursor = databaseHelper.getArchivedNotes();
        if (cursor.getCount() == 0) {
            tv_no_notes_archive.setVisibility(View.VISIBLE);
            iv_no_notes_archive.setVisibility(View.VISIBLE);
        } else {
            tv_no_notes_archive.setVisibility(View.GONE);
            iv_no_notes_archive.setVisibility(View.GONE);
            while (cursor.moveToNext()) {
                notesArrayList.add(new NoteModel(
                        cursor.getInt(0),
                        cursor.getString(1), DateHelper.convertSimpleToDetailedDate(cursor.getString(2)),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5))
                );
            }
        }
    }
}