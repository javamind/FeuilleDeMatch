package com.ehret.scoresheet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.ehret.scoresheet.adapter.ListViewImageMatchEventAdapter;
import com.ehret.scoresheet.database.ScoreSheetDatabase;
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
public class ScoreSheetActivity extends SherlockActivity {
    public static final String TAG = "ScoreSheetActivity";
    public static final int RESULT_ADDER_MATCHEVENT = 0;
    public static final String PARAM_SCORE_SHEET = "com.ehret.scoresheet";

    private TextView scoreLocalTextView;
    private TextView scoreVisitorTextView;
    private TextView sportTextView;
    private Button addEventLocauxButton;
    private Button addEventVisitorButton;
    private ScoreSheet scoreSheet;
    private ListView faitListView;
    private ScoreSheetDatabase scoreSheetDatabase;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	Log.v(TAG, "Activity State: onCreate()");

	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
		launchMatchEventAdder(null, Team.HOME);
	    }
	});
	addEventVisitorButton.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		Log.d(TAG, "addEventVisitorButton clicked");
		launchMatchEventAdder(null, Team.VISITOR);
	    }
	});

	// Ajout aussi d'un event sur la liste pour les modifs des temps forts
	faitListView.setClickable(true);
	faitListView.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		launchMatchEventAdder((MatchEvent) faitListView.getItemAtPosition(position), null);
	    }
	});

	// Dans le cas ou aucun match n'existe il faut renseigner les paramètres
	if (getIntent().getExtras() != null) {
	    scoreSheet = getIntent().getExtras().getParcelable(PARAM_SCORE_SHEET);
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
	startActivityForResult(i, RESULT_ADDER_MATCHEVENT);
    }

    /**
     * Lance la fonction de suppression d'un match
     */
    protected void launchScoreSheetDeleter() {
	//Confirmation
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setMessage(R.string.message_suppression).setCancelable(false)
		.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int id) {
			getScoreSheetDatabase().deleteMatch(scoreSheet);
			launchScoreSheetAdder();
		    }
		}).setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int id) {
			//On ne fait rien
		    }
		});
	AlertDialog alert = builder.create();
	alert.show();
	
    }
    
    /**
     * Lance la fonction d'envoi de mail
     */
    protected void sendScoreSheetByMail() {
	final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
	emailIntent.setType("plain/text");
	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Feuille de match " + scoreSheet.getDate());
	
	StringBuffer buffer = new StringBuffer();
	buffer.append(Team.HOME.toString())
		.append(" : ")
		.append(scoreSheet.getScoreHome())
		.append("\n")
		.append(Team.VISITOR.toString())
		.append(" : ")
		.append(scoreSheet.getScoreVisitor())
		.append("\n\n")
		.append("Liste des événements du match");
	for(MatchEvent m :scoreSheet.getMatchEvents()){
	    buffer.append(" * ").append(m.getLabel()).append("\n");
	}
	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,buffer.toString());
	startActivity(Intent.createChooser(emailIntent, "Envoi email..."));
    }
    
    /**
     * Lance la fonction d'ajout d'une nouvelle feuille de match
     */
    protected void launchScoreSheetAdder() {
	Intent i = new Intent(this, ScoreSheetAdder.class);
	startActivity(i);
    }

    /**
     * Retourne la ScoreSheetDatabase
     * 
     * @return
     */
    private ScoreSheetDatabase getScoreSheetDatabase() {
	if (scoreSheetDatabase == null) {
	    scoreSheetDatabase = new ScoreSheetDatabase(getBaseContext());
	}
	return scoreSheetDatabase;
    }

    /**
     * Save the score sheet and all the events
     */
    protected void launchScoreSheetSaver() {
	try {
	    getScoreSheetDatabase().saveMatch(this.scoreSheet);
	    Toast.makeText(this, R.string.message_savedOK, Toast.LENGTH_SHORT).show();
	} catch (SQLException e) {
	    Log.d(TAG, "error when the match was saved", e);
	    // A message is displayed to the user
	    Toast.makeText(this, R.string.message_savedKO, Toast.LENGTH_SHORT).show();
	}
    }

    /**
     * Save the score sheet and all the events
     */
    protected void launchScoreSheetOpener() {
	Intent i = new Intent(this, ScoreSheetOpener.class);
	startActivity(i);
	finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	if (data != null) {
	    switch (requestCode) {
	    case RESULT_ADDER_MATCHEVENT:
		// event deleted ?
		MatchEvent eventdeleted = data.getParcelableExtra(MatchEventAdder.PARAM_EVENT_DELETED);
		if (eventdeleted != null) {
		    scoreSheet.removePointsTeam(eventdeleted.getTeam(), eventdeleted.getMatchTypeEvent().getScore()
			    .longValue());
		    scoreSheet.removeMatchEvents(eventdeleted);
		} else {
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

	    // ListViewCustomAdapter<MatchEvent> adapter = new
	    // ListViewCustomAdapter<MatchEvent>(this,
	    // R.id.faitListView,
	    // android.R.layout.simple_list_item_1,
	    // scoreSheet.getMatchEvents());
	    // faitListView.setAdapter(adapter);
	    ListViewImageMatchEventAdapter adapter = new ListViewImageMatchEventAdapter(this,
		    scoreSheet.getMatchEvents());
	    faitListView.setAdapter(adapter);

	} else {
	    launchScoreSheetOpener();
	}
	super.onResume();
    }

    /**
     * Ajoute la partie menu contextuel
     */
    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.menu, menu);
	getSupportMenuInflater().inflate(R.menu.menu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
	// Handle item selection
	switch (item.getItemId()) {
	case R.id.newMatch:
	    launchScoreSheetAdder();
	    return true;
	case R.id.saveMatch:
	    launchScoreSheetSaver();
	    return true;
	case R.id.openMatch:
	    launchScoreSheetOpener();
	    return true;
	case R.id.deleteMatch:
	    launchScoreSheetDeleter();
	    return true;
	case R.id.shareMatch:
	    sendScoreSheetByMail();
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    @Override
    protected void onStop() {
	super.onStop();
    }

    @Override
    public void onBackPressed() {
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setMessage(R.string.message_sortie).setCancelable(false)
		.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int id) {
			finish();
		    }
		}).setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int id) {
			dialog.cancel();
		    }
		});
	AlertDialog alert = builder.create();
	alert.show();
    }

}