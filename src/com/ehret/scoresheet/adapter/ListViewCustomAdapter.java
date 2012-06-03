package com.ehret.scoresheet.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ehret.scoresheet.domain.ISpinnerData;

public class ListViewCustomAdapter<T extends ISpinnerData> extends SpinnerCustomAdapter<T> {
    
    /**
     * The super constructor we need
     * @param context
     * @param textViewResourceId
     * @param objects
     */
    public ListViewCustomAdapter(Context context, int textViewResourceId, List<T> objects) {
	super(context, textViewResourceId, objects);
    }
    

    /**
     * We had to override the creation of the composant to display your own label
     * @param position
     * @param convertView
     * @param parent
     * @param layout
     * @return
     */
    protected View getCustomView(int position, View convertView, ViewGroup parent, int layout){
	View view;
        TextView label;

        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(layout, parent, false);
        } else {
            view = convertView;
        }
        
        label = (TextView) view;
	label.setText(getItem(position).getLabel());
        label.setPadding(5, 5, 5, 5);
        return label;
    }
}
