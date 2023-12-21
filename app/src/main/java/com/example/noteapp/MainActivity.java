package com.example.noteapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.noteapp.Adapters.NoteListAdapter;
import com.example.noteapp.Database.RoomDB;
import com.example.noteapp.Models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    RecyclerView rcvHome;
    NoteListAdapter noteListAdapter;
    List<Notes> notes = new ArrayList<>();
    RoomDB database;
    FloatingActionButton btnAdd;
    SearchView searchViewHome;
    Notes selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rcvHome = findViewById(R.id.rcv_home);
        btnAdd = findViewById(R.id.fab_add);
        searchViewHome = findViewById(R.id.search_view_home);

        database = RoomDB.getInstance(this);
        notes = database.mainDAO().getAll();
        rcvHome.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        updateRecycleView(notes);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        searchViewHome.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onFilter(newText);
                return false;
            }
        });
    }

    private void onFilter(String newText) {
        List<Notes> filterdList = new ArrayList<>();
        for (Notes singleNote : notes) {
            if (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase()) || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())) {
                filterdList.add(singleNote);
            }
        }
        noteListAdapter.filterList(filterdList);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            Notes note = (Notes) data.getSerializableExtra("note");
            database.mainDAO().insert(note);
            refreshList();
        } else if (requestCode == 102 && resultCode == Activity.RESULT_OK) {
            Notes new_note = (Notes) data.getSerializableExtra("note");
            database.mainDAO().update(new_note.getID(), new_note.getTitle(), new_note.getNotes());
            refreshList();
        }
    }

    private void updateRecycleView(List<Notes> notes) {
        rcvHome.setHasFixedSize(true);
        noteListAdapter = new NoteListAdapter(MainActivity.this, notes, notesClickListener);
        rcvHome.setAdapter(noteListAdapter);
    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selectedNote = new Notes();
            selectedNote = notes;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.menu_note);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_pin) {
            if (selectedNote.isPinned()) {
                database.mainDAO().pin(selectedNote.getID(), false);
                Toast.makeText(MainActivity.this, "Unpined!", Toast.LENGTH_SHORT).show();
            } else {
                database.mainDAO().pin(selectedNote.getID(), true);
                Toast.makeText(MainActivity.this, "Pined!", Toast.LENGTH_SHORT).show();
            }
            refreshList();
            return true;
        } else if (item.getItemId() == R.id.menu_item_delete) {
            database.mainDAO().delete(selectedNote);
            notes.remove(selectedNote);
            noteListAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Note Deleted!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void refreshList() {
        notes.clear();
        notes.addAll(database.mainDAO().getAll());
        noteListAdapter.notifyDataSetChanged();
    }
}