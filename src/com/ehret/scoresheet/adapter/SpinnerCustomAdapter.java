package com.ehret.scoresheet.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ehret.scoresheet.domain.ISpinnerData;

public class SpinnerCustomAdapter<T extends ISpinnerData> extends ArrayAdapter<T> {
   /**
    * Our MatchTypeEvent
    */
    private List<T> values;
    /**
     * Layout
     */
    private int dropDownResource;
    
    /**
     * The super constructor we need
     * @param context
     * @param textViewResourceId
     * @param objects
     */
    public SpinnerCustomAdapter(Context context, int textViewResourceId, List<T> objects) {
	super(context, textViewResourceId, objects);
	this.values = objects;
    }
    
    @Override
    public int getCount(){
	return values !=null ? values.size() : 0;
    }

    @Override
    public T getItem(int position){
	return values!=null ? values.get(position) : null;
    }
    
    @Override
    public long getItemId(int position){
	T item = getItem(position);
	return item.getId();
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	return getCustomView(position, convertView, parent, android.R.layout.simple_spinner_item);
    }
    
    
    @Override
    public void setDropDownViewResource(int resource) {
        this.dropDownResource = resource;
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
        
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
	return getCustomView(position, convertView, parent, dropDownResource);
    }
}
