package com.ajdeguzman.pocketloadtoolkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class History extends ActionBarActivity {
	int SERVICE_TYPE;
	String SERVICE;
	SimpleAdapter adapter;
	ListView lstActivityLogs;
	TextView txtNoDataFound;
	PromoClassHandler db = new PromoClassHandler(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		lstActivityLogs = (ListView)findViewById(R.id.lstActivityLogs);
		txtNoDataFound = (TextView)findViewById(R.id.txtNoDataFound);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history, menu);
		return true;
	}

 	@Override
 	public void onBackPressed() {
 	    super.onBackPressed();
 	    slideIt(1);
 		finish();
 	}
	@Override
	public void onStart(){
		super.onStart();
    	getSupportActionBar().setIcon(R.drawable.ic_launcher_main);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			SERVICE_TYPE = extras.getInt("service_type");
		}
		switch(SERVICE_TYPE){
		case 0:
			SERVICE = "CNT";
			getSupportActionBar().setSubtitle("Call & Text Promos");
			break;
		case 1:
			SERVICE = "INT";
			break;
		case 2:
			SERVICE = "NET";
			getSupportActionBar().setSubtitle("Internet Sevices");
			break;
		case 3:
			SERVICE = "TRA";
			getSupportActionBar().setSubtitle("Load Transfer");
			break;
		case 4:
			SERVICE = "CUS";
			getSupportActionBar().setSubtitle("Customer Service");
			break;
		case 5:
			SERVICE = "BAL";
			getSupportActionBar().setSubtitle("Balance Inquiry");
			break;
		case 6:
			SERVICE = "EME";
			getSupportActionBar().setSubtitle("Emergency Load");
			break;
		case 7:
			SERVICE = "REQ";
			getSupportActionBar().setSubtitle("Ask-a-Load");
			break;
		}
		getAllHistory(SERVICE);
	}
	public void showToast(String msg){
 		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
 	}

	private void getAllHistory(String s){
        List<HistoryClass> history = db.getAllHistoryWhere(s);  
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		for (HistoryClass cn : history) {
		    Map<String, String> datum = new HashMap<String, String>(2);
		    datum.put("title", cn.getRecipient());
		    datum.put("sub",  cn.getMessage() + "  <" + cn.getDate() + ">");
		    data.add(datum);
		}
		adapter = new SimpleAdapter(this, data,
		                                          android.R.layout.simple_list_item_2,
		                                          new String[] {"title", "sub"},
		                                          new int[] {android.R.id.text1,
		                                                     android.R.id.text2});

		lstActivityLogs.setAdapter(adapter);
		if(adapter.getCount() == 0){
			txtNoDataFound.setText("Activity log is empty.");
		}else{
			txtNoDataFound.setText("");
		}
	}
	public void slideIt(int opt){
 		if (opt != 0){
 			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
 		}
 	}
 	private void showConfirmDeleteLogs(){
	    AlertDialog.Builder bldr = new AlertDialog.Builder(this);
	    bldr.setTitle("Delete");
		bldr.setMessage("Are you sure you want to delete logs?");
		bldr.setCancelable(true);
		bldr.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which) {
	 				db.deleteHistoryWhere(SERVICE);
	 				getAllHistory(SERVICE);
				}
			});
		bldr.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
		final AlertDialog alt = bldr.create();
		alt.show();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    switch (item.getItemId())
	    {
	    case android.R.id.home:
	 		switch(SERVICE_TYPE){
			case 0:
		    	Intent intent1 = new Intent(getApplicationContext(), CntServices.class);
		    	intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		 		startActivity(intent1);
		 		slideIt(1);
				break;
			case 1:
		    	Intent intent2 = new Intent(getApplicationContext(), InternationalServices.class);
		    	intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		 		startActivity(intent2);
		 		slideIt(1);
				break;
			case 2:
		    	Intent intent3 = new Intent(getApplicationContext(), InternetServices.class);
		    	intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		 		startActivity(intent3);
		 		slideIt(1);
				break;
			case 3:
		    	Intent intent4 = new Intent(getApplicationContext(), TransferLoad.class);
		    	intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		 		startActivity(intent4);
		 		slideIt(1);
				break;
			case 4:
		    	Intent intent5 = new Intent(getApplicationContext(), CustomerService.class);
		    	intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		 		startActivity(intent5);
		 		slideIt(1);
				break;
			case 5:
		    	Intent intent6 = new Intent(getApplicationContext(), InquireBalance.class);
		    	intent6.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		 		startActivity(intent6);
		 		slideIt(1);
				break;
			case 6:
		    	Intent intent7 = new Intent(getApplicationContext(), EmergencyLoad.class);
		    	intent7.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		 		startActivity(intent7);
		 		slideIt(1);
				break;
			case 7:
		    	Intent intent8 = new Intent(getApplicationContext(), AskALoad.class);
		    	intent8.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		 		startActivity(intent8);
		 		slideIt(1);
				break;
			}
	        return true;
	    case R.id.action_delete:
	    	showConfirmDeleteLogs();
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
}
