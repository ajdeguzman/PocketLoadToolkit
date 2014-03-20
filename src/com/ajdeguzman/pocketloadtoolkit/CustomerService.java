package com.ajdeguzman.pocketloadtoolkit;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.app.ActionBar;

public class CustomerService extends ActionBarActivity implements ActionBar.OnNavigationListener {
	public static final String APP_PREFERENCES = "APP_PREF";
	public static final String APP_PREFERENCES_SIM = "sim";
	SharedPreferences mAppSettings;
	PromoClassHandler db = new PromoClassHandler(this);
	
	int NETWORK, SEND_TO;
	String NUMBER_TO_CALL, NUMBER_TO_TEXT;
	String[] cust_smart = new String[]{
			  "*888", "*800", "*888","*1888", "15177", "*333", "*7744",  "*7788"
			};
	String[] cust_sun = new String[]{
			   "200","888","333"
			};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_service);
		generateNavigationMenu();
		mAppSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
		getPreferencesValue();
	}
	public void generateNavigationMenu(){
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		final String[] mobileNetworks = getResources().getStringArray(R.array.mobile_networks); 
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(actionBar.getThemedContext(),R.layout.support_simple_spinner_dropdown_item, android.R.id.text1, mobileNetworks);
		adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(adapter, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.customer, menu);
		return true;
	}
 	@Override
 	public void onBackPressed() {
 	    super.onBackPressed();
 	    slideIt(1);
 		finish();
 	}
 	public void clickCall(View v){
 		switch(NETWORK){
	 		case 0:
	 	 		startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + NUMBER_TO_CALL)));
	 	 		writeLog("",NUMBER_TO_CALL);
	 	 		break;
	 		case 1:
	 			showNumberList(R.array.arr_customerservice_smart);
	 			break;
	 		case 2:
	 			showNumberList(R.array.arr_customerservice_sun);
	 			break;
	 		case 3:
	 			startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + NUMBER_TO_CALL)));
	 	 		writeLog("",NUMBER_TO_CALL);
	 			break;
	 		case 4:
	 			startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + NUMBER_TO_CALL)));
	 	 		writeLog("",NUMBER_TO_CALL);
	 			break;
 		}
 	}
 	private void writeLog(String recipient, String message){
		String date = null;
		String type = "CUS";
		if (null == date) {
			date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
			.format(new Date());
		}
        db.addHistory(new HistoryClass(type,recipient,message,date));
	}
	private void showNumberList(int arr){
 		AlertDialog.Builder builderSingle = new AlertDialog.Builder(CustomerService.this);
         final ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                 this, arr, android.R.layout.simple_list_item_1);
         builderSingle.setTitle("Select Customer Service");
         builderSingle.setNegativeButton("Cancel",
                 new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         dialog.dismiss();
                     }
                 });

         builderSingle.setAdapter(arrayAdapter,
                 new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                    	 switch(NETWORK){
             	 			case 1:
	                           	 NUMBER_TO_CALL = cust_smart[which];
	             	 			 startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + NUMBER_TO_CALL)));
	             	 	 		 writeLog("",NUMBER_TO_CALL);
             	 				 break;
             	 			case 2:
	                           	 NUMBER_TO_CALL = cust_sun[which];
	             	 			 startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + NUMBER_TO_CALL)));
	             	 	 		 writeLog("",NUMBER_TO_CALL);
             	 				 break;
                    	 }
                     }
                 });
         builderSingle.show();
 	}
	public void slideIt(int opt){
 		if (opt != 0){
 			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
 		}
 	}
	private void getPreferencesValue(){
 		SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(this);
 		String simSelected;
 		simSelected = appPrefs.getString("lstDefaultSim", "");
 		if(simSelected != ""){
 			getSupportActionBar().setSelectedNavigationItem(Integer.parseInt(simSelected));
 		}else{
 			getSupportActionBar().setSelectedNavigationItem(0);
 		}
	}	
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		 switch (itemPosition)
		    {
		    case 0:
		 		NETWORK = 0;
		 		SEND_TO = 888;
		 		NUMBER_TO_CALL = "*888";
		 		getSupportActionBar().setIcon(R.drawable.ic_tnt);
		 		break;
		    case 1:
		    	NETWORK = 1;
		 		SEND_TO = 888;
		 		getSupportActionBar().setIcon(R.drawable.ic_smart);
		 		break;
		    case 2:
		    	NETWORK = 2;
		 		SEND_TO = 2292;
		 		getSupportActionBar().setIcon(R.drawable.ic_sun);
		 		break;
		    case 3:
		    	NETWORK = 3;
		 		SEND_TO = 2292;
		 		NUMBER_TO_CALL = "*143" + Uri.encode("#");
		 		getSupportActionBar().setIcon(R.drawable.ic_globe);
		 		break;
		    case 4:
		    	NETWORK = 4;
		 		NUMBER_TO_CALL = "*143" + Uri.encode("#");
		 		NUMBER_TO_TEXT = "1234";
		 		getSupportActionBar().setIcon(R.drawable.ic_tm);
		 		break;
		 	default:
		 		NETWORK = 0;
		    }
		return false;
	}
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    switch (item.getItemId())
	    {
	    case android.R.id.home:
	    	Intent intent = new Intent(getApplicationContext(), MainActivity.class);
	 		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
	 		startActivity(intent);
	 		slideIt(1);
	 		finish();
	        return true;
	    case R.id.action_history:
	    	Intent i = new Intent(getApplicationContext(), History.class);
		    Bundle bundle = new Bundle();
		    bundle.putInt("service_type", 4);
		    i.putExtras(bundle);
		    startActivity(i);
	 		slideIt(1);
	 		finish();
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
}
