package com.ajdeguzman.pocketloadtoolkit;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class InquireBalance extends ActionBarActivity implements ActionBar.OnNavigationListener {

	int NETWORK;
	String simSelected, SEND_TO, NUMBER_TO_CALL;
	PromoClassHandler db = new PromoClassHandler(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inquire_balance);
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
		getMenuInflater().inflate(R.menu.inquire_balance, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		switch (itemPosition)
	    {
	    case 0:
	 		NETWORK = 0;
	 		SEND_TO = "214";
	 		getSupportActionBar().setIcon(R.drawable.ic_tnt);
	 		break;
	    case 1:
	    	NETWORK = 1;
	 		SEND_TO = "214";
	 		getSupportActionBar().setIcon(R.drawable.ic_smart);
	 		break;
	    case 2:
	    	NETWORK = 2;
	 		SEND_TO = "2292";
	 		getSupportActionBar().setIcon(R.drawable.ic_sun);
	 		break;
	    case 3:
	    	NETWORK = 3;
	 		SEND_TO = "222";
	 		getSupportActionBar().setIcon(R.drawable.ic_globe);
	 		break;
	    case 4:
	    	NETWORK = 4;
	    	getSupportActionBar().setIcon(R.drawable.ic_tm);
	 		break;
	 	default:
	 		NETWORK = 0;
	    }
	return false;
	}
	public void clickInquireBalance(View v){
		switch(NETWORK){
			case 0:
					sendInquiry(SEND_TO,"?1515");
					writeLog(SEND_TO,"?1515");
					break;
			case 1:
					sendInquiry(SEND_TO,"?1515");
					writeLog(SEND_TO,"?1515");
				break;
			case 2:
					NUMBER_TO_CALL = "*221" + Uri.encode("#");
					callInquiry(NUMBER_TO_CALL);
					writeLog(NUMBER_TO_CALL,"");
				break;
			case 3:
					sendInquiry(SEND_TO,"BAL");
					writeLog(NUMBER_TO_CALL,"BAL");
				break;
			case 4:
					NUMBER_TO_CALL = "*102" + Uri.encode("#");
					callInquiry(NUMBER_TO_CALL);
					writeLog(NUMBER_TO_CALL,"");
				break;
		}
	}
	private void writeLog(String recipient, String message){
		String date = null;
		String type = "BAL";
		if (null == date) {
			date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
			.format(new Date());
		}
        db.addHistory(new HistoryClass(type,recipient,message,date));
	}
	private void callInquiry(String number){
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
		    bundle.putInt("service_type", 5);
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

}
