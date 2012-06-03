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
	
	return sports;
    }
    
    /**
     * Construct the list of the sports MatchTypeEvents
     * @return
     */
    public static List<MatchTypeEvent> buildMatchTypeEvent(Context context, Long cdsport){
	List<MatchTypeEvent> events = new ArrayList<MatchTypeEvent>();
	if(cdsport.equals(1L)){
	    //Rugby à XIII
	    events.add(new MatchTypeEvent(1L, context.getString(R.string.param_debutmatch), 0));
	    events.add(new MatchTypeEvent(2L, context.getString(R.string.param_essai), 4));
	    events.add(new MatchTypeEvent(3L, context.getString(R.string.param_transformation), 2));
	    events.add(new MatchTypeEvent(4L, context.getString(R.string.param_penalite), 2));
	    events.add(new MatchTypeEvent(5L, context.getString(R.string.param_drop), 1));
	    events.add(new MatchTypeEvent(6L, context.getString(R.string.param_essai_penalite), 8));
	    events.add(new MatchTypeEvent(99L, context.getString(R.string.param_finmatch), 0));
	}
	else if(cdsport.equals(2L)){
	    //Rugby à XV
	    events.add(new MatchTypeEvent(1L, context.getString(R.string.param_debutmatch), 0));
	    events.add(new MatchTypeEvent(2L, context.getString(R.string.param_essai), 5));
	    events.add(new MatchTypeEvent(3L, context.getString(R.string.param_transformation), 2));
	    events.add(new MatchTypeEvent(4L, context.getString(R.string.param_penalite), 3));
	    events.add(new MatchTypeEvent(5L, context.getString(R.string.param_drop), 3));
	    events.add(new MatchTypeEvent(6L, context.getString(R.string.param_essai_penalite), 5));
	    events.add(new MatchTypeEvent(99L, context.getString(R.string.param_finmatch), 0));
	}
	return events;
    }
}
