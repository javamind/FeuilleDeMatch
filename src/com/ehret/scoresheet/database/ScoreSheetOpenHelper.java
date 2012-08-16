package com.ehret.scoresheet.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ehret.scoresheet.domain.MatchEvent;
import com.ehret.scoresheet.domain.MatchTypeEvent;
import com.ehret.scoresheet.domain.ScoreSheet;
import com.ehret.scoresheet.domain.Sport;

/**
 * The application's parameters are stored in a SQL Lite DBMS. This class is an
 * helper to access to the data
 */
public class ScoreSheetOpenHelper extends SQLiteOpenHelper {
    // Used for debugging and logging
    private static final String TAG = "ScoreSheetOpenHelper";
    
    /** 
     * The constructor is only seen by the class who are in the same package
     */
    ScoreSheetOpenHelper(Context context) {
	// calls the super constructor, requesting the default cursor factory.
	super(context, ScoreSheetDatabase.DATABASE_NAME, null, ScoreSheetDatabase.DATABASE_VERSION);
    }

    /**
     * Creates the underlying database with table name and column names taken
     * from the NotePad class.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
	// Logs that the database is being upgraded
	Log.w(TAG, "Create database version " + ScoreSheetDatabase.DATABASE_VERSION);
	create_tables(db);
    }

    /**
     * N/A for this first version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	// Logs that the database is being upgraded
	Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
		+ ", which will destroy all old data");
    }
    
    
    /**
     * Table creation in the DBMS
     * @param db
     */
    private void create_tables(SQLiteDatabase db){
	// Sport table
	db.execSQL(new StringBuilder("CREATE TABLE ")
        	.append(Sport.TABLE_NAME).append(" (")
        	.append(Sport.COLUMN_NAME_ID).append(" INTEGER PRIMARY KEY,")
        	.append(Sport.COLUMN_NAME_LABEL).append(" TEXT")
        	.append(");").toString());
	// Type event table
	db.execSQL(new StringBuilder("CREATE TABLE ")
        	.append(MatchTypeEvent.TABLE_NAME).append(" (")
        	.append(MatchTypeEvent.COLUMN_NAME_ID).append(" INTEGER PRIMARY KEY,")
        	.append(MatchTypeEvent.COLUMN_NAME_LABEL).append(" TEXT,")
        	.append(MatchTypeEvent.COLUMN_NAME_SCORE)
        	.append(" INTEGER,").append(MatchTypeEvent.COLUMN_NAME_ID_SPORT).append(" INTEGER,")
        	.append("FOREIGN KEY(").append(MatchTypeEvent.COLUMN_NAME_ID_SPORT).append(") ").append("REFERENCES ")
        	.append(Sport.TABLE_NAME).append("(").append(Sport.COLUMN_NAME_ID).append(")").append(");").toString());
	// Score Sheet table
	db.execSQL(new StringBuilder("CREATE TABLE ")
        	.append(ScoreSheet.TABLE_NAME).append(" (")
        	.append(ScoreSheet.COLUMN_NAME_ID).append(" INTEGER PRIMARY KEY,")
        	.append(ScoreSheet.COLUMN_NAME_DATE).append(" TEXT,")
        	.append(ScoreSheet.COLUMN_NAME_SCORE_HOME).append(" INTEGER,")
        	.append(ScoreSheet.COLUMN_NAME_SCORE_VISITOR).append(" INTEGER,")
        	.append(ScoreSheet.COLUMN_NAME_ID_SPORT).append(" INTEGER,")
        	.append("FOREIGN KEY(").append(ScoreSheet.COLUMN_NAME_ID_SPORT).append(") ").append("REFERENCES ")
        	.append(Sport.TABLE_NAME).append("(").append(Sport.COLUMN_NAME_ID).append(")").append(");").toString());
	// Event table
	db.execSQL(new StringBuilder("CREATE TABLE ")
        	.append(MatchEvent.TABLE_NAME).append(" (")
        	.append(MatchEvent.COLUMN_NAME_ID).append(" INTEGER PRIMARY KEY,")
        	.append(MatchEvent.COLUMN_NAME_COMMENT).append(" TEXT,")
        	.append(MatchEvent.COLUMN_NAME_ID_SCORE_SHEET).append(" INTEGER,")
        	.append(MatchEvent.COLUMN_NAME_ID_TYPE_EVENT).append(" INTEGER,")
        	.append(MatchEvent.COLUMN_NAME_MINUTE).append(" INTEGER,")
        	.append(MatchEvent.COLUMN_NAME_PLAYER).append(" INTEGER,")
        	.append(MatchEvent.COLUMN_NAME_TEAM).append(" TEXT,")
        	.append("FOREIGN KEY(").append(MatchEvent.COLUMN_NAME_ID_SCORE_SHEET).append(") ").append("REFERENCES ")
        	.append(ScoreSheet.TABLE_NAME).append("(").append(ScoreSheet.COLUMN_NAME_ID).append("), ")
        	.append("FOREIGN KEY(").append(MatchEvent.COLUMN_NAME_ID_TYPE_EVENT).append(") ").append("REFERENCES ")
        	.append(MatchTypeEvent.TABLE_NAME).append("(").append(MatchTypeEvent.COLUMN_NAME_ID).append(")").append(");").toString());		
    }
}
