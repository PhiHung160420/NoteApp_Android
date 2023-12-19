package com.example.noteapp.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.R;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle, tvNotes, tvDate;
    CardView notesContainer;
    ImageView imgPin;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tv_title);
        tvNotes = itemView.findViewById(R.id.tv_notes);
        tvDate = itemView.findViewById(R.id.tv_date);
        notesContainer = itemView.findViewById(R.id.notes_container);
        imgPin = itemView.findViewById(R.id.img_pin);
    }
}
