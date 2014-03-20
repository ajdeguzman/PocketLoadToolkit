package com.ajdeguzman.pocketloadtoolkit;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	public static final String APP_PREFERENCES = "APP_PREF";
	public static final String APP_PREFERENCES_SIM = "sim";
	private SearchView mSearchView;
	ImageButton imgBtnCallnText, imgBtnInternational, imgBtnInternet, imgBtnTransfer;
	String strMoreOption;
	PromoClassHandler db = new PromoClassHandler(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_main);
		imgBtnCallnText = (ImageButton) findViewById(R.id.imgBtnCallnText);
	}
	@Override
	public void onStart(){
		super.onStart();
    	getSupportActionBar().setIcon(R.drawable.ic_launcher_main);
	}
	//Clickables for Menu
	 	public void clickCallnText(View v) {
	 		startActivity(new Intent(getApplicationContext(), CntServices.class));
	 		slideIt(1);
	    }
	 	public void clickCallnText2(View v) {
	 		startActivity(new Intent(getApplicationContext(), CntServices.class));
	 		slideIt(1);
	    }
	 	public void clickTransfer(View v){
	 		startActivity(new Intent(getApplicationContext(), TransferLoad.class));
	 		slideIt(1);
	 	}
	 	public void clickMore(View v) {
	 		showMore();
	    }
	 	public void clickCustomer(View v) {
	 		startActivity(new Intent(getApplicationContext(), CustomerService.class));
	 		slideIt(1);
	 	}
	 	public void clickInquireBalance(View v) {
	 		startActivity(new Intent(getApplicationContext(), InquireBalance.class));
	 		slideIt(1);
	 	}
	 	public void clickInternet(View v) {
	 		startActivity(new Intent(getApplicationContext(), InternetServices.class));
	 		slideIt(1);
	 	}
	 	 private void showMore(){
	 		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
	 				MainActivity.this);
	         final ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
	                 this, R.array.more_options, android.R.layout.simple_list_item_1);
	         builderSingle.setTitle("More Services");
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
	                    	 		startActivity(new Intent(getApplicationContext(), EmergencyLoad.class));
	                    	 		slideIt(1);
	                    	 		break;
	                    	 	case 1:
	                    	 		startActivity(new Intent(getApplicationContext(), AskALoad.class));
	                    	 		slideIt(1);
	                    	 		break;
	                    	 }
	                     }
	                 });
	         builderSingle.show();
	 	}	

	 	public void showToast(String msg){
	 		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	 	}
		@Override
		public void onBackPressed() {
			showConfirmExit();
		}
	 	private void showConfirmExit(){
		    AlertDialog.Builder bldr = new AlertDialog.Builder(this);
			bldr.setTitle("Confirm Exit");
			bldr.setMessage("Are you sure you want to exit?");
			bldr.setCancelable(true);
			bldr.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
				public void onClick(DialogInterface dialog, int which) {
	                MainActivity.super.onBackPressed();
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
	 	public void slideIt(int opt){
	 		if (opt != 0){
	 			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	 		}
	 	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    switch (item.getItemId())
	    {
	    case R.id.action_settings:
	    	startActivity(new Intent(getApplicationContext(), AppPreferenceActivity.class));
	 		slideIt(1);
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

}
