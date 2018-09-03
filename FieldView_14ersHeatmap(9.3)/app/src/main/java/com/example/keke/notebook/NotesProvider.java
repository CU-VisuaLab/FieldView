package com.example.keke.notebook;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Random;

/**
 * Created by keke on 11/23/17.
 */

public class NotesProvider extends ContentProvider{

    private static final String AUTHORITY = "com.example.keke.notebook.notesprovider";
    private static final String BASE_PATH = "notes";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    // Constant to identify the requested operation
    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    public static final String CONTENT_ITEM_TYPE = "Notes";

    public static String allEntriesStr = "Hasn't been intitalized";
    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, NOTES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTES_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {

        DBOpenHelper helper = new DBOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    public static String getAllEntries() {
        return allEntriesStr;
    }

    @Nullable
    @Override
    public Cursor query( Uri uri,  String[] projection,  String selection, String[] selectionArgs, String sortOrder) {

        if(uriMatcher.match(uri) == NOTES_ID){
            selection = DBOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();
        }

        Cursor allEntries = database.query(DBOpenHelper.TABLE_NOTES, DBOpenHelper.ALL_COLUMNS,
                selection, null, null, null,
                DBOpenHelper.NOTE_CREATED + " DESC");
        allEntriesStr = "[";
        if (allEntries.moveToFirst()) {
            while (!allEntries.isAfterLast()) {
                Random rand = new Random();
                int randomElevation = 14000 + rand.nextInt(1000);
                allEntriesStr += (
                        "{\"title\": \"" + allEntries.getString(allEntries.getColumnIndex("noteText")) +
                        "\", \"longitude\": \"" + allEntries.getString(allEntries.getColumnIndex("longitude")) +
                        "\", \"latitude\": \"" + allEntries.getString(allEntries.getColumnIndex("latitude")) +
                        "\", \"altitude\": \"" + Integer.toString(randomElevation) + "\"}, ");
                allEntries.moveToNext();
            }
            allEntriesStr = allEntriesStr.substring(0, allEntriesStr.length() - 2);
        }
        allEntriesStr += ']';
        return allEntries;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = database.insert(DBOpenHelper.TABLE_NOTES,
                null, values);
        return Uri.parse(BASE_PATH + "/" + id);
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.delete(DBOpenHelper.TABLE_NOTES, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.update(DBOpenHelper.TABLE_NOTES,
                values, selection, selectionArgs);
    }
}