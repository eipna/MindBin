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

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private ArrayList<NoteModel> notesArrayList;
    private DatabaseHelper databaseHelper;
    private ImageView iv_favorites;
    private View view;
    private TextView tv_favorites;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorites, container, false);

        initializeComponents();
        storeNoteData();

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

        return view;
    }

    private void storeNoteData() {
        Cursor cursor = databaseHelper.getFavoriteNotes();
        if (cursor.getCount() == 0) {
            tv_favorites.setVisibility(View.VISIBLE);
            iv_favorites.setVisibility(View.VISIBLE);
        } else {
            tv_favorites.setVisibility(View.GONE);
            iv_favorites.setVisibility(View.GONE);
            while (cursor.moveToNext()) {
                notesArrayList.add(new NoteModel(
                        cursor.getInt(0),
                        cursor.getString(1), getNoteCreationDateType(cursor.getString(2)),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5))
                );
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

    private void initializeComponents() {
        recyclerView = view.findViewById(R.id.recycler_view_note_favorites);
        notesArrayList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(getContext());
        tv_favorites = view.findViewById(R.id.tv_no_favorites);
        iv_favorites = view.findViewById(R.id.iv_no_favorites);
        preferences = getContext().getSharedPreferences("MODE", Context.MODE_PRIVATE);
    }
}