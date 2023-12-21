package com.example.noteapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.Models.Notes;
import com.example.noteapp.NotesClickListener;
import com.example.noteapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteListAdapter extends RecyclerView.Adapter<NoteViewHolder> {
    Context context;
    List<Notes> listNotes;
    NotesClickListener listener;

    public NoteListAdapter(Context context, List<Notes> listNotes, NotesClickListener listener) {
        this.context = context;
        this.listNotes = listNotes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Notes notes = listNotes.get(position);
        if (notes == null) {
            return;
        }
        holder.tvTitle.setText(notes.getTitle());
        holder.tvTitle.setSelected(true);

        holder.tvNotes.setText(notes.getNotes());

        holder.tvDate.setText(notes.getDate());
        holder.tvDate.setSelected(true);

        if (notes.isPinned()) {
            holder.imgPin.setImageResource(R.drawable.ic_pin);
        } else {
            holder.imgPin.setImageResource(0);
        }

        int colorCode = randomColor();
        holder.notesContainer.setCardBackgroundColor(holder.itemView.getResources().getColor(colorCode, null));

        holder.notesContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(listNotes.get(holder.getAdapterPosition()));
            }
        });

        holder.notesContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(listNotes.get(holder.getAdapterPosition()), holder.notesContainer);
                return true;
            }
        });
    }

    private int randomColor() {
        List<Integer> colorCodes = new ArrayList<>();
        colorCodes.add(R.color.color1);
        colorCodes.add(R.color.color2);
        colorCodes.add(R.color.color3);
        colorCodes.add(R.color.color4);
        colorCodes.add(R.color.color5);
        Random random = new Random();
        int random_code = random.nextInt(colorCodes.size());
        return colorCodes.get(random_code);
    }

    @Override
    public int getItemCount() {
        if (listNotes != null) {
            return listNotes.size();
        }
        return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<Notes> filteredList) {
        this.listNotes = filteredList;
        notifyDataSetChanged();
    }
}

