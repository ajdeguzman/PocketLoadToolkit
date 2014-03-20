package com.ajdeguzman.pocketloadtoolkit;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TransferLoad extends ActionBarActivity implements ActionBar.OnNavigationListener {
	public static final String APP_PREFERENCES = "APP_PREF";
	public static final String APP_PREFERENCES_SIM = "sim";
	private final int PICK = 1;
	SharedPreferences mAppSettings;
	EditText txtRecipient, txtLoadAmount, txtPinCode;
	PromoClassHandler db = new PromoClassHandler(this);
	String strRecipient, strLoadAmount, strPin, recipient, message, strLoadCode, simSelected;
	String SEND_TO;
	int NETWORK;
	boolean quickSend, vibrate;
	Button btnAdd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transfer_load);
		generateNavigationMenu();
		txtRecipient = (EditText) findViewById(R.id.txtRecipient);
		txtLoadAmount = (EditText) findViewById(R.id.txtLoadAmount);
		txtPinCode = (EditText) findViewById(R.id.txtPinCode);
		btnAdd = (Button) findViewById(R.id.btnAdd);
		mAppSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
		btnAdd.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, PICK);
				
			}
		});
		getPreferencesValue();
	}
 	public void clickBrowserTransferLoadCode(View v){
 		switch(NETWORK){
 			case 0:
 				showLoadTransferList(R.array.arr_transferload_tnt);
 				break;
 			case 1:
 				showLoadTransferList(R.array.arr_transferload_smart);
 				break;
 			case 2:
 				showLoadTransferList(R.array.arr_transferload_sun);
 				break;
 			case 3:
 				showLoadTransferList(R.array.arr_transferload_globe);
 				break;
 			case 4:
 				showLoadTransferList(R.array.arr_transferload_tm);
 				break;
 		}
 	}
 	public void clickPinInfo(View v){
 		String msg_pin = "Other networks allow you to have an option to include a security PIN whenever you transfer. The security PIN ensures that load is not transferred without your knowledge. ";
 		showMessage(msg_pin, "Pin Code");
 	}
 	public void clickTransferSend(View v){
 		if(!isEmpty(txtRecipient) && !isEmpty(txtLoadAmount)){
 			showConfirmSendLoad();
 		}
 		if(isEmpty(txtRecipient)){
 			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
 	        findViewById(R.id.txtRecipient).startAnimation(shake);
 	        findViewById(R.id.btnAdd).startAnimation(shake);
 	        showToast("Please check required fields");
	        if(vibrate){
	        	vibrateMe();
	        }
 		}
 		if(isEmpty(txtLoadAmount)){
 			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
 	        findViewById(R.id.txtLoadAmount).startAnimation(shake);
 	        findViewById(R.id.btnBrowseTransferLoadCode).startAnimation(shake);
	        if(vibrate){
	        	vibrateMe();
	        }
 		}
 	}
 	private boolean isEmpty(EditText txt) {
 	    if (txt.getText().toString().trim().length() > 0) {
 	        return false;
 	    } else {
 	        return true;
 	    }
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
	private void vibrateMe(){
		 Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		 // Vibrate for 500 milliseconds
		 v.vibrate(200);
	}
 	private void sendLoad(){
 		strRecipient = txtRecipient.getText().toString();
 		strLoadAmount = txtLoadAmount.getText().toString();
 		strPin = txtPinCode.getText().toString();
 		switch(NETWORK){
 			case 0: case 1:
 				recipient = SEND_TO;
 				message = strRecipient + " " + strLoadAmount;
 				break;
 			case 2:
 				recipient = SEND_TO;
 				if(strPin.length() > 0){
 	 				message = strLoadAmount + " " + strPin + " " + strRecipient;
 				}else{
 					message = strLoadAmount + " " + strRecipient;
 				}
 				break;
 			case 3: case 4:
 				recipient = SEND_TO + "" + strRecipient.substring(1, strRecipient.length());
 				if(strPin.length() > 0){
 	 				message = strLoadAmount + " " + strPin;
 				}else{
 					message = strLoadAmount;
 				}
 				break;
 		}
	 		if(quickSend)
	 		{
	 			quickSendSMS(recipient, message);
 				writeLog(recipient,message);
	 			showToast("Sending...");
	 		}
	 		else
	 		{
	 			sendSMS(recipient, message);
	 		}
 	}
 	private void showConfirmSendLoad(){
	    AlertDialog.Builder bldr = new AlertDialog.Builder(this);
		bldr.setTitle("Load Transfer");
		bldr.setMessage("Are you sure you want to transfer load to " + txtRecipient.getText() + " ?");
		bldr.setCancelable(true);
		bldr.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which) {
					sendLoad();
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

	private void writeLog(String recipient, String message){
		String date = null;
		String type = "TRA";
		if (null == date) {
			date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
			.format(new Date());
		}
        db.addHistory(new HistoryClass(type,recipient,message,date));
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
 	@Override
 	public void onBackPressed() {
 	    super.onBackPressed();
 	    slideIt(1);
 		finish();
 	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.transfer_load, menu);
		return true;
	}
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		switch (reqCode) {
		case (PICK):
			if (resultCode == Activity.RESULT_OK) {
				 Uri contactData = data.getData();
		            ContentResolver cr = getContentResolver();
		            Cursor c = cr.query(contactData, null, null, null, null);
		            if (c.moveToFirst()) {
		                String id = c
		                        .getString(c
		                                .getColumnIndexOrThrow(ContactsContract.Contacts._ID));

		                String hasPhone = c
		                        .getString(c
		                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

		                if (hasPhone.equalsIgnoreCase("1")) {
		                    Cursor phones = getContentResolver()
		                            .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
		                                    null,
		                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
		                                            + " = " + id, null, null);
		                    phones.moveToFirst();
		                    String cNumber = phones
		                            .getString(phones
		                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

		                    String nameContact = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
		                    String num = cNumber.toString().replaceAll("\\s+","");
		                    Toast.makeText(getApplicationContext(), "You've selected " + nameContact.toString(),
		                            Toast.LENGTH_SHORT).show();
		                    if(num.charAt(0) == '+'){
		                    	num = num.replace("+63", "0");
		                    }
							txtRecipient.setText(num);
							txtRecipient.setSelection(txtRecipient.getText().length());
		}
		}
			}
			break;
		}
	}
 	public void slideIt(int opt){
 		if (opt != 0){
 			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
 		}
 	}
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		 switch (itemPosition)
		    {
		    case 0:
		 		NETWORK = 0;
		 		SEND_TO = "888";
		 		getSupportActionBar().setIcon(R.drawable.ic_tnt);
		 		txtPinCode.setEnabled(false);
		 		clearTextFields();
		 		break;
		    case 1:
		    	NETWORK = 1;
		 		SEND_TO = "888";
		 		getSupportActionBar().setIcon(R.drawable.ic_smart);
		 		txtPinCode.setEnabled(false);
		 		clearTextFields();
		 		break;
		    case 2:
		    	NETWORK = 2;
		 		SEND_TO = "2292";
		 		getSupportActionBar().setIcon(R.drawable.ic_sun);
		 		txtPinCode.setEnabled(true);
		 		clearTextFields();
		 		break;
		    case 3:
		    	NETWORK = 3;
		 		SEND_TO = "2";
		 		getSupportActionBar().setIcon(R.drawable.ic_globe);
		 		txtPinCode.setEnabled(true);
		 		clearTextFields();
		 		break;
		    case 4:
		    	NETWORK = 4;
		    	getSupportActionBar().setIcon(R.drawable.ic_tm);
		 		txtPinCode.setEnabled(true);
		 		clearTextFields();
		 		SEND_TO = "2";
		 		break;
		 	default:
		 		NETWORK = 0;
		    }
		return false;
	}
	private void clearTextFields(){
 		txtRecipient.setText(null);
 		txtLoadAmount.setText(null);
 		txtPinCode.setText(null);
	}
	private void showLoadTransferList(int i){
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
				TransferLoad.this);
        final ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                this, i, android.R.layout.simple_list_item_1);
        builderSingle.setTitle("Load Amount");
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
                    	strLoadCode = (String) arrayAdapter.getItem(which);
                    	txtLoadAmount.setText(strLoadCode.toString());
                    	txtLoadAmount.setFocusable(true);
                    	txtLoadAmount.setSelection(txtLoadAmount.getText().length());
                    }
                });
        builderSingle.show();
	}	

 	public void showToast(String msg){
 		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
 	}
	private void showMessage(String msg, String title){
		 AlertDialog.Builder bldr = new AlertDialog.Builder(this);
			bldr.setTitle(title);
			bldr.setMessage(msg);
			bldr.setCancelable(true);
			bldr.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
				public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
			final AlertDialog alt = bldr.create();
			alt.show();
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
		    bundle.putInt("service_type", 3);
		    i.putExtras(bundle);
		    startActivity(i);
	 		slideIt(1);
	 		finish();
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
}
