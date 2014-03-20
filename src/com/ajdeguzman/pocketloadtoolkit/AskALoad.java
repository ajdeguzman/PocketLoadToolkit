package com.ajdeguzman.pocketloadtoolkit;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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

public class AskALoad extends ActionBarActivity implements ActionBar.OnNavigationListener {

	int NETWORK;
	boolean quickSend, vibrate;
	EditText txtFriendNumber;
	Button btnBrowseFriendNumber;
	private final int PICK = 1;
	PromoClassHandler db = new PromoClassHandler(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_aload);
		generateNavigationMenu();
		txtFriendNumber = (EditText)findViewById(R.id.txtFriendNumber);
		btnBrowseFriendNumber = (Button)findViewById(R.id.btnBrowseFriendNumber);
		btnBrowseFriendNumber.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, PICK);
				
			}
		});
		getPreferencesValue();
	}

	private void getPreferencesValue(){
 		SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(this);
 		String simSelected;
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.ask_aload, menu);
		return true;
	}
 	public void slideIt(int opt){
 		if (opt != 0){
 			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
 		}
 	}
 	@Override
 	public void onBackPressed() {
 	    super.onBackPressed();
 	    slideIt(1);
 		finish();
 	}
 	public void showToast(String msg){
 		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
 	}
 	private boolean isEmpty(EditText txt) {
 	    if (txt.getText().toString().trim().length() > 0) {
 	        return false;
 	    } else {
 	        return true;
 	    }
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
		                    txtFriendNumber.setText(num);
		                    txtFriendNumber.setSelection(txtFriendNumber.getText().length());
		}
		}
			}
			break;
		}
	}
	private void vibrateMe(){
		 Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		 // Vibrate for 500 milliseconds
		 v.vibrate(200);
	}
	public void clickAskALoad(View v){
		if(isEmpty(txtFriendNumber)){
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
	        findViewById(R.id.txtFriendNumber).startAnimation(shake);
	        findViewById(R.id.btnBrowseFriendNumber).startAnimation(shake);
	        showToast("Please enter friend\'s number");
	        if(vibrate){
	        	vibrateMe();
	        }
		}else{
			callInquiry("*808" + txtFriendNumber.getText());
			writeLog("*808", txtFriendNumber.getText().toString());
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
	private void callInquiry(String number){
		startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number)));
	}
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		 switch (itemPosition)
		    {
		    case 0:
		 		getSupportActionBar().setIcon(R.drawable.ic_tnt);
		 		break;
		    case 1:
		    	getSupportActionBar().setIcon(R.drawable.ic_smart);
		 		break;
		    case 2:
		 		getSupportActionBar().setIcon(R.drawable.ic_tnt);
	 			getSupportActionBar().setSelectedNavigationItem(0);
	 			showToast("Service not yet supported");
		 		break;
		    case 3:
		 		getSupportActionBar().setIcon(R.drawable.ic_tnt);
	 			getSupportActionBar().setSelectedNavigationItem(0);
	 			showToast("Service not yet supported");
		 		break;
		    case 4:
		 		getSupportActionBar().setIcon(R.drawable.ic_tnt);
	 			getSupportActionBar().setSelectedNavigationItem(0);
	 			showToast("Service not yet supported");
		 		break;
		 	default:
		 		NETWORK = 0;
		    }
		return false;
	}

	private void writeLog(String recipient, String message){
		String date = null;
		String type = "REQ";
		if (null == date) {
			date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
			.format(new Date());
		}
        db.addHistory(new HistoryClass(type,recipient,message,date));
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
		    bundle.putInt("service_type", 7);
		    i.putExtras(bundle);
		    startActivity(i);
	 		slideIt(1);
	 		finish();
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

}
