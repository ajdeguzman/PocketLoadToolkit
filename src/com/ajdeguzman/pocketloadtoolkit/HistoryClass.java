package com.ajdeguzman.pocketloadtoolkit;

public class HistoryClass {
	//private variables
	int _id;
	String _type;
	String _recipient;
	String _message;
	String _date;
	
	// Empty constructor
	public HistoryClass(){
		
	}
	// constructor
	public HistoryClass(int _id, String _type, String _recipient, String _message, String _date){
		this._id = _id;
		this._type = _type;
		this._recipient = _recipient;
		this._message = _message;
		this._date= _date;
	}
	
	// constructor
	public HistoryClass(String _type, String _recipient, String _message, String _date){
		this._type = _type;
		this._recipient = _recipient;
		this._message = _message;
		this._date = _date;
	}
	
	public int getID(){
		return this._id;
	}
	
	public void setID(int id){
		this._id = id;
	}
	
	public String getType(){
		return this._type;
	}
	
	public void setType(String _type){
		this._type = _type;
	}
	
	public String getRecipient(){
		return this._recipient;
	}
	
	public void setRecipient(String _recipient){
		this._recipient = _recipient;
	}
	public String getMessage(){
		return this._message;
	}
	
	public void setMessage(String _message){
		this._message = _message;
	}
	
	public String getDate(){
		return this._date;
	}
	
	public void setDate(String _date){
		this._date = _date;
	}
}
