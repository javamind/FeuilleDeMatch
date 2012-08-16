package com.ehret.scoresheet.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ehret.scoresheet.R;
import com.ehret.scoresheet.domain.MatchEvent;
import com.ehret.scoresheet.domain.Team;

/**
 * Diplay the result in a list with an image on the left
 * 
 * @author ehret_g
 * 
 * @param <T>
 */
public class ListViewImageMatchEventAdapter extends BaseAdapter {
    private List<MatchEvent> items;
    private Context context;
    
    public ListViewImageMatchEventAdapter(Context context, List<MatchEvent> results) {
	this.items = results;
	this.context=context;
    }
    
    public int getCount() {
	return items.size();
    }

    public MatchEvent getItem(int position) {
	return items.get(position);
    }

    public long getItemId(int position) {
	return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder;
	if (convertView == null) {
	    convertView = LayoutInflater.from(context).inflate(R.layout.matcheventitem, null);
	    holder = new ViewHolder();
	    holder.itemName = (TextView) convertView.findViewById(R.id.name);
	    holder.itemComment = (TextView) convertView.findViewById(R.id.commentaire);
	    holder.itemMinute = (TextView) convertView.findViewById(R.id.minute);
	    holder.itemImage = (ImageView) convertView.findViewById(R.id.photo);
	    
	    convertView.setTag(holder);
	} else {
	    holder = (ViewHolder) convertView.getTag();
	}
	MatchEvent myevent = items.get(position);
	
	holder.itemName.setText(myevent.getMatchTypeEvent().getLabel());
	holder.itemImage.setImageResource(myevent.getTeam()==Team.HOME ?
		R.drawable.local : R.drawable.visitor); 

	String commentaire="";
	if(myevent.getPlayer()>0){
	    commentaire= "Joueur " + myevent.getPlayer() + " ";
	} 
	if(myevent.getComment()!=null){
	    commentaire.concat(myevent.getComment());
	}
	holder.itemComment.setText(commentaire);
	if(myevent.getMinute()>0){
	    holder.itemMinute.setText(myevent.getMinute()+"\" ");
	}
	return convertView;
    }

    static class ViewHolder {
	TextView itemName;
	TextView itemComment;
	TextView itemMinute;
	ImageView itemImage;
    }

}
