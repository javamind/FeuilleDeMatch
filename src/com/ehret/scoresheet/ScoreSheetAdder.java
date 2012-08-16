package com.ehret.scoresheet;

import java.util.Date;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockActivity;
import com.ehret.scoresheet.adapter.SpinnerCustomAdapter;
import com.ehret.scoresheet.database.ScoreSheetDatabase;
import com.ehret.scoresheet.domain.ScoreSheet;
import com.ehret.scoresheet.domain.Sport;

/**
 * Create a new score sheet
 * 
 * @author ehret_g
 * 
 */
public class ScoreSheetAdder extends SherlockActivity {
    public static final String TAG = "ScoreSheetAdder";
    
    private EditText dateEditText;
    private Spinner sportSpinner;
    private Button saveButton;
    private ScoreSheet scoreSheet;
    private ScoreSheetDatabase scoreSheetDatabase;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	Log.v(TAG, "Activity State: onCreate()");

	super.onCreate(savedInstanceState);
	setContentView(R.layout.addmatch);

	scoreSheet = new ScoreSheet();

	// Handles sur les objets de la vue
	dateEditText = (EditText) findViewById(R.id.dateEditText);
	sportSpinner = (Spinner) findViewById(R.id.sportSpinner);
	saveButton = (Button) findViewById(R.id.saveButton);
	
	// Default date is now
	String currentDateTimeString = DateFormat.getDateFormat(this).format(new Date());
	dateEditText.setText(currentDateTimeString);

	// List of sports
	if(scoreSheetDatabase==null){
	    scoreSheetDatabase = new ScoreSheetDatabase(getBaseContext());
	}
	SpinnerCustomAdapter<Sport> adapter = new SpinnerCustomAdapter<Sport>(this, android.R.layout.simple_spinner_item,
		scoreSheetDatabase.getAllSport());
	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	sportSpinner.setAdapter(adapter);
	
	// Ajout d'un listener sur le bouton pour traiter le click dessus
	saveButton.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		Log.v(TAG, "saveMatch");
		saveMatch();
	    }
	});
    }

    /**
     * Ajoute la partie menu contextuel
     */
    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
	getSupportMenuInflater().inflate(R.menu.menumatchadder, menu);
	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
	// Handle item selection
	if (item.getItemId() == R.id.openMatch){
	    Intent intent = new Intent(this, ScoreSheetOpener.class);
	    startActivity(intent);
	    finish();
	}
	return true;
    }
    
    /**
     * M�thode permettant de persister le fait de match saisi par l'utilisateur
     */
    private void saveMatch() {
	// Recup�ration des valeurs saisies
	scoreSheet.setDate(dateEditText.getText().toString());
	scoreSheet.setSport((Sport)sportSpinner.getSelectedItem());

	SharedPreferences settings = getSharedPreferences(MatchEventAdder.PREFS_NAME, 0);
	SharedPreferences.Editor editor = settings.edit();
	editor.putLong("minute", 0);
	editor.commit();
	
	// Appel de la feuille principale
	Intent intent = new Intent(this, ScoreSheetActivity.class);
	intent.putExtra(ScoreSheetActivity.PARAM_SCORE_SHEET, scoreSheet);
	this.setResult(RESULT_OK, intent);
	startActivity(intent);
	finish();
    }

   
}