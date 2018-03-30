package com.example.keke.notebook;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by keke on 11/24/17.
 */

public class NotesCursorAdapter extends CursorAdapter {
    public NotesCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.note_list_item, parent, false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String noteText = cursor.getString(
                cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT)) + cursor.getString(
                cursor.getColumnIndex(DBOpenHelper.NOTE_CREATED));

        int position = noteText.indexOf(10);
        if (position != -1){
            noteText = noteText.substring(0, position) + " ... ";
        }

        TextView textView = view.findViewById(R.id.exampleNote);
        textView.setText(noteText);
    }
}