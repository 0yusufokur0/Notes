package com.resurrection.notes;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(@Nullable Context context) {
        super(context, ConstantValues.dbName, null, ConstantValues.dbVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ConstantValues.dbCreateTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {/*now have only one version*/}

    public void addNotes(String header, String content) {
        String date = getDate();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String noteId = firebaseIdCreator();
        System.out.println("databsae deki note ıd " + noteId);
        contentValues.put(ConstantValues.dbNoteId, noteId);
        contentValues.put(ConstantValues.dbNoteHeader, header);
        contentValues.put(ConstantValues.dbNoteContent, content);
        contentValues.put(ConstantValues.dbNoteDate, date);

        sqLiteDatabase.insert(ConstantValues.dbTableName, null, contentValues);
        sqLiteDatabase.close();
        firebaseNoteSync(noteId, header, content);

    }

    public ArrayList<NoteTemplate> getDataAndSet() {

        ArrayList<NoteTemplate> noteTemplateArrayList = new ArrayList<>();

        String selectionQuery = "SELECT * FROM " + ConstantValues.dbTableName;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectionQuery, null);

        while (cursor.moveToNext()) {
            NoteTemplate noteTemplate = new NoteTemplate(
                    "" + cursor.getInt(cursor.getColumnIndex(ConstantValues.dbNotePrimarykey)),
                    "" + cursor.getString(cursor.getColumnIndex(ConstantValues.dbNoteId)),
                    "" + cursor.getString(cursor.getColumnIndex(ConstantValues.dbNoteHeader)),
                    "" + cursor.getString(cursor.getColumnIndex(ConstantValues.dbNoteContent)),
                    "" + cursor.getString(cursor.getColumnIndex(ConstantValues.dbNoteDate))
            );
            noteTemplateArrayList.add(noteTemplate);
        }
        sqLiteDatabase.close();


        return noteTemplateArrayList;
    }


    public String getDate() {
        Date nowDateTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy kk:mm");
        String date = dateFormat.format(nowDateTime);
        return date;
    }

    public void deleteData(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(ConstantValues.dbTableName, ConstantValues.dbNoteId + "= ?", new String[]{id});
        sqLiteDatabase.close();

    }

    public void firebaseNoteSync(String noteId, String header, String content) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        HashMap<String, String> noteData = new HashMap<>();
        noteData.put("date", getDate());
        noteData.put("header", header);
        noteData.put("content", content);
        if (header.equals("") && content.equals("")) {
            noteData.clear();
        }

        databaseReference.child("users").child(firebaseUser.getUid()).child("notes").child(noteId).setValue(noteData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });

    }

    public void updateData(String id, String header, String content) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantValues.dbNoteHeader, header);
        contentValues.put(ConstantValues.dbNoteContent, content);
        contentValues.put(ConstantValues.dbNoteDate, getDate());
        sqLiteDatabase.update(ConstantValues.dbTableName, contentValues, ConstantValues.dbNoteId + " = ? ", new String[]{id});
        sqLiteDatabase.close();


    }

    public int getcount() {
        String countQuery = "SELECT * FROM " + ConstantValues.dbTableName;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        return cursor.getCount();
    }

    private String firebaseIdCreator() {
        Date nowDateTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy--kk_mm_ss");
        String date = dateFormat.format(nowDateTime);
        System.out.println(date);
        return date;
    }

    public boolean requestStoragePermission(Context context) {
        final int STORAGE_REQUEST_CODE = 101;
        String[] storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        boolean isPermissionGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        if (!isPermissionGranted) {
            ActivityCompat.requestPermissions((Activity) context, storagePermission, STORAGE_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }


}





