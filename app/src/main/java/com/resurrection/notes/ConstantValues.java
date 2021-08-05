package com.resurrection.notes;

public class ConstantValues {

        public static final String dbName = "Note";
        public static final int dbVersion = 1;
        public static final String dbTableName = "NoteTable";
        public static final String dbNotePrimarykey = "primaryKey";
        public static final String dbNoteId = "id";
        public static final String dbNoteHeader = "header";
        public static final String dbNoteContent = "content";
        public static final String dbNoteDate = "date";

        public static final String dbCreateTableQuery =
                "CREATE TABLE "+dbTableName +" ( "
                +dbNotePrimarykey+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +dbNoteId+" TEXT, "
                +dbNoteHeader +" TEXT,"
                +dbNoteContent +" TEXT,"
                +dbNoteDate+" TEXT "
                +" )";





}
