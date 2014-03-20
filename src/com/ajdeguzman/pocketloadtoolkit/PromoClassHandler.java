package com.ajdeguzman.pocketloadtoolkit;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PromoClassHandler extends SQLiteOpenHelper  {
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "dbpromo";
	private static final String TABLE_PROMO = "tblpromo";
	private static final String KEY_ID = "id";
	private static final String KEY_PROMO_NAME = "promoname";
	private static final String KEY_PROMO_CODE = "promocode";
	private static final String KEY_SEND_TO = "sendto";
	private static final String KEY_NETWORK = "network";
	private static final String KEY_SERVICE= "service";
	

	private static final String TABLE_HISTORY = "tblhistory";
	private static final String HIST_ID = "id";
	private static final String HIST_TYPE = "type";
	private static final String HIST_RECIPIENT = "recipient";
	private static final String HIST_MESSAGE = "message";
	private static final String HIST_DATE = "date";
	
	public PromoClassHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_PROMO_TABLE = "CREATE TABLE " + TABLE_PROMO + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," 
				+ KEY_PROMO_NAME + " TEXT,"
				+ KEY_PROMO_CODE + " TEXT,"
				+ KEY_SEND_TO + " TEXT,"
				+ KEY_NETWORK + " TEXT,"
				+ KEY_SERVICE + " TEXT" + ")";
		String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
				+ HIST_ID + " INTEGER PRIMARY KEY," 
				+ HIST_TYPE + " TEXT,"
				+ HIST_RECIPIENT + " TEXT,"
				+ HIST_MESSAGE + " TEXT,"
				+ HIST_DATE + " TEXT" + ")";
		db.execSQL(CREATE_PROMO_TABLE);
		db.execSQL(CREATE_HISTORY_TABLE);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMO);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
		// Create tables again
		onCreate(db);
	}
	/**
	 * All CRUD(Create, Read, Update, Delete) Operations for Promo
	 */

	// Adding new promo
	void addPromo(PromoClass promo) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_PROMO_NAME, promo.getPromoName());
		values.put(KEY_PROMO_CODE, promo.getPromoCode());
		values.put(KEY_SEND_TO, promo.getSendTo());
		values.put(KEY_NETWORK, promo.getNetwork());
		values.put(KEY_SERVICE, promo.getService());
		// Inserting Row
		db.insert(TABLE_PROMO, null, values);
		db.close(); // Closing database connection
	}

	// Getting single promo
	PromoClass getPromo(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_PROMO, new String[] { KEY_ID, KEY_PROMO_NAME, KEY_PROMO_CODE, KEY_SEND_TO, KEY_NETWORK, KEY_SERVICE }, KEY_ID + "=?",
						new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		PromoClass promo = new PromoClass(Integer.parseInt(cursor.getString(0)),
															cursor.getString(1),
															cursor.getString(2),
															cursor.getString(3),
															cursor.getString(4),
															cursor.getString(5));
		return promo;
	}
	
	// Getting All promo
	public List<PromoClass> getAllPromo() {
		List<PromoClass> promoList = new ArrayList<PromoClass>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PROMO;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				PromoClass promo = new PromoClass();
				promo.setID(Integer.parseInt(cursor.getString(0)));
				promo.setPromoName(cursor.getString(1));
				promo.setPromoCode(cursor.getString(2));
				promo.setSendTo(cursor.getString(3));
				promo.setNetwork(cursor.getString(4));
				promo.setService(cursor.getString(5));
				// Adding promo to list
				promoList.add(promo);
			} while (cursor.moveToNext());
		}

		// return promo list
		return promoList;
	}
	// Getting All promo
	public List<PromoClass> getAllPromoWhere(String where, String service) {
		List<PromoClass> promoList = new ArrayList<PromoClass>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PROMO + " WHERE network = '" + where + "' AND service = '" + service + "'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				PromoClass promo = new PromoClass();
				promo.setID(Integer.parseInt(cursor.getString(0)));
				promo.setPromoName(cursor.getString(1));
				promo.setPromoCode(cursor.getString(2));
				promo.setSendTo(cursor.getString(3));
				promo.setNetwork(cursor.getString(4));
				promo.setService(cursor.getString(5));
				// Adding promo to list
				promoList.add(promo);
			} while (cursor.moveToNext());
		}

		// return promo list
		return promoList;
	}
	// Updating single promo
	public int updatePromo(PromoClass contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_PROMO_NAME, contact.getPromoName());
		values.put(KEY_PROMO_CODE, contact.getPromoCode());
		values.put(KEY_SEND_TO, contact.getSendTo());

		// updating row
		return db.update(TABLE_PROMO, values, KEY_ID + " = ?",
				new String[] { String.valueOf(contact.getID()) });
	}

	// Deleting single contact
	public void deleteContact(PromoClass promo) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PROMO, KEY_ID + " = ?",
				new String[] { String.valueOf(promo.getID()) });
		db.close();
	}

	// Deleting single contact
	public void deleteContact(String code) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PROMO, KEY_PROMO_CODE + " = ?",
				new String[] { code });
		db.close();
	}
	
	public PromoClass selectSendToWhere(String code) {
		String query = "Select * FROM " + TABLE_PROMO + " WHERE " + KEY_PROMO_CODE + " =  \"" + code + "\"";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		PromoClass promo = new PromoClass();
		if (cursor.moveToFirst()) {
			cursor.moveToFirst();
			promo.setSendTo(cursor.getString(3));
			promo.getSendTo();
			cursor.close();
		} else {
			promo = null;
		}
	        db.close();
		return promo;
	}

	// Getting contacts Count
	public int getPromoCount() {
		String countQuery = "SELECT  * FROM " + TABLE_PROMO;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		//cursor.close();

		// return count
		return cursor.getCount();
	}
	

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations for History
	 */
	public void deleteHistoryWhere(String code) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_HISTORY, HIST_TYPE + " = ?",
				new String[] { code });
		db.close();
	}
	
	// Getting All promo
		public List<HistoryClass> getAllHistoryWhere(String where) {
			List<HistoryClass> historyList = new ArrayList<HistoryClass>();
			// Select All Query
			String selectQuery = "Select * FROM " + TABLE_HISTORY + " WHERE " + HIST_TYPE + " =  \'" + where + "\'";
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					HistoryClass history = new HistoryClass();
					history.setID(Integer.parseInt(cursor.getString(0)));
					history.setType(cursor.getString(1));
					history.setRecipient(cursor.getString(2));
					history.setMessage(cursor.getString(3));
					history.setDate(cursor.getString(4));
					// Adding promo to list
					historyList.add(history);
				} while (cursor.moveToNext());
			}
			// return promo list
			return historyList;
		}
		
		// Adding new promo
		void addHistory(HistoryClass history) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(HIST_TYPE, history.getType());
			values.put(HIST_RECIPIENT, history.getRecipient());
			values.put(HIST_MESSAGE, history.getMessage());
			values.put(HIST_DATE, history.getDate());
			// Inserting Row
			db.insert(TABLE_HISTORY, null, values);
			db.close(); // Closing database connection
		}
}
