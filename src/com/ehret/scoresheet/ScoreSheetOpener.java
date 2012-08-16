package com.ehret.scoresheet;

import java.util.List;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.ehret.scoresheet.adapter.ListViewImageScoreSheetAdapter;
import com.ehret.scoresheet.database.ScoreSheetDatabase;
import com.ehret.scoresheet.domain.ScoreSheet;

/**
 * Create a new score sheet
 * 
 * @author ehret_g
 * 
 */
public class ScoreSheetOpener extends SherlockActivity {
    public static final String TAG = "ScoreSheetOpener";
    private ListView listView;
    private ScoreSheetDatabase scoreSheetDatabase;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	Log.v(TAG, "Activity State: onCreate()");

	super.onCreate(savedInstanceState);
	setContentView(R.layout.openerlist);
	listView = (ListView) findViewById(R.id.list);
	
	// Ajout aussi d'un event sur la liste pour les modifs des temps forts
	listView.setClickable(true);
	listView.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		openScoreSheet((ScoreSheet) listView.getItemAtPosition(position));
	    }
	});
    }
    
    /**
     * 
     * @param scoreSheet
     */
    private void openScoreSheet(ScoreSheet scoreSheet){
	getScoreSheetDatabase().completeScoreSheet(scoreSheet);
	//Return to the main activity
	Intent intent = new Intent(this, ScoreSheetActivity.class);
	intent.putExtra(ScoreSheetActivity.PARAM_SCORE_SHEET, scoreSheet);
	startActivity(intent);
	finish();
    }
    
    /**
     * @return ScoreSheetDatabase
     */
    private ScoreSheetDatabase getScoreSheetDatabase(){
	if(scoreSheetDatabase==null){
	    scoreSheetDatabase = new ScoreSheetDatabase(getBaseContext());
	}
	return scoreSheetDatabase;   
    }
    
    /** Called after one start, restart or pause
     */
    @Override
    protected void onResume() {
	super.onResume();
	//Load the enreg
	try{
	    List<ScoreSheet> scoreSheets = getScoreSheetDatabase().getListSavedMatch();
	    
	    if(scoreSheets.isEmpty()){
		Toast.makeText(this, R.string.message_noScoreSheetInDatabase, Toast.LENGTH_SHORT).show();
	    }
	    else{
		ListViewImageScoreSheetAdapter adapter = new ListViewImageScoreSheetAdapter(this,scoreSheets);  	   
		listView.setAdapter(adapter);		
	    }
	}
	catch(SQLException e){
	    Log.d(TAG, "error when the list of the matchs was load",e);
    	    //A message is displayed to the user
            Toast.makeText(this, R.string.message_errorLoadMatches, Toast.LENGTH_SHORT).show();
	}
    }

    
    
    
    
}