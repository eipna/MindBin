package com.serbi.mindbin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.serbi.mindbin.R;
import com.serbi.mindbin.models.Note;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    Context context;
    ArrayList<Note> noteArrayList;

    public NoteAdapter(Context context, ArrayList<Note> noteArrayList) {
        this.context = context;
        this.noteArrayList = noteArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.et_note_title.setText(noteArrayList.get(position).getTitle());
        holder.et_note_content.setText(noteArrayList.get(position).getContent());
        holder.et_note_creation_date.setText(noteArrayList.get(position).getDateCreation());
    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView et_note_title, et_note_creation_date, et_note_content;
        CardView note_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            et_note_title = itemView.findViewById(R.id.note_item_title);
            et_note_content = itemView.findViewById(R.id.note_item_content);
            et_note_creation_date = itemView.findViewById(R.id.note_item_creation_date);
            note_item = itemView.findViewById(R.id.note_item);
        }
    }
}