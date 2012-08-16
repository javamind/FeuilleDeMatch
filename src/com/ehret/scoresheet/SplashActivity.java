package com.ehret.scoresheet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Activity used to display a splash screen at the startup
 * @author ehret_g
 *
 */
public class SplashActivity extends Activity{
    
    private final static int DELAY = 3000;
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
    }
    
    /**
     * On g�re le traitement du splashscreen dans la m�thode onResume
     * pour g�rer le cas o� l'application est mis en pause par une autre
     * ou par le syst�me 
     */
    @Override
    protected void onResume(){
	super.onResume();
        //Un handler permet de lancer un traitement dans le futur
	new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //Finish the splash activity so it can't be returned to.
                SplashActivity.this.finish();
                // Create an Intent that will start the main activity.
                Intent mainIntent = new Intent(SplashActivity.this, ScoreSheetAdder.class);
                SplashActivity.this.startActivity(mainIntent);
            }
        } , DELAY);
    }
}
