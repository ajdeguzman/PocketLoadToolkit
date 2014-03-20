package com.ajdeguzman.pocketloadtoolkit;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.app.ActionBar;

public class EmergencyLoad extends ActionBarActivity implements ActionBar.OnNavigationListener {

	int NETWORK;
	String simSelected, SEND_TO, NUMBER_TO_CALL;
	PromoClassHandler db = new PromoClassHandler(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emergency_load);
		generateNavigationMenu();
		getPreferencesValue();
	}

	@Override
 	public void onBackPressed() {
 	    super.onBackPressed();
 		slideIt(1);
 		finish();
 	}
	private void getPreferencesValue(){
 		SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(this);
 		simSelected = appPrefs.getString("lstDefaultSim", "");
 		if(simSelected != ""){
 			getSupportActionBar().setSelectedNavigationItem(Integer.parseInt(simSelected));
 		}else{
 			getSupportActionBar().setSelectedNavigationItem(0);
 		}
	}	

	public void generateNavigationMenu(){
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		final String[] mobileNetworks = getResources().getStringArray(R.array.mobile_networks); 
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(actionBar.getThemedContext(), R.layout.support_simple_spinner_dropdown_item, android.R.id.text1, mobileNetworks);
		adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(adapter, this);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.emergency_load, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		switch (itemPosition)
	    {
	    case 0:
	 		NETWORK = 0;
	 		getSupportActionBar().setIcon(R.drawable.ic_tnt);
	 		break;
	    case 1:
	    	NETWORK = 1;
	 		getSupportActionBar().setIcon(R.drawable.ic_smart);
	 		break;
	    case 2:
	    	NETWORK = 2;
	 		getSupportActionBar().setIcon(R.drawable.ic_sun);
	 		break;
	    case 3:
	    	NETWORK = 3;
	 		SEND_TO = "3733";
	 		getSupportActionBar().setIcon(R.drawable.ic_globe);
	 		break;
	    case 4:
	    	NETWORK = 4;
	    	getSupportActionBar().setIcon(R.drawable.ic_tm);
	 		SEND_TO = "3733";
	 		break;
	 	default:
	 		NETWORK = 0;
	    }
	return false;
	}
	public void clickRequestEmerg(View v){
		switch(NETWORK){
			case 0:
					showEmergencyList(R.array.arr_emergencyload_smart);
					break;
			case 1:
					showEmergencyList(R.array.arr_emergencyload_smart);
				break;
			case 2:
					showToast("Service not available");
				break;
			case 3:
					sendInquiry(SEND_TO,"GTSOS");
					writeLog(SEND_TO,"GTSOS");
				break;
			case 4:
					sendInquiry(SEND_TO,"GTSOS");
					writeLog(SEND_TO,"GTSOS");
				break;
		}
	}

 	private void writeLog(String recipient, String message){
		String date = null;
		String type = "EME";
		if (null == date) {
			date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
			.format(new Date());
		}
        db.addHistory(new HistoryClass(type,recipient,message,date));
	}
	private void showEmergencyList(int arr){
 		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
 				EmergencyLoad.this);
         final ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                 this, arr, android.R.layout.simple_list_item_1);
         builderSingle.setTitle("Select Emergency Load");
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
                    	 switch(which){
                    	 	case 0:
                    	 		callInquiry("*767");
            					writeLog("*767","");
                    	 		break;
                    	 	case 1:
                    	 		callInquiry("*7572");
            					writeLog("*767","");
                    	 		break;
                    	 }
                     }
                 });
         builderSingle.show();
 	}
	public void callInquiry(String number){
		startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number)));
	}
	private void sendInquiry(String phoneNumber, String message)
    {      
    	String SENT = "SMS_SENT";
    	String DELIVERED = "SMS_DELIVERED";
    	
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
            new Intent(SENT), 0);
        
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
            new Intent(DELIVERED), 0);
    	
        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode())
				{
				    case Activity.RESULT_OK:
					    Toast.makeText(getBaseContext(), "Your balance inquiry will be sent to you shortly", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					    Toast.makeText(getBaseContext(), "Balance Inquiry failed", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_NO_SERVICE:
					    Toast.makeText(getBaseContext(), "No service", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_NULL_PDU:
					    Toast.makeText(getBaseContext(), "Null PDU", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_RADIO_OFF:
					    Toast.makeText(getBaseContext(), "Radio off", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				}
			}
        }, new IntentFilter(SENT));
        
        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode())
				{
				    case Activity.RESULT_OK:
					    Toast.makeText(getBaseContext(), "SMS delivered", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case Activity.RESULT_CANCELED:
					    Toast.makeText(getBaseContext(), "SMS not delivered", 
					    		Toast.LENGTH_SHORT).show();
					    break;					    
				}
			}
        }, new IntentFilter(DELIVERED));        
    	
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);               
    } 
	@Override
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
		    bundle.putInt("service_type", 6);
		    i.putExtras(bundle);
		    startActivity(i);
	 		slideIt(1);
	 		finish();
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

 	public void slideIt(int opt){
 		if (opt != 0){
 			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
 		}
 	}
 	public void showToast(String msg){
 		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
 	}

}
