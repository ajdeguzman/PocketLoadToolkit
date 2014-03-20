package com.ajdeguzman.pocketloadtoolkit;


import java.util.List;
import java.util.Map;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class MyAdapter extends ArrayAdapter<String> {
	public MyAdapter(Context context, int textViewResourceId,
			List<String> objects) {
		super(context,textViewResourceId, objects);
	}
@Override
	public View getView(int position, View convertView, ViewGroup parent) {  
	View view = super.getView(position, convertView, parent);
	
	if (position % 2 == 1) {
	    view.setBackgroundColor(Color.rgb(210,210,210));  
	} else {
	    view.setBackgroundColor(Color.TRANSPARENT);
	}
	return view;  
	}
}

