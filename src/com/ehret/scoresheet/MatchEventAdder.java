package com.ehret.scoresheet;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.ehret.scoresheet.adapter.SpinnerCustomAdapter;
import com.ehret.scoresheet.domain.MatchEvent;
import com.ehret.scoresheet.domain.MatchTypeEvent;
import com.ehret.scoresheet.domain.Team;

public class MatchEventAdder extends SherlockActivity {
    public static final String TAG = "MatchEventAdder";
    public static final String PARAM_TEAM = "com.ehret.scoresheet.AddTeam";
    public static final String PARAM_SPORT_EVENTS = "com.ehret.scoresheet.SportEvents";
    public static final String PARAM_EVENT_SAVED = "com.ehret.scoresheet.SavedEvent";
    public static final String PARAM_EVENT_TO_UPDATE = "com.ehret.scoresheet.UpdateEvent";
    public static final String PARAM_EVENT_DELETED = "com.ehret.scoresheet.DeletedEvent";
    public static final String PREFS_NAME = "PreferenceMatchEventAdder";

    private TextView equipeTextView;
    private EditText joueurEditText;
    private EditText commentaireEditText;
    private EditText minuteEditText;
    private Spinner faitSpinner;
    private Button saveButton;
    private Button deleteButton;
    private MatchEvent matchEvent;
    private long compteur = 0;
    private long minute = -1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	Log.v(TAG, "Activity State: onCreate()");

	super.onCreate(savedInstanceState);
	setContentView(R.layout.addevent);

	// Handles sur les objets de la vue
	equipeTextView = (TextView) findViewById(R.id.equipeTextView);
	joueurEditText = (EditText) findViewById(R.id.joueurEditText);
	commentaireEditText = (EditText) findViewById(R.id.commentaireEditText);
	minuteEditText = (EditText) findViewById(R.id.MinuteEditText);
	faitSpinner = (Spinner) findViewById(R.id.faitSpinner);
	saveButton = (Button) findViewById(R.id.saveButton);
	deleteButton = (Button) findViewById(R.id.deleteButton);

	populateEquipeTextView();

	if (getIntent().getExtras() != null) {
	    matchEvent = getIntent().getExtras().getParcelable(PARAM_EVENT_TO_UPDATE);
	}

	if (getIntent().getExtras() != null) {
	    List<MatchTypeEvent> events = getIntent().getExtras().getParcelableArrayList(PARAM_SPORT_EVENTS);
	    SpinnerCustomAdapter<MatchTypeEvent> adapter = new SpinnerCustomAdapter<MatchTypeEvent>(this,
		    android.R.layout.simple_spinner_item, events);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    faitSpinner.setAdapter(adapter);

	    if (matchEvent != null) {
		deleteButton.setVisibility(View.VISIBLE);
		joueurEditText.setText(matchEvent.getPlayer() != null ? matchEvent.getPlayer().toString() : "");
		commentaireEditText.setText(matchEvent.getComment() != null ? matchEvent.getComment() : "");
		minuteEditText.setText(matchEvent.getMinute() != null ? matchEvent.getMinute().toString() : "");
		int position = adapter.getPosition(matchEvent.getMatchTypeEvent());
		faitSpinner.setSelection(position);
	    }
	}

	if (matchEvent == null) {
	    matchEvent = new MatchEvent();
	    deleteButton.setVisibility(View.INVISIBLE);
	}

	// Ajout d'un listener sur le bouton pour traiter le click dessus
	saveButton.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		Log.v(TAG, "saveFaitMarquant");
		saveEvent();
	    }
	});
	
	deleteButton.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		Log.v(TAG, "deleteFaitMarquant");
		confirmDeleteEvent();
	    }
	});
    }

    /**
     * Populate the name of the team with the paramter sent by the
     * ScoreSheetActivity
     * 
     * @return
     */
    private void populateEquipeTextView() {
	if (getIntent().getExtras() != null) {
	    String equipe = getIntent().getExtras().getString(PARAM_TEAM);
	    Log.d(TAG, "Equipe " + equipe);
	    switch (Team.valueOf(equipe)) {
	    case HOME:
		equipeTextView.setText(R.string.team_local);
		break;
	    case VISITOR:
		equipeTextView.setText(R.string.team_visitor);
		break;
	    }
	} else {
	    equipeTextView.setText(R.string.team_inconnue);
	}
    }

    /**
     * M�thode permettant de supprimer le fait marquant 
     */
    private void confirmDeleteEvent() {
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setMessage(R.string.message_delete)
	       .setCancelable(false)
	       .setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               deleteEvent();
	           }
	       })
	       .setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               dialog.cancel();
	           }
	       });
	AlertDialog alert =builder.create();
	alert.show(); 
	    
    }
    /**
     * M�thode permettant de supprimer le fait marquant 
     */
    private void deleteEvent() {
	Intent intent = this.getIntent();
	intent.putExtra(PARAM_EVENT_DELETED, matchEvent);
	this.setResult(RESULT_OK, intent);
	finish();    
    }
    
    /**
     * M�thode permettant de persister le fait de match saisi par l'utilisateur
     */
    private void saveEvent() {
	// Read the user values
	if (matchEvent.getId() == null) {
	    matchEvent.setId(++compteur);
	}

	try {
	    matchEvent.setPlayer(Integer.valueOf(joueurEditText.getText().toString()));
	} catch (Exception e) {
	    matchEvent.setPlayer(0);
	}
	try {
	    matchEvent.setMinute(Integer.valueOf(minuteEditText.getText().toString()));
	    minute = matchEvent.getMinute();
	} catch (Exception e) {
	    matchEvent.setMinute(0);
	}
	matchEvent.setComment(commentaireEditText.getText().toString());
	matchEvent.setTeam(equipeTextView.getText().toString().equals(getString(R.string.team_visitor)) ? Team.VISITOR
		: Team.HOME);
	matchEvent.setMatchTypeEvent((MatchTypeEvent) faitSpinner.getSelectedItem());

	// and sent it to the parent
	Intent intent = this.getIntent();
	intent.putExtra(PARAM_EVENT_SAVED, matchEvent);
	this.setResult(RESULT_OK, intent);
	finish();
    }

    @Override
    protected void onStop() {
	super.onStop();
	// We need an Editor object to make preference changes.
	// All objects are from android.context.Context
	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	SharedPreferences.Editor editor = settings.edit();
	editor.putLong("compteur", compteur);
	editor.putLong("minute", minute);
	editor.commit();
    }

    @Override
    protected void onStart() {
	super.onStart();
	// Restore preferences
	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	compteur = settings.getLong("compteur", 0L);
	minute = settings.getLong("minute", -1);

	minuteEditText.setText(matchEvent.getMinute() != null ? matchEvent.getMinute().toString()
		: Long.valueOf(minute + 1).toString());
    }

    protected MatchEvent getMatchEvent() {
	return matchEvent;
    }

    protected void setMatchEvent(MatchEvent matchEvent) {
	this.matchEvent = matchEvent;
    }
}