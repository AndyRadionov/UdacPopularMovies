package io.github.andyradionov.udacpopularmovies.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static io.github.andyradionov.udacpopularmovies.data.db.MoviesContract.MovieEntry;

/**
 * @author Andrey Radionov
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";

    private static final int VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE = "CREATE TABLE "  +
                MovieEntry.TABLE_NAME           + " ("    +
                MovieEntry._ID                  + " INTEGER PRIMARY KEY, " +
                MovieEntry.COLUMN_TITLE         + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE  + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POSTER_PATH   + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_VOTE_AVERAGE  + " REAL NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW      + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
