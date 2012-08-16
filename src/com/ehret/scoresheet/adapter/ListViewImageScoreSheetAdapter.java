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
import com.ehret.scoresheet.domain.ScoreSheet;

/**
 * Diplay the result in a list with an image on the left
 * 
 * @author ehret_g
 * 
 * @param <T>
 */
public class ListViewImageScoreSheetAdapter extends BaseAdapter {
    private List<ScoreSheet> items;
    private Context context;
    
    public ListViewImageScoreSheetAdapter(Context context, List<ScoreSheet> results) {
	this.items = results;
	this.context=context;
    }
    
    public int getCount() {
	return items.size();
    }

    public Object getItem(int position) {
	return items.get(position);
    }

    public long getItemId(int position) {
	return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder;
	if (convertView == null) {
	    convertView = LayoutInflater.from(context).inflate(R.layout.openeritem, null);
	    holder = new ViewHolder();
	    holder.itemDate = (TextView) convertView.findViewById(R.id.name);
	    holder.itemScore = (TextView) convertView.findViewById(R.id.score);
	    holder.itemImage = (ImageView) convertView.findViewById(R.id.photo);
	    
	    convertView.setTag(holder);
	} else {
	    holder = (ViewHolder) convertView.getTag();
	}
	ScoreSheet scoreSheet = items.get(position);
	
	holder.itemDate.setText(scoreSheet.getDate());
	holder.itemImage.setImageResource(context.getResources().getIdentifier(
		"sport"+scoreSheet.getSport().getId().intValue(), 
		"drawable", 
		context.getPackageName()));

	String scoreMatch = new StringBuilder()
		.append(context.getText(R.string.team_local))
		.append(" : ")
		.append(scoreSheet.getScoreHome())
		.append(" ")
		.append(context.getText(R.string.team_visitor))
		.append(" : ")
		.append(scoreSheet.getScoreVisitor())
		.append(" ")
		.toString();
	holder.itemScore.setText(scoreMatch);
	return convertView;
    }

    static class ViewHolder {
	TextView itemDate;
	TextView itemScore;
	ImageView itemImage;
    }

}
