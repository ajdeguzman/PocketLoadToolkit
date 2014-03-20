package com.ajdeguzman.pocketloadtoolkit;

public class PromoClass {
	//private variables
	int _id;
	String _promo_name;
	String _promo_code;
	String _send_to;
	String _network;
	String _service;
	// Empty constructor
	public PromoClass(){
		
	}
	// constructor
	public PromoClass(int id, String _name, String _phone_number, String _send_to, String _network, String _service){
		this._id = id;
		this._promo_name = _name;
		this._promo_code = _phone_number;
		this._send_to = _send_to;
		this._network= _network;
		this._service = _service;
	}
	
	// constructor
	public PromoClass(String name, String _phone_number, String _send_to, String _network, String _service){
		this._promo_name = name;
		this._promo_code = _phone_number;
		this._send_to = _send_to;
		this._network = _network;
		this._service = _service;
	}
	// getting ID
	public int getID(){
		return this._id;
	}
	
	// setting id
	public void setID(int id){
		this._id = id;
	}
	
	// getting name
	public String getPromoName(){
		return this._promo_name;
	}
	
	// setting name
	public void setPromoName(String promo_name){
		this._promo_name = promo_name;
	}
	
	// getting phone number
	public String getPromoCode(){
		return this._promo_code;
	}
	
	// setting phone number
	public void setPromoCode(String promo_code){
		this._promo_code = promo_code;
	}
	// getting phone number
	public String getSendTo(){
		return this._send_to;
	}
	
	// setting phone number
	public void setSendTo(String send_to){
		this._send_to = send_to;
	}
	// getting phone number
	public String getNetwork(){
		return this._network;
	}
	
	// setting phone number
	public void setNetwork(String network){
		this._network = network;
	}
	// getting phone number
	public String getService(){
		return this._service;
	}
	
	// setting phone number
	public void setService(String service){
		this._service = service;
	}
}
