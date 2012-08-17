/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ehret.scoresheet.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.ehret.scoresheet.builder.ParameterBuilder;
import com.ehret.scoresheet.domain.MatchEvent;
import com.ehret.scoresheet.domain.MatchTypeEvent;
import com.ehret.scoresheet.domain.ScoreSheet;
import com.ehret.scoresheet.domain.Sport;
import com.ehret.scoresheet.domain.Team;

/**
 * Provides access to a database of sports.
 */
public class ScoreSheetDatabase {
    public static final String TAG = "ScoreSheetDatabase";
    /**
     * The database that the provider uses as its underlying data store
     */
    static final String DATABASE_NAME = "score_sheet.db";

    /**
     * The database version
     */
    static final int DATABASE_VERSION = 1;

    /**
     * Context
     */
    private Context context;

    /**
     * Handle to a new DatabaseHelper.
     */
    private ScoreSheetOpenHelper mOpenHelper;

    /**
     * this list is a cache to avoid to call the database for nothing
     */
    private List<MatchTypeEvent> typeEventsLoad = new ArrayList<MatchTypeEvent>();
    /**
     * this list is a cache to avoid to call the database for nothing
     */
    private List<Sport> sportsLoad = new ArrayList<Sport>();

    /**
     * Constructor
     * 
     * @param context
     *            The Context within which to work, used to create the DB
     */
    public ScoreSheetDatabase(Context context) {
	this.mOpenHelper = new ScoreSheetOpenHelper(context);
	this.context = context;
    }

    /**
     * Called if database is empty
     */
    private void initData() {
	// Opens the database object in "write" mode.
	SQLiteDatabase db = mOpenHelper.getWritableDatabase();

	try {
	    List<Sport> sports = ParameterBuilder.buildSport(this.context);

	    for (Sport sport : sports) {
		// ContentValues is a HasMap containing the column name and the
		// values
		ContentValues values = new ContentValues();
		values.put(Sport.COLUMN_NAME_ID, sport.getId());
		values.put(Sport.COLUMN_NAME_LABEL, sport.getLabel());

		Log.w(TAG, "Sauvegarde du sport " + sport.getLabel());
		db.insertOrThrow(Sport.TABLE_NAME, null, values);

		List<MatchTypeEvent> typesEvent = sport.getMatchTypesEvents();
		for (MatchTypeEvent typeEvent : typesEvent) {
		    ContentValues values2 = new ContentValues();
		    values2.put(MatchTypeEvent.COLUMN_NAME_ID, typeEvent.getId());
		    values2.put(MatchTypeEvent.COLUMN_NAME_LABEL, typeEvent.getLabel());
		    values2.put(MatchTypeEvent.COLUMN_NAME_ID_SPORT, sport.getId());
		    values2.put(MatchTypeEvent.COLUMN_NAME_SCORE, typeEvent.getScore());
		    db.insertOrThrow(MatchTypeEvent.TABLE_NAME, null, values2);
		}
	    }

	} finally {
	    db.close();
	}
    }

    /**
     * Save or update a match
     */
    public void saveMatch(ScoreSheet scoreSheet) {
	// Opens the database object in "write" mode.
	SQLiteDatabase db = mOpenHelper.getWritableDatabase();

	// We have to save the score sheet and theirs events
	ContentValues values = new ContentValues();
	values.put(ScoreSheet.COLUMN_NAME_ID_SPORT, scoreSheet.getSport().getId());
	values.put(ScoreSheet.COLUMN_NAME_DATE, scoreSheet.getDate());
	values.put(ScoreSheet.COLUMN_NAME_SCORE_HOME, scoreSheet.getScoreHome());
	values.put(ScoreSheet.COLUMN_NAME_SCORE_VISITOR, scoreSheet.getScoreVisitor());

	String whereClause = ScoreSheet.COLUMN_NAME_ID + "= ?";
	String whereClause2 = MatchEvent.COLUMN_NAME_ID + "= ?";
	String[] whereArgs = new String[] { scoreSheet.getId().toString() };
	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	qb.setTables(MatchEvent.TABLE_NAME);
	
	try {
	    db.beginTransaction();
	    if (scoreSheet.getId() == null || scoreSheet.getId()==0) {
		long rowId = db.insert(ScoreSheet.TABLE_NAME, null, values);
		scoreSheet.setId(rowId);
	    } else {
		db.update(ScoreSheet.TABLE_NAME, values, whereClause, whereArgs);
	    }
	    
	    for (MatchEvent matchEvent : scoreSheet.getMatchEvents()) {
		ContentValues values2 = new ContentValues();
		values2.put(MatchEvent.COLUMN_NAME_COMMENT, matchEvent.getComment());
		values2.put(MatchEvent.COLUMN_NAME_ID_SCORE_SHEET, scoreSheet.getId());
		values2.put(MatchEvent.COLUMN_NAME_ID_TYPE_EVENT, matchEvent.getMatchTypeEvent().getId());
		values2.put(MatchEvent.COLUMN_NAME_MINUTE, matchEvent.getMinute());
		values2.put(MatchEvent.COLUMN_NAME_PLAYER, matchEvent.getPlayer());
		values2.put(MatchEvent.COLUMN_NAME_TEAM, matchEvent.getTeam().name());
		values2.put(MatchEvent.COLUMN_NAME_ID, matchEvent.getId());
		
		//On regarde si la donnee est deja en base
		Cursor c = null;
		try {
		    c = qb.query(db, new String[]{MatchEvent.COLUMN_NAME_ID}, whereClause2, new String[] { matchEvent.getId().toString() }, null, null, null);
		    if (c != null && c.getCount() > 0) {
			db.update(MatchEvent.TABLE_NAME, values2, whereClause2, new String[] { matchEvent.getId().toString() });
		    }
		    else{
			matchEvent.setId(db.insert(MatchEvent.TABLE_NAME, null, values2));
		    }
		}
		finally{
		    if(c!=null){
			c.close();
		    }
		}
	    }
	    db.setTransactionSuccessful();
	    Log.w(TAG, "ScoreSheet saved ");

	} catch (Exception e) {
	    throw new SQLException("Failed to insert score sheet" + e.getMessage());
	} finally {
	    db.endTransaction();
	    db.close();
	}
    }

    /**
     * Delete a match
     */
    public void deleteMatch(ScoreSheet scoreSheet) {
	// Opens the database object in "write" mode.
	SQLiteDatabase db = mOpenHelper.getWritableDatabase();

	String whereClause = ScoreSheet.COLUMN_NAME_ID + "= ?";
	String[] whereArgs = new String[] { scoreSheet.getId().toString() };

	try {
	    db.beginTransaction();
	    db.delete(MatchEvent.TABLE_NAME, whereClause, whereArgs);
	    db.delete(ScoreSheet.TABLE_NAME, whereClause, whereArgs);
	    db.setTransactionSuccessful();
	    Log.w(TAG, "ScoreSheet deleted ");
	} catch (Exception e) {
	    throw new SQLException("Failed to insert score sheet" + e.getMessage());
	} finally {
	    db.endTransaction();
	    db.close();
	}
    }

    /**
     * A scoresheet is selected in a list loaded with {@link
     * this#getListSavedMatch()} and completed in this method
     */
    public MatchTypeEvent getMatchTypeEvent(Long id) {
	// if this type event is in the cache we return it
	for (MatchTypeEvent m : typeEventsLoad) {
	    if (m.getId().equals(id)) {
		return m;
	    }
	}

	// Opens the database object in "read" mode
	SQLiteDatabase db = mOpenHelper.getReadableDatabase();

	// Constructs a new query builder to read the events of the match
	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	qb.setTables(MatchTypeEvent.TABLE_NAME);

	Cursor c = null;
	try {
	    String[] colonnes = new String[] { MatchTypeEvent.COLUMN_NAME_ID, MatchTypeEvent.COLUMN_NAME_LABEL,
		    MatchTypeEvent.COLUMN_NAME_SCORE };

	    c = qb.query(db, colonnes, MatchTypeEvent.COLUMN_NAME_ID + "=" + id, null, null, null, null);

	    if (c != null && c.getCount() > 0) {
		c.moveToFirst();
		MatchTypeEvent type = new MatchTypeEvent();
		type.setId(c.getLong(0));
		type.setLabel(c.getString(1));
		type.setScore(c.getInt(2));
		typeEventsLoad.add(type);
		return type;
	    }
	} finally {
	    if (c != null) {
		c.close();
	    }
	    db.close();
	}
	return null;
    }

    /**
     * A scoresheet is selected in a list loaded with {@link
     * this#getListSavedMatch()} and completed in this method
     */
    public void completeScoreSheet(ScoreSheet scoreSheet) {
	// Opens the database object in "read" mode
	SQLiteDatabase db = mOpenHelper.getReadableDatabase();

	// Constructs a new query builder to read the events of the match
	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	qb.setTables(MatchEvent.TABLE_NAME);

	Cursor c = null;
	try {
	    String[] colonnes = new String[] { MatchEvent.COLUMN_NAME_ID, MatchEvent.COLUMN_NAME_COMMENT,
		    MatchEvent.COLUMN_NAME_ID_TYPE_EVENT, MatchEvent.COLUMN_NAME_MINUTE, MatchEvent.COLUMN_NAME_PLAYER,
		    MatchEvent.COLUMN_NAME_TEAM };

	    c = qb.query(db, colonnes, MatchEvent.COLUMN_NAME_ID_SCORE_SHEET + "=" + scoreSheet.getId(), null, null,
		    null, MatchEvent.COLUMN_NAME_ID + " DESC");

	    if (c != null && c.getCount() > 0) {
		c.moveToFirst();
		do {
		    MatchEvent matchEvent = new MatchEvent();
		    matchEvent.setId(c.getLong(0));
		    matchEvent.setComment(c.getString(1));
		    matchEvent.setMatchTypeEvent(getMatchTypeEvent(c.getLong(2)));
		    matchEvent.setMinute(c.getInt(3));
		    matchEvent.setPlayer(c.getInt(4));
		    matchEvent.setTeam(Team.valueOf(c.getString(5)));
		    scoreSheet.addMatchEvents(matchEvent);
		} while (c.moveToNext());
	    }
	} finally {
	    if (c != null) {
		c.close();
	    }
	}
    }

    /**
     * Open all the match
     */
    public List<ScoreSheet> getListSavedMatch() {
	// Opens the database object in "read" mode
	SQLiteDatabase db = mOpenHelper.getReadableDatabase();

	// Constructs a new query builder and sets its table name
	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	qb.setTables(ScoreSheet.TABLE_NAME);

	List<ScoreSheet> scoresheets = new ArrayList<ScoreSheet>();
	Cursor c = null;
	try {
	    String[] colonnes = new String[] { ScoreSheet.COLUMN_NAME_ID, ScoreSheet.COLUMN_NAME_DATE,
		    ScoreSheet.COLUMN_NAME_ID_SPORT, ScoreSheet.COLUMN_NAME_SCORE_HOME,
		    ScoreSheet.COLUMN_NAME_SCORE_VISITOR };

	    c = qb.query(db, colonnes, null, null, null, null, Sport.COLUMN_NAME_ID + " DESC");

	    if (c != null && c.getCount() > 0) {
		c.moveToFirst();
		do {
		    ScoreSheet scoreSheet = new ScoreSheet();
		    scoreSheet.setId(c.getLong(0));
		    scoreSheet.setDate(c.getString(1));
		    scoreSheet.setSport(getSport(c.getLong(2)));
		    scoreSheet.setScoreHome(c.getLong(3));
		    scoreSheet.setScoreVisitor(c.getLong(4));
		    scoresheets.add(scoreSheet);
		} while (c.moveToNext());
	    }
	} finally {
	    if (c != null) {
		c.close();
	    }
	    db.close();
	}
	return scoresheets;
    }

    /**
     * @param id
     * @return Sport
     */
    public Sport getSport(Long id) {
	if (sportsLoad.isEmpty()) {
	    getAllSport();
	}
	for (Sport s : sportsLoad) {
	    if (s.getId().equals(id)) {
		return s;
	    }
	}
	return null;
    }

    /**
     * Queries the database and returns a cursor containing the sports.
     * 
     * @return sports
     */
    public List<Sport> getAllSport() {
	// Opens the database object in "read" mode
	SQLiteDatabase db = mOpenHelper.getReadableDatabase();

	// Constructs a new query builder and sets its table name
	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	qb.setTables(Sport.TABLE_NAME);

	List<Sport> sports = new ArrayList<Sport>();
	Map<Long, List<MatchTypeEvent>> matchTypeEventsSport = getSportMatchTypeEvent(db);

	Cursor c = null;
	try {
	    c = qb.query(db, // The database to query
		    new String[] { Sport.COLUMN_NAME_ID, Sport.COLUMN_NAME_LABEL }, null, null, // The
												// values
												// for
												// the
												// where
												// clause
		    null, // don't group the rows
		    null, // don't filter by row groups
		    Sport.COLUMN_NAME_LABEL // The sort order
	    );

	    if (c != null && c.getCount() > 0) {
		c.moveToFirst();
		do {
		    Sport sport = new Sport();
		    sport.setId(c.getLong(0));
		    sport.setLabel(c.getString(1));
		    List<MatchTypeEvent> types = matchTypeEventsSport.get(sport.getId());
		    if (types != null && !types.isEmpty()) {
			for (MatchTypeEvent type : types) {
			    sport.addMatchTypesEvents(type);
			}
		    }
		    sports.add(sport);
		} while (c.moveToNext());
	    }
	} finally {
	    if (c != null) {
		c.close();
	    }
	    db.close();
	}
	if (sports.isEmpty()) {
	    // If no sport is present in the database we initialize 2 sports
	    initData();
	    sports.addAll(getAllSport());
	}
	sportsLoad.addAll(sports);
	return sports;
    }

    /**
     * Queries the database and returns a cursor containing the MatchTypeEvent.
     * 
     * @return A cursor containing the results of the query. The cursor exists
     *         but is empty if the query returns no results or an exception
     *         occurs.
     */
    protected Map<Long, List<MatchTypeEvent>> getSportMatchTypeEvent(SQLiteDatabase db) {
	// Constructs a new query builder and sets its table name
	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	qb.setTables(MatchTypeEvent.TABLE_NAME);

	Map<Long, List<MatchTypeEvent>> matchTypeEventsSport = new HashMap<Long, List<MatchTypeEvent>>();
	Cursor c = null;
	try {
	    String[] colonnes = new String[] { MatchTypeEvent.COLUMN_NAME_ID, MatchTypeEvent.COLUMN_NAME_LABEL,
		    MatchTypeEvent.COLUMN_NAME_ID_SPORT, MatchTypeEvent.COLUMN_NAME_SCORE };

	    c = qb.query(db, colonnes, null, null, null, null, MatchTypeEvent.COLUMN_NAME_ID);

	    if (c != null && c.getCount() > 0) {
		c.moveToFirst();
		do {
		    MatchTypeEvent typeEvent = new MatchTypeEvent();
		    typeEvent.setId(c.getLong(0));
		    typeEvent.setLabel(c.getString(1));
		    typeEvent.setScore(c.getInt(3));

		    List<MatchTypeEvent> types = matchTypeEventsSport.get(c.getLong(2));
		    if (types == null) {
			types = new ArrayList<MatchTypeEvent>();
			matchTypeEventsSport.put(c.getLong(2), types);
		    }
		    types.add(typeEvent);
		} while (c.moveToNext());
	    }
	} finally {
	    if (c != null) {
		c.close();
	    }
	}
	return matchTypeEventsSport;
    }
}
