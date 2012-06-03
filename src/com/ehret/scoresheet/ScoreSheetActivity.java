package com.ehret.scoresheet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ehret.scoresheet.adapter.ListViewCustomAdapter;
import com.ehret.scoresheet.domain.MatchEvent;
import com.ehret.scoresheet.domain.MatchTypeEvent;
import com.ehret.scoresheet.domain.ScoreSheet;
import com.ehret.scoresheet.domain.Team;

/**
 * Classe principale de l'appplication permettant d'afficher la feuille de match
 * 
 * @author ehret_g
 * 
 */
public class ScoreSheetActivity extends Activity {
    public static final String TAG = "ScoreSheetActivity";
    public static final int ADD_MATCHEVENT_REQUEST = 0;
    public static final int ADD_SCORESHEET_EVENT = 1;

    private TextView scoreLocalTextView;
    private TextView scoreVisitorTextView;
    private TextView sportTextView;
    private Button addEventLocauxButton;
    private Button addEventVisitorButton;
    private ScoreSheet scoreSheet;
    private ListView faitListView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	Log.v(TAG, "Activity State: onCreate()");

	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	// Recuperation des handlers sur les objets
	scoreLocalTextView = (TextView) findViewById(R.id.scoreLocalTextView);
	scoreVisitorTextView = (TextView) findViewById(R.id.scoreVisitorTextView);
	sportTextView = (TextView) findViewById(R.id.sportTextView);
	faitListView = (ListView) findViewById(R.id.faitListView);
	addEventLocauxButton = (Button) findViewById(R.id.addEventLocauxButton);
	addEventVisitorButton = (Button) findViewById(R.id.addEventVisitorButton);

	// Ajout des listners sur les boutons ajouts faits de matchs
	addEventLocauxButton.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		Log.d(TAG, "addEventLocauxButton clicked");
		launchMatchEventAdder(null,Team.HOME);
	    }
	});
	addEventVisitorButton.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		Log.d(TAG, "addEventVisitorButton clicked");
		launchMatchEventAdder(null,Team.VISITOR);
	    }
	});

	// Ajout aussi d'un event sur la liste pour les modifs des temps forts
	faitListView.setClickable(true);
	faitListView.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		launchMatchEventAdder((MatchEvent) faitListView.getItemAtPosition(position),null);
	    }
	});

	// Dans le cas ou aucun match n'existe il faut renseigner les paramètres
	// nécessaires
	if (scoreSheet != null) {
	    sportTextView.setText(scoreSheet.getSport().getLabel());
	}
    }

    /**
     * Lance la fonction d'ajout d'un fait marquant à un match
     */
    protected void launchMatchEventAdder(MatchEvent matchEvent, Team team) {
	Intent i = new Intent(this, MatchEventAdder.class);
	if (matchEvent != null) {
	    // the event to update
	    i.putExtra(MatchEventAdder.PARAM_EVENT_TO_UPDATE, matchEvent);
	    // The type of the team is a parameter
	    i.putExtra(MatchEventAdder.PARAM_TEAM, matchEvent.getTeam().name());
	} else {
	    i.putExtra(MatchEventAdder.PARAM_TEAM, team.name());
	}
	i.putParcelableArrayListExtra(MatchEventAdder.PARAM_SPORT_EVENTS, (ArrayList<MatchTypeEvent>) scoreSheet
		.getSport().getMatchTypesEvents());
	startActivityForResult(i, ADD_MATCHEVENT_REQUEST);
    }

    /**
     * Lance la fonction d'ajout d'une nouvelle feuille de match
     */
    protected void launchScoreSheetAdder() {
	Intent i = new Intent(this, ScoreSheetAdder.class);
	startActivityForResult(i, ADD_SCORESHEET_EVENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	if (data != null) {
	    switch (requestCode) {
	    case ADD_MATCHEVENT_REQUEST:
		//event deleted ?
		MatchEvent eventdeleted = data.getParcelableExtra(MatchEventAdder.PARAM_EVENT_DELETED);
		if(eventdeleted!=null){
		    scoreSheet.removePointsTeam(eventdeleted.getTeam(), eventdeleted.getMatchTypeEvent().getScore().longValue());
		    scoreSheet.removeMatchEvents(eventdeleted);
		}
		else{
        		MatchEvent eventdefined = data.getParcelableExtra(MatchEventAdder.PARAM_EVENT_SAVED);
        		if (resultCode == RESULT_OK && eventdefined != null) {
        		    // If the event is already define the score must be
        		    // corrected
        		    for (MatchEvent e : scoreSheet.getMatchEvents()) {
        			if (e.getId().equals(eventdefined.getId())) {
        			    // We substract the old score before the update
        			    scoreSheet.removePointsTeam(e.getTeam(), e.getMatchTypeEvent().getScore().longValue());
        			    scoreSheet.removeMatchEvents(e);
        			    break;
        			}
        		    }
        		    // Update the score
        		    scoreSheet.addPointsTeam(eventdefined.getTeam(), eventdefined.getMatchTypeEvent().getScore()
        			    .longValue());
        		    scoreSheet.addMatchEvents(eventdefined);
        		}
		}
		break;
	    case ADD_SCORESHEET_EVENT:
		if (resultCode == RESULT_OK && data.getParcelableExtra(ScoreSheetAdder.PARAM_SCORE_SHEET) != null) {
		    Log.d(TAG, "onActivityResult add scoresheet");
		    scoreSheet = data.getParcelableExtra(ScoreSheetAdder.PARAM_SCORE_SHEET);
		}
		break;
	    }
	}
    }

    @Override
    protected void onResume() {
	if (scoreSheet != null) {
	    sportTextView.setText(scoreSheet.getSport().getLabel());
	    scoreLocalTextView.setText(scoreSheet.getScoreHome() + "");
	    scoreVisitorTextView.setText(scoreSheet.getScoreVisitor() + "");

	    // Update the ListView
	    Collections.sort(scoreSheet.getMatchEvents(), new Comparator<MatchEvent>() {
		@Override
		public int compare(MatchEvent lhs, MatchEvent rhs) {
		    return rhs.getMinute().compareTo(lhs.getMinute());
		}
	    });
	    ListViewCustomAdapter<MatchEvent> adapter = 
			new ListViewCustomAdapter<MatchEvent>(
				this, 
				//R.id.faitListView,
				android.R.layout.simple_list_item_1,
				scoreSheet.getMatchEvents());
	    faitListView.setAdapter(adapter);
	    
	} else {
	    launchScoreSheetAdder();
	}
	super.onResume();
    }

    /**
     * Ajoute la partie menu contextuel
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.menu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle item selection
	switch (item.getItemId()) {
	case R.id.newMatch:
	    launchScoreSheetAdder();
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

}