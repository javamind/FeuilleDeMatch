package com.ehret.scoresheet.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A type of event which could occur in a match. It's linked with a sport
 * and in a match an event can have a number of points and impact the score 
 * 
 * @author ehret_g
 * 
 */
public class MatchTypeEvent implements Parcelable,ISpinnerData {
    /**
     * Table name
     */
    public static final String TABLE_NAME = "type_event";
    /**
     * Id (type : Long) {@link #id}
     */
    public static final String COLUMN_NAME_ID = "id";
    /**
     * Label (type : String) {@link #label}
     */
    public static final String COLUMN_NAME_LABEL = "label";
    /**
     * Point (type : Long) {@link #score}
     */
    public static final String COLUMN_NAME_SCORE = "score";
    /**
     * Id sport (type : Long) {@link Sport#id}
     */
    public static final String COLUMN_NAME_ID_SPORT = "id_sport";
    /**
     * Id
     */
    private Long id;
    /**
     * Llabel
     */
    private String label;

    /**
     * Score
     */
    private Integer score;

    /**
     * Constructor
     */
    public MatchTypeEvent() {
	super();
    }

    /**
     * 
     * @param id
     * @param label
     * @param sport
     * @param score
     */
    public MatchTypeEvent(Long id, String label, Integer score) {
	super();
	this.id = id;
	this.label = label;
	this.score = score;
    }

    /**
     * 
     * Constructor to use when re-constructing object from a parcel
     * 
     * @param in a parcel from which to read this object
     * @see this.writeToParcel
     */
    public MatchTypeEvent(Parcel in) {
	//We read in the order we wrote in the parcel
	id = in.readLong();
	label = in.readString();
	score = in.readInt();
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

    public Integer getScore() {
	return score;
    }

    public void setScore(Integer score) {
	this.score = score;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((label == null) ? 0 : label.hashCode());
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
	MatchTypeEvent other = (MatchTypeEvent) obj;
	if (label == null) {
	    if (other.label != null)
		return false;
	} else if (!label.equals(other.label))
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
	dest.writeString(label);
	dest.writeInt(score);
    }
    
    /**
    * Classes implementing the Parcelable interface must also have a 
    * static field called CREATOR, which is an object implementing the 
    * Parcelable.Creator interface.
    *
    */
   public static final Parcelable.Creator<MatchTypeEvent> CREATOR = new Parcelable.Creator<MatchTypeEvent>() {
       public MatchTypeEvent createFromParcel(Parcel in) {
           return new MatchTypeEvent(in);
       }

       public MatchTypeEvent[] newArray(int size) {
           return new MatchTypeEvent[size];
       }
   };
}
