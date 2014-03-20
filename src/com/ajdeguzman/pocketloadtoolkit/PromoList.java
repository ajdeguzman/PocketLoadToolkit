package com.ajdeguzman.pocketloadtoolkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class PromoList extends Activity {
	
	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_promo_list);
        expListView = (ExpandableListView) findViewById(R.id.expandablePromo);
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
 
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });
 
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
 
            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Expanded",
//                        Toast.LENGTH_SHORT).show();
            }
        });
 
        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
 
            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Collapsed",
//                        Toast.LENGTH_SHORT).show();
 
            }
        });
 
        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
//                Toast.makeText(
//                        getApplicationContext(),
//                        listDataHeader.get(groupPosition)
//                                + " : "
//                                + listDataChild.get(
//                                        listDataHeader.get(groupPosition)).get(
//                                        childPosition), Toast.LENGTH_SHORT)
//                        .show();
            	//sendItem("loadcode", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
            	showToast("Soon \u263A");
                return false;
            }
        });
        expListView.expandGroup(0);
	}
	public void showToast(String msg){
 		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
 	}
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        // Adding child data
    	listDataHeader.add("Combo");
    	listDataHeader.add("Text");
    	listDataHeader.add("Call");

    	String[] call_promo = getResources().getStringArray(R.array.arr_smart_call_promo);
    	String[] text_promo = getResources().getStringArray(R.array.arr_smart_text_promo);
    	String[] combo = getResources().getStringArray(R.array.arr_smart_combo_promo);
        // Adding child data
        List<String> call_child = new ArrayList<String>();
        List<String> text_child = new ArrayList<String>();
        List<String> combo_child = new ArrayList<String>();
        for(String str : call_promo){
        	call_child.add(str);
        }
        for(String str : text_promo){
        	text_child.add(str);
        }
        for(String str : combo){
        	combo_child.add(str);
        }
        listDataChild.put(listDataHeader.get(0), combo_child);
        listDataChild.put(listDataHeader.get(1), text_child);
        listDataChild.put(listDataHeader.get(2), call_child);
    }
    private void sendItem(String name, String value){
    	Intent i = new Intent(getApplicationContext(), CntServices.class);
    	i.putExtra(name,value);
    	startActivity(i);
 	    slideIt(1);
    }
 	@Override
 	public void onBackPressed() {
 	    super.onBackPressed();
 	    slideIt(1);
 		finish();
 	}
 	public void slideIt(int opt){
 		if (opt != 0){
 			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
 		}
 	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    switch (item.getItemId())
	    {
	    case android.R.id.home:
	 		startActivity(new Intent(getApplicationContext(), CntServices.class));
	 		slideIt(1);
	 		finish();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

}
