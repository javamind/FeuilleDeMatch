package com.ehret.scoresheet.domain;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author ehret_g
 *
 */
public class Sport implements Parcelable, ISpinnerData {
    /**
     * a sport has an id
     */
    private Long id;
    /**
     * and a label
    */
    private String label;
    /**
     * Liste des faits marquants associés à un sport 
     * pouvant être ajoutés à une feuille de match
     */
    private List<MatchTypeEvent> matchTypesEvents;
    
    /**
     * Constructor   
     */
    public Sport() {
	super();
	matchTypesEvents = new ArrayList<MatchTypeEvent>();
    }
    
    /**
     * Constructor
     * @param code
     * @param label
     */
    public Sport(Long id, String label) {
	this();
	this.id = id;
	this.label = label;
    }
    
    /**
     * Constructor to use when re-constructing object from a parcel
     * 
     * @param in a parcel from which to read this object
     * @see this.writeToParcel
     */
    public Sport(Parcel in) {
	this();
	//We read in the order we wrote in the parcel
	id = in.readLong();
	label = in.readString();
	in.readTypedList(matchTypesEvents, MatchTypeEvent.CREATOR);
    }
    
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
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
	Sport other = (Sport) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	if (label == null) {
	    if (other.label != null)
		return false;
	} else if (!label.equals(other.label))
	    return false;
	return true;
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

    public List<MatchTypeEvent> getMatchTypesEvents() {
        return matchTypesEvents;
    }

    public void setMatchTypesEvents(List<MatchTypeEvent> faitsmarquants) {
        this.matchTypesEvents = faitsmarquants;
    }
    
    public List<MatchTypeEvent> addMatchTypesEvents(MatchTypeEvent object) {
	matchTypesEvents.add(object);
	return matchTypesEvents;
    }

    public List<MatchTypeEvent> removeMatchTypesEvents(MatchTypeEvent object) {
	matchTypesEvents.remove(object);
	return matchTypesEvents;
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
	dest.writeTypedList(matchTypesEvents);
    }
    
    /**
    * Classes implementing the Parcelable interface must also have a 
    * static field called CREATOR, which is an object implementing the 
    * Parcelable.Creator interface.
    *
    */
   public static final Parcelable.Creator<Sport> CREATOR = new Parcelable.Creator<Sport>() {
       public Sport createFromParcel(Parcel in) {
           return new Sport(in);
       }

       public Sport[] newArray(int size) {
           return new Sport[size];
       }
   };
}
