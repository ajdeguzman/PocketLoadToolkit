package com.ajdeguzman.pocketloadtoolkit;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;


public class AppPreferenceActivity extends PreferenceActivity {
	ActionBarActivity ab = new ActionBarActivity();
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.app_preference);
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
	    	Intent intent = new Intent(getApplicationContext(), MainActivity.class);
	 		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
	 		startActivity(intent);
	 		slideIt(1);
	 		finish();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

}
