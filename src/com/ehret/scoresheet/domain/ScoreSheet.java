package com.ehret.scoresheet.domain;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * a score sheet is specific for a sport and it has a set of 
 * Une feuille de match est liée à un sport et elle a un ensemble de faits
 * marquants s'étant déroulés au cours du match
 * @author ehret_g
 *
 */
public class ScoreSheet implements Parcelable{
    /**
     * Table name
     */
    public static final String TABLE_NAME = "score_sheet";
    /**
     * Id (type : Long) {@link #id}
     */
    public static final String COLUMN_NAME_ID = "id";
    /**
     * Date (type : String) {@link #date}
     */
    public static final String COLUMN_NAME_DATE = "date";
    /**
     * Id Sport (type : Lons) {@link #sport}
     */
    public static final String COLUMN_NAME_ID_SPORT = "id_sport";
    /**
     * Score team Home (type : Long) {@link #scoreHome}
     */
    public static final String COLUMN_NAME_SCORE_HOME = "scoreHome";
    /**
     * Score team Visitor (type : Long) {@link #scoreVisitor}
     */
    public static final String COLUMN_NAME_SCORE_VISITOR = "scoreVisitor";
    /**
     * Id
     */
    private Long id;
    /**
     * Date
     */
    private String date;
    /**
     * Sport
     */
    private Sport sport;
    /**
     * Match events
     */
    private List<MatchEvent> matchEvents;
    /**
     * Score for home team
     */
    private Long scoreHome;
    /**
     * Score for visitor team
     */
    private Long scoreVisitor;
    /**
     * Constructor
     * @param id
     * @param date
     * @param sport
     */
    public ScoreSheet(Long id, String date, Sport sport) {
	this();
	this.id = id;
	this.date = date;
	this.sport = sport;
    }
    
    /**
     * Constructor
     */
    public ScoreSheet() {
	super();
	matchEvents = new ArrayList<MatchEvent>();
	scoreVisitor = 0L;
	scoreHome = 0L;
    }
    
    /**
     * Constructor to use when re-constructing object from a parcel
     * 
     * @param in a parcel from which to read this object
     * @see this.writeToParcel
     */
    public ScoreSheet(Parcel in) {
	this();
	//We read in the order we wrote in the parcel
	id = in.readLong();
	date = in.readString();
	sport = in.readParcelable(Sport.class.getClassLoader());
	scoreHome = in.readLong();
	scoreVisitor = in.readLong();
	in.readTypedList(matchEvents, MatchEvent.CREATOR);
    }
    
    /**
     * Remove points of a team
     * @param team
     * @param points
     */
    public void removePointsTeam(Team team, Long points){
	if(team==Team.HOME){
	    this.scoreHome = this.scoreHome - points; 
	}
	else{
	    this.scoreVisitor = this.scoreVisitor -points; 
	}
    }
    /**
     * Add points of a team
     * @param team
     * @param points
     */
    public void addPointsTeam(Team team, Long points){
	if(team==Team.HOME){
	    this.scoreHome = this.scoreHome + points; 
	}
	else{
	    this.scoreVisitor = this.scoreVisitor + points; 
	}
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }
    
    public Long getScoreHome() {
        return scoreHome;
    }

    public void setScoreHome(Long scoreHome) {
        this.scoreHome = scoreHome;
    }

    public Long getScoreVisitor() {
        return scoreVisitor;
    }

    public void setScoreVisitor(Long score) {
        this.scoreVisitor = score;
    }
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((date == null) ? 0 : date.hashCode());
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	result = prime * result + ((sport == null) ? 0 : sport.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	ScoreSheet other = (ScoreSheet) obj;
	if (date == null) {
	    if (other.date != null)
		return false;
	} else if (!date.equals(other.date))
	    return false;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	if (sport == null) {
	    if (other.sport != null)
		return false;
	} else if (!sport.equals(other.sport))
	    return false;
	return true;
    }

    public List<MatchEvent> getMatchEvents() {
        return matchEvents;
    }

    public void setMatchEvents(List<MatchEvent> matchEvents) {
        this.matchEvents = matchEvents;
    }
    
    public List<MatchEvent> addMatchEvents(MatchEvent object) {
	matchEvents.add(object);
	return matchEvents;
    }

    public List<MatchEvent> removeMatchEvents(MatchEvent object) {
	matchEvents.remove(object);
	return matchEvents;
    }
    
    @Override
    public int describeContents() {
	return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
	//we write each fieds in the parcel
	//the order is important when you read the information
	dest.writeLong(id!=null ? id : 0);
	dest.writeString(date!=null ? date : "");
	dest.writeParcelable(sport, flags);
	dest.writeLong(scoreHome);
	dest.writeLong(scoreVisitor);
	dest.writeTypedList(matchEvents);
    }
    
    /**
    * Classes implementing the Parcelable interface must also have a 
    * static field called CREATOR, which is an object implementing the 
    * Parcelable.Creator interface.
    *
    */
   public static final Parcelable.Creator<ScoreSheet> CREATOR = new Parcelable.Creator<ScoreSheet>() {
       public ScoreSheet createFromParcel(Parcel in) {
           return new ScoreSheet(in);
       }

       public ScoreSheet[] newArray(int size) {
           return new ScoreSheet[size];
       }
   };
    

    
}
