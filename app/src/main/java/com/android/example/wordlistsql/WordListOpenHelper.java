package com.android.example.wordlistsql;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WordListOpenHelper extends SQLiteOpenHelper {


    // It's a good idea to always define a log tag like this.
    private static final String TAG = WordListOpenHelper.class.getSimpleName();
    // has to be 1 first time or app will crash
    private static final int DATABASE_VERSION = 1;
    private static final String WORD_LIST_TABLE = "word_entries";
    private static final String DATABASE_NAME = "wordlist";
    // Column names...
    public static final String KEY_ID = "_id";
    public static final String KEY_WORD = "word";
    // ... and a string array of columns.
    private static final String[] COLUMNS = { KEY_ID, KEY_WORD };

    // Build the SQL query that creates the table.
    private static final String WORD_LIST_TABLE_CREATE =
            "CREATE TABLE " + WORD_LIST_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY, " +
// id will auto-increment if no value passed
                    KEY_WORD + " TEXT );";

    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;


    public WordListOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(WORD_LIST_TABLE_CREATE);
        fillDatabaseWithData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void fillDatabaseWithData(SQLiteDatabase db){

        String[] words = {"Android", "Adapter", "ListView", "AsyncTask",
                "Android Studio", "SQLiteDatabase", "SQLOpenHelper",
                "Data model", "ViewHolder","Android Performance",
                "OnClickListener"};

        ContentValues values = new ContentValues();
        for (int i = 0; i < words.length; i++) {
            values.put(KEY_WORD, words[i]);
            db.insert(WORD_LIST_TABLE, null, values);
        }
    }

    @SuppressLint("Range")
    public WordItem query(int position) {
        String query = "SELECT * FROM " + WORD_LIST_TABLE +
                " ORDER BY " + KEY_WORD + " ASC " +
                "LIMIT " + position + ",1";
        Cursor cursor = null;
        WordItem entry = new WordItem();

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();
            entry.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            entry.setWord(cursor.getString(cursor.getColumnIndex(KEY_WORD)));

        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION! " + e);
        } finally {
            cursor.close();
            return entry;
        }

    }

    public long insert(String word){
        long newId = 0;
        ContentValues values = new ContentValues();
        values.put(KEY_WORD, word);
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            newId = mWritableDB.insert(WORD_LIST_TABLE, null, values);
        } catch (Exception e) {
            Log.d(TAG, "INSERT EXCEPTION! " + e.getMessage());
        }
        return newId;
    }

    public long count(){
        if (mReadableDB == null) {
            mReadableDB = getReadableDatabase();
        }
        return DatabaseUtils.queryNumEntries(mReadableDB, WORD_LIST_TABLE);
    }

    public int delete(int id) {

        int deleted = 0;
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            deleted = mWritableDB.delete(WORD_LIST_TABLE, KEY_ID + " = ? ", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.d (TAG, "DELETE EXCEPTION! " + e.getMessage());
        }
        return deleted;

    }

    public int update(int id, String word){
        int mNumberOfRowsUpdated = -1;
        try{
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            ContentValues values = new ContentValues();
            values.put(KEY_WORD, word);
            mNumberOfRowsUpdated = mWritableDB.update(WORD_LIST_TABLE, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        }catch (Exception e){
            Log.d (TAG, "UPDATE EXCEPTION: " + e.getMessage());
        }
        return mNumberOfRowsUpdated;
    }

    public Cursor search(String word) {
        // Declaramos la variable del cursor
        Cursor cursor = null;

        try {
            // Obtener la base de datos en modo lectura (si no está ya obtenida)


            if (mReadableDB== null) {
                mReadableDB = getReadableDatabase();  // Obtén la base de datos si no estaba disponible
            }

            // Definir las columnas que necesitamos
            String[] columns = { WordListOpenHelper.KEY_WORD };

            // Crear el parámetro de búsqueda (agregar % a la búsqueda)
            String searchString = "%" + word + "%";

            // Construir la cláusula WHERE (sin "WHERE" ya que se maneja en query())
            String selection = WordListOpenHelper.KEY_WORD + " LIKE ?";

            // Realizar la consulta usando parámetros para evitar inyección SQL
            cursor = mReadableDB.query(WordListOpenHelper.WORD_LIST_TABLE, // Nombre de la tabla
                    columns,                           // Columnas que necesitamos
                    selection,                         // Cláusula WHERE
                    new String[]{searchString},        // Parámetro para LIKE
                    null,                              // No necesitamos agrupar resultados
                    null,                              // No necesitamos ordenar los resultados
                    null);                             // No necesitamos un límite

        } catch (Exception e) {
            // Manejar excepciones (como problemas de base de datos)
            Log.e("WordListOpenHelper", "Error al realizar la búsqueda: ", e);
        }

        // Devolver el cursor con los resultados de la búsqueda
        return cursor;
    }

}