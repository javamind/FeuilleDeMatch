package com.ehret.scoresheet.builder;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.ehret.scoresheet.R;
import com.ehret.scoresheet.domain.MatchTypeEvent;
import com.ehret.scoresheet.domain.Sport;

/**
 * In the first version the parameters aren't in a DBMS.
 * This builder help to build them
 * @author ehret_g
 *
 */
public class ParameterBuilder {
    
    /**
     * Construct the list of the sports
     * @return
     */
    public static List<Sport> buildSport(Context context){
	List<Sport> sports = new ArrayList<Sport>();
	
	Sport rugby13 = new Sport(1L, context.getString(R.string.param_rugby13));
	rugby13.setMatchTypesEvents(buildMatchTypeEvent(context, rugby13.getId()));
	sports.add(rugby13);
	
	Sport rugby15 = new Sport(2L, context.getString(R.string.param_rugby15));
	rugby15.setMatchTypesEvents(buildMatchTypeEvent(context, rugby15.getId()));
	sports.add(rugby15);
	
	Sport basket = new Sport(3L, context.getString(R.string.param_basket));
	basket.setMatchTypesEvents(buildMatchTypeEvent(context, basket.getId()));
	sports.add(basket);
	
	Sport foot = new Sport(4L, context.getString(R.string.param_foot));
	foot.setMatchTypesEvents(buildMatchTypeEvent(context, foot.getId()));
	sports.add(foot);
	
	return sports;
    }
    
    /**
     * Construct the list of the sports MatchTypeEvents
     * @return
     */
    public static List<MatchTypeEvent> buildMatchTypeEvent(Context context, Long cdsport){
	List<MatchTypeEvent> events = new ArrayList<MatchTypeEvent>();
	switch(cdsport.intValue()){
	case 1:
	    //Rugby a XIII
	    events.add(new MatchTypeEvent(1L, context.getString(R.string.param_debutmatch), 0));
	    events.add(new MatchTypeEvent(2L, context.getString(R.string.param_essai), 4));
	    events.add(new MatchTypeEvent(3L, context.getString(R.string.param_transformation), 2));
	    events.add(new MatchTypeEvent(4L, context.getString(R.string.param_penalite), 2));
	    events.add(new MatchTypeEvent(5L, context.getString(R.string.param_drop), 1));
	    events.add(new MatchTypeEvent(6L, context.getString(R.string.param_essai_penalite), 8));
	    events.add(new MatchTypeEvent(7L, context.getString(R.string.param_finmatch), 0));
	    break;
	case 2:
	    //Rugby a XV
	    events.add(new MatchTypeEvent(10L, context.getString(R.string.param_debutmatch), 0));
	    events.add(new MatchTypeEvent(11L, context.getString(R.string.param_essai), 5));
	    events.add(new MatchTypeEvent(12L, context.getString(R.string.param_transformation), 2));
	    events.add(new MatchTypeEvent(13L, context.getString(R.string.param_penalite), 3));
	    events.add(new MatchTypeEvent(14L, context.getString(R.string.param_drop), 3));
	    events.add(new MatchTypeEvent(15L, context.getString(R.string.param_essai_penalite), 5));
	    events.add(new MatchTypeEvent(16L, context.getString(R.string.param_finmatch), 0));
	    break;
	case 3:
	    //Basket
	    events.add(new MatchTypeEvent(20L, context.getString(R.string.param_debutmatch), 0));
	    events.add(new MatchTypeEvent(21L, context.getString(R.string.param_panier2), 2));
	    events.add(new MatchTypeEvent(22L, context.getString(R.string.param_panier3), 3));
	    events.add(new MatchTypeEvent(23L, context.getString(R.string.param_lancer1), 1));
	    events.add(new MatchTypeEvent(24L, context.getString(R.string.param_lancer2), 2));
	    events.add(new MatchTypeEvent(25L, context.getString(R.string.param_lancer3), 3));
	    events.add(new MatchTypeEvent(26L, context.getString(R.string.param_faute), 0));
	    events.add(new MatchTypeEvent(27L, context.getString(R.string.param_faute_off), 0));
	    events.add(new MatchTypeEvent(28L, context.getString(R.string.param_technique), 0));
	    events.add(new MatchTypeEvent(29L, context.getString(R.string.param_divers), 0));
	    events.add(new MatchTypeEvent(30L, context.getString(R.string.param_finmatch), 0));
	    break;
	case 4:
	    //Foot
	    events.add(new MatchTypeEvent(40L, context.getString(R.string.param_debutmatch), 0));
	    events.add(new MatchTypeEvent(41L, context.getString(R.string.param_but), 1));
	    events.add(new MatchTypeEvent(42L, context.getString(R.string.param_penalty), 0));
	    events.add(new MatchTypeEvent(43L, context.getString(R.string.param_faute), 0));
	    events.add(new MatchTypeEvent(44L, context.getString(R.string.param_carton_jaune), 0));
	    events.add(new MatchTypeEvent(45L, context.getString(R.string.param_carton_rouge), 0));
	    events.add(new MatchTypeEvent(46L, context.getString(R.string.param_divers), 0));
	    events.add(new MatchTypeEvent(47L, context.getString(R.string.param_finmatch), 0));
	}
	return events;
    }
}
