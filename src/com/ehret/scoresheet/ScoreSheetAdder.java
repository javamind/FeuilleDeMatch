package com.ehret.scoresheet;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.ehret.scoresheet.adapter.SpinnerCustomAdapter;
import com.ehret.scoresheet.builder.ParameterBuilder;
import com.ehret.scoresheet.domain.ScoreSheet;
import com.ehret.scoresheet.domain.Sport;

/**
 * Create a new score sheet
 * 
 * @author ehret_g
 * 
 */
public class ScoreSheetAdder extends Activity {
    public static final String TAG = "ScoreSheetAdder";
    public static final String PARAM_SCORE_SHEET = "com.ehret.scoresheet.AddSheet";

    private EditText dateEditText;
    private Spinner sportSpinner;
    private Button saveButton;
    private ScoreSheet scoreSheet;

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
	SpinnerCustomAdapter<Sport> adapter = new SpinnerCustomAdapter<Sport>(this, android.R.layout.simple_spinner_item,
		ParameterBuilder.buildSport(this));
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
     * Méthode permettant de persister le fait de match saisi par l'utilisateur
     */
    private void saveMatch() {
	// Recupération des valeurs saisies
	scoreSheet.setDate(dateEditText.getText().toString());
	scoreSheet.setSport((Sport)sportSpinner.getSelectedItem());

	SharedPreferences settings = getSharedPreferences(MatchEventAdder.PREFS_NAME, 0);
	SharedPreferences.Editor editor = settings.edit();
	editor.putLong("minute", 0);
	editor.commit();
	
	// Preparation du resultat
	Intent intent = this.getIntent();
	intent.putExtra(PARAM_SCORE_SHEET, scoreSheet);
	this.setResult(RESULT_OK, intent);
	finish();
    }

}