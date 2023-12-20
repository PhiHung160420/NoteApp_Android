package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.noteapp.Models.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesTakerActivity extends AppCompatActivity {
    ImageView imgSave;
    EditText edtTitle, edtNote;
    Notes note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);
        imgSave = findViewById(R.id.img_save);
        edtNote = findViewById(R.id.edt_note);
        edtTitle = findViewById(R.id.edt_title);

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title  = edtTitle.getText().toString();
                String description = edtNote.getText().toString();

                if (title.isEmpty() ||  description.isEmpty()) {
                    Toast.makeText(NotesTakerActivity.this, "Please enter fully information", Toast.LENGTH_LONG).show();
                }
                SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                Date date = new Date();
                note = new Notes();
                note.setTitle(title);
                note.setNotes(description);
                note.setDate(dateFormatter.format(date));

                Intent intent = new Intent();
                intent.putExtra("note", note);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}