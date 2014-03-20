package com.ajdeguzman.pocketloadtoolkit;

import android.os.Bundle;
import android.os.Vibrator;
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
import android.database.SQLException;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.telephony.SmsManager;


public class InternationalServices extends ActionBarActivity implements ActionBar.OnNavigationListener {

	PromoClassHandler db = new PromoClassHandler(this);

	boolean quickSend, vibrate;
	String simSelected;
	int NETWORK;
	String SEND_TO;
	EditText txtPromoCode;
	final String SERVICE = "INT";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_international_services);
		txtPromoCode = (EditText)findViewById(R.id.txtPromoCode);
		generateNavigationMenu();
		getPreferencesValue();
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

	public void generateNavigationMenu(){
		ActionBar actionBar;
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		final String[] mobileNetworks = getResources().getStringArray(R.array.mobile_networks); 
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(actionBar.getThemedContext(), R.layout.support_simple_spinner_dropdown_item, android.R.id.text1, mobileNetworks);
		adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(adapter, this);
	}
	private void getPreferencesValue(){
 		SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(this);
 		simSelected = appPrefs.getString("lstDefaultSim", "");
 		quickSend = appPrefs.getBoolean("chkQuickSend", true);
 		vibrate = appPrefs.getBoolean("chkVibrate", true);
 		if(simSelected != ""){
 			getSupportActionBar().setSelectedNavigationItem(Integer.parseInt(simSelected));
 		}else{
 			getSupportActionBar().setSelectedNavigationItem(0);
 		}
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
			    bundle.putInt("service_type", 1);
			    i.putExtras(bundle);
			    startActivity(i);
		 		slideIt(1);
		 		finish();
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void onStart(){
		super.onStart();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			SEND_TO = extras.getString("sendto");
		    String loadcode = extras.getString("loadcode");
		    txtPromoCode.setText(loadcode);
		    txtPromoCode.setSelection(txtPromoCode.getText().length());
		}
	}
	
	public void clickBrowsePromoCode(View v){
		 Intent i = new Intent(getApplicationContext(), PromoCreated.class);
		    Bundle bundle = new Bundle();
		    bundle.putString("network", String.valueOf(NETWORK));
		    bundle.putString("service", "INT");
		    i.putExtras(bundle);
		    startActivity(i);
	 		slideIt(1);
	 		finish();
	}
 	private boolean isEmpty(EditText txt) {
 	    if (txt.getText().toString().trim().length() > 0) {
 	        return false;
 	    } else {
 	        return true;
 	    }
 	}

	private void vibrateMe(){
		 Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		 // Vibrate for 500 milliseconds
		 v.vibrate(200);
	}
	public void clickRegister(View v){
		if(isEmpty(txtPromoCode)){
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
	        findViewById(R.id.txtPromoCode).startAnimation(shake);
	        findViewById(R.id.btnBrowsePromoCode).startAnimation(shake);
	        showToast("Please Select Promo Code");
	        if(vibrate){
	        	vibrateMe();
	        }
		}else{
			if(SEND_TO.toString().length() > 0){
				if(quickSend)
		 		{
		 			quickSendSMS(SEND_TO, txtPromoCode.getText().toString().trim());
		 			showToast("Sending...");
		 		}
		 		else
		 		{
		 			sendSMS(SEND_TO, txtPromoCode.getText().toString().trim());
		 		}
			}
		}
	}
	private void sendSMS(String phoneNumber, String message){
		Intent smsIntent= new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms"); 
        smsIntent.putExtra("address", phoneNumber);
        smsIntent.putExtra("sms_body", message);
        startActivity(smsIntent);
}
	private void quickSendSMS(String phoneNumber, String message)
	{      
	/*
	PendingIntent pi = PendingIntent.getActivity(this, 0,
	        new Intent(this, test.class), 0);                
	    SmsManager sms = SmsManager.getDefault();
	    sms.sendTextMessage(phoneNumber, null, message, pi, null);        
	*/
	
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
				    Toast.makeText(getBaseContext(), "Message succesfully sent", 
				    		Toast.LENGTH_SHORT).show();
				    break;
			    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				    Toast.makeText(getBaseContext(), "Message sending failed", 
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
	        if(vibrate){
	        	vibrateMe();
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.international_services, menu);
		return true;
	}
	private void clearTextFields(){
		txtPromoCode.setText(null);
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
	public void showToast(String msg){
 		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
 	}

}
