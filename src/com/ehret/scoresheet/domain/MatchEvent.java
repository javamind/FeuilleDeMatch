package com.ehret.scoresheet.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Event which could occur in a match
 * @author ehret_g
 *
 */
public class MatchEvent implements Parcelable, ISpinnerData{
    /**
     * Table name
     */
    public static final String TABLE_NAME = "event";
    /**
     * Id (type : Long) {@link #id}
     */
    public static final String COLUMN_NAME_ID = "id";
    /**
     * Team (type : String) {@link #team}
     */
    public static final String COLUMN_NAME_TEAM = "team";
    /**
     * Id type event (type : Long) {@link MatchTypeEvent#id}
     */
    public static final String COLUMN_NAME_ID_TYPE_EVENT = "id_type_event";
    /**
     * Id score sheet (type : Long) {@link ScoreSheet#id}
     */
    public static final String COLUMN_NAME_ID_SCORE_SHEET = "id_score_sheet";
    /**
     * Minute (type : Integer) {@link #minute}
     */
    public static final String COLUMN_NAME_MINUTE = "minute";
    /**
     * Player (type : Integer) {@link #player}
     */
    public static final String COLUMN_NAME_PLAYER = "player";
    /**
     * Comment (type : String) {@link #comment}
     */
    public static final String COLUMN_NAME_COMMENT = "comment";
    /**
     * Id
     */
    private Long id;
    /**
     * Team
     */
    private Team team;
    /**
     * Type of event 
     */
    private MatchTypeEvent matchTypeEvent;
    /**
     * Minute when the event occurs
     */
    private Integer minute;
    /**
     * Player who's linked with the event (in the future we could manage
     * the  the player list for each teams)
     */
    private Integer player;
    /**
     * Comment
     */
    private String comment;
    
    /**
     * Constructor
     * @param id
     * @param team
     * @param matchTypeEvent
     * @param minute
     */
    public MatchEvent(Long id, Team team, MatchTypeEvent matchTypeEvent, Integer minute, Integer player) {
	super();
	this.id = id;
	this.team = team;
	this.matchTypeEvent = matchTypeEvent;
	this.minute = minute;
	this.player=player;
    }
    
    /**
     * Constructor
     */
    public MatchEvent() {
	super();
    }

    /**
     * 
     * Constructor to use when re-constructing object from a parcel
     * 
     * @param in a parcel from which to read this object
     * @see this.writeToParcel
     */
    public MatchEvent(Parcel in) {
	//We read in the order we wrote in the parcel
	id = in.readLong();
	matchTypeEvent = in.readParcelable(MatchTypeEvent.class.getClassLoader());
	minute = in.readInt();
	player = in.readInt();
	team = Team.valueOf(in.readString());
	comment = in.readString();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }
    
    @Override
    public String getLabel() {
	return new StringBuilder().append(minute)
		.append(": " )
		.append(" [" ).append(team.name()).append("] ")
		.append(matchTypeEvent.getLabel())
		.append(player!=null ? " (" + player + ") " : "")
		.toString();
    }
    
    public void setTeam(Team team) {
        this.team = team;
    }

    public Integer getPlayer() {
        return player;
    }

    public void setPlayer(Integer player) {
        this.player = player;
    }
    
    public MatchTypeEvent getMatchTypeEvent() {
        return matchTypeEvent;
    }

    public void setMatchTypeEvent(MatchTypeEvent matchTypeEvent) {
        this.matchTypeEvent = matchTypeEvent;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((team == null) ? 0 : team.hashCode());
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	result = prime * result + ((minute == null) ? 0 : minute.hashCode());
	result = prime * result + ((matchTypeEvent == null) ? 0 : matchTypeEvent.hashCode());
	result = prime * result + ((player == null) ? 0 : player.hashCode());
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
	MatchEvent other = (MatchEvent) obj;
	if (team == null) {
	    if (other.team != null)
		return false;
	} else if (!team.equals(other.team))
	    return false;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	if (minute == null) {
	    if (other.minute != null)
		return false;
	} else if (!minute.equals(other.minute))
	    return false;
	if (matchTypeEvent == null) {
	    if (other.matchTypeEvent != null)
		return false;
	} else if (!matchTypeEvent.equals(other.matchTypeEvent))
	    return false;
	if (player == null) {
	    if (other.player != null)
		return false;
	} else if (!player.equals(other.player))
	    return false;
	return true;
    }
    
    @Override
    public int describeContents() {
	return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
	//we write each fieds in the parcel
	//the order is important when you read the information
	dest.writeLong(id);
	dest.writeParcelable(matchTypeEvent, flags);
	dest.writeInt(minute);
	dest.writeInt(player);
	dest.writeString(team.name());
	dest.writeString(comment);
    }
    
    /**
    * Classes implementing the Parcelable interface must also have a 
    * static field called CREATOR, which is an object implementing the 
    * Parcelable.Creator interface.
    *
    */
   public static final Parcelable.Creator<MatchEvent> CREATOR = new Parcelable.Creator<MatchEvent>() {
       public MatchEvent createFromParcel(Parcel in) {
           return new MatchEvent(in);
       }

       public MatchEvent[] newArray(int size) {
           return new MatchEvent[size];
       }
   };

   

    
   
}
