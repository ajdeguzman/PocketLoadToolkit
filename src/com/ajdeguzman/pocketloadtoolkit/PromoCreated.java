package com.ajdeguzman.pocketloadtoolkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

public class PromoCreated extends ActionBarActivity {
	
	ListView lstPromoCreated;
	PromoClassHandler db = new PromoClassHandler(this);
	TextView txtNoDataFound;
	SimpleAdapter adapter;
	boolean quickSend, vibrate;
	String NETWORK, SERVICE;
	AssetManager manager; String line = null;
	private ProgressBar spinner;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_promo_created);
		lstPromoCreated = (ListView)findViewById(R.id.lstPromoCreated);
		txtNoDataFound = (TextView)findViewById(R.id.txtNoDataFound);
		spinner = (ProgressBar)findViewById(R.id.progressBar);
        lstPromoCreated.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				 String x = ((TwoLineListItem) arg1).getText2().getText().toString();
				 PromoClass y = db.selectSendToWhere(x);
				 Intent i = null;
			 		if(SERVICE.equals("CNT")){
					 	 i = new Intent(PromoCreated.this, CntServices.class);
			 		}else if(SERVICE.equals("INT")){
					 	 i = new Intent(PromoCreated.this, InternationalServices.class);
			 		}else if(SERVICE.equals("NET")){
					 	 i = new Intent(PromoCreated.this, InternetServices.class);
			 		}
			 	 i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				 Bundle bundle = new Bundle();
				 bundle.putString("sendto", y.getSendTo().toString());
				 bundle.putString("loadcode", x.toString());
				 i.putExtras(bundle);
		    	 startActivity(i);
		 	     slideIt(1);
		 	     finish();
			}
        });
        lstPromoCreated.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				String x = ((TwoLineListItem) arg1).getText2().getText().toString();
				showPrompt("This item will be deleted.", "Delete?", x);
				return true;
			}
			
		});

		getPreferencesValue();
		AsyncTaskRunner runner = new AsyncTaskRunner();
	    runner.execute("");
	}
	
	
/*****************************************/

	 private class AsyncTaskRunner extends AsyncTask<String, String, String> {
	  private String resp;
	  @Override
	  protected String doInBackground(String... params) {
	   publishProgress("Sleeping..."); // Calls onProgressUpdate()
	   try {
		   	getSuggestedPromo();
		    Thread.sleep(500);
	   	   } catch (InterruptedException e) {
		    e.printStackTrace();
		    resp = e.getMessage();
		   } catch (Exception e) {
			    e.printStackTrace();
			    resp = e.getMessage();
		   }
		   return resp;
		  }
	  @Override
	  protected void onPostExecute(String result) {
	        getAllPromo();
			spinner.setVisibility(View.GONE);
	  }

	  @Override
	  protected void onPreExecute() {
		  spinner.setVisibility(View.VISIBLE);
	  }
	  
	  @Override
	  protected void onProgressUpdate(String... text) {
	   
	  }
	 }
	
/*****************************************/
	private void getPreferencesValue(){
 		SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(this);
 		vibrate = appPrefs.getBoolean("chkVibrate", true);
	}	
	private void vibrateMe(){
		 Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		 // Vibrate for 500 milliseconds
		 v.vibrate(200);
	}
	@Override
	public void onStart(){
		super.onStart();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			NETWORK = extras.getString("network");
			SERVICE = extras.getString("service");
		}
		if(SERVICE.equals("CNT")){
			getSupportActionBar().setSubtitle("Call & Text Promos");
		}else if(SERVICE.equals("INT")){
			getSupportActionBar().setSubtitle("International Services Promo");
		}else if(SERVICE.equals("NET")){
			getSupportActionBar().setSubtitle("Mobile Internet Promo");
		}
		 switch (Integer.parseInt(NETWORK))
		    {
		    case 0:
		 		getSupportActionBar().setIcon(R.drawable.ic_tnt);
		 		break;
		    case 1:
		    	getSupportActionBar().setIcon(R.drawable.ic_smart);
		 		break;
		    case 2:
		    	getSupportActionBar().setIcon(R.drawable.ic_sun);
		 		break;
		    case 3:
		    	getSupportActionBar().setIcon(R.drawable.ic_globe);
		 		break;
		    case 4:
		    	getSupportActionBar().setIcon(R.drawable.ic_tm);
		 		break;
		    }
        getSuggestedPromo();
	}
	private void getSuggestedPromo(){
			String promoname, promocode, send_to, network, service;
			manager = getAssets();
			try {
				if (!(db.getPromoCount()>0)){
					InputStream is = manager.open("promo.txt");
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(isr);
			            while ((line = br.readLine()) != null) {
			            	String promo[] = line.split("~");
			            	promoname = promo[0];
			            	promocode = promo[1];
			            	send_to = promo[2];
			            	network = promo[3];
			            	service = promo[4];
			                db.addPromo(new PromoClass(promoname,promocode,send_to,network,service));
			            }
			            br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				showToast(e.toString());
			} catch (RuntimeException e) {
				e.printStackTrace();
				showToast(e.toString());
			}
	}
		private void showPrompt(String msg, String title, final String code){
			 AlertDialog.Builder bldr = new AlertDialog.Builder(this);
				bldr.setTitle(title);
				bldr.setMessage(msg);
				bldr.setCancelable(true);
				bldr.setIcon(R.drawable.ic_action_warning);
				bldr.setPositiveButton("Delete", new DialogInterface.OnClickListener() { 
					public void onClick(DialogInterface dialog, int which) {
							db.deleteContact(code);
							getAllPromo();
						}
					});

				bldr.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { 
					public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
				final AlertDialog alt = bldr.create();
				alt.show();
	}
	private void getAllPromo(){
		
        List<PromoClass> contacts = db.getAllPromoWhere(NETWORK, SERVICE);  
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		for (PromoClass cn : contacts) {
		    Map<String, String> datum = new HashMap<String, String>(2);
		    datum.put("title", cn.getPromoName());
		    datum.put("sub", cn.getPromoCode());
		    data.add(datum);
		}
		adapter = new SimpleAdapter(this, data,
		                                          android.R.layout.simple_list_item_2,
		                                          new String[] {"title", "sub"},
		                                          new int[] {android.R.id.text1,
		                                                     android.R.id.text2});

		lstPromoCreated.setAdapter(adapter);
		if(adapter.getCount() == 0){
			txtNoDataFound.setText("Promo list is empty.");
		}else{
			txtNoDataFound.setText("");
		}
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

 	private boolean isEmpty(EditText txt) {
 	    if (txt.getText().toString().trim().length() > 0) {
 	        return false;
 	    } else {
 	        return true;
 	    }
 	}

 	
 	private void insertNewPromo(String promo_name, String promo_code, String send_to, String network, String service){
       try{
    	   db.addPromo(new PromoClass(promo_name, promo_code, send_to, network, service)); 
    	   showToast("Promo Successfully Saved");
       }catch(SQLException e){
    	   showToast("Saving failed");
       }
 	}
 	
 	private void createNewPromo(){
 		AlertDialog.Builder alert = new AlertDialog.Builder(this);
 		LayoutInflater inflater = PromoCreated.this.getLayoutInflater();
 		alert.setTitle("Create New Promo");
 			
 		View layout=inflater.inflate(R.layout.create_new_promo,null);       
        alert.setView(layout); 
        
		final EditText txtInsertPromoName, txtInsertPromoCode, txtInsertSendTo;
		txtInsertPromoName = (EditText)layout.findViewById(R.id.txtInsertPromoName);
		txtInsertPromoCode = (EditText)layout.findViewById(R.id.txtInsertPromoCode);
		txtInsertSendTo = (EditText)layout.findViewById(R.id.txtInsertSendTo);
		
 		alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
 		public void onClick(DialogInterface dialog, int whichButton) {
 			String promo_name,promo_code,send_to;
			promo_name = txtInsertPromoName.getText().toString().trim();
			promo_code = txtInsertPromoCode.getText().toString().trim();
			send_to = txtInsertSendTo.getText().toString().trim();
			if(isEmpty(txtInsertPromoName) || isEmpty(txtInsertPromoCode) || isEmpty(txtInsertSendTo)){
				showToast("All fields are required");
				if(vibrate){
		        	vibrateMe();
		        }
			}else{
	 			insertNewPromo(promo_name,promo_code,send_to, String.valueOf(NETWORK), SERVICE);	
			}	

	        getAllPromo();
 		  }
 		
 		});

 		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
 		  public void onClick(DialogInterface dialog, int whichButton) {
 		    dialog.cancel();
 		  }
 		});
 		
 		alert.show();
 	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    switch (item.getItemId())
	    {
	    case android.R.id.home:
	 		if(SERVICE.equals("CNT")){
	 			startActivity(new Intent(getApplicationContext(), CntServices.class));
		 		slideIt(1);
		 		finish();
	 		}else if(SERVICE.equals("INT")){
	 			startActivity(new Intent(getApplicationContext(), InternationalServices.class));
		 		slideIt(1);
		 		finish();
	 		}else if(SERVICE.equals("NET")){
	 			startActivity(new Intent(getApplicationContext(), InternetServices.class));
		 		slideIt(1);
		 		finish();
	 		}
	 		break;
	    case R.id.action_add:
	    	createNewPromo();
	        break;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
		return true;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.promo_created, menu);
		return true;
	}

	public void showToast(String msg){
 		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
 	}

}
