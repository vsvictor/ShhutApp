package com.shhutapp.data;

public class Address extends IntStringPair{
	private double lat;
	private double lon;
	
	public Address(int id, String name, double lat, double lon){
		super(id, name);
		this.lat = lat;
		this.lon = lon;
	}
	
	public void setLatitude(double lat){this.lat = lat;}
	public double getLatitude(){return this.lat;}
	public void setLongitude(double lon){this.lon = lon;}
	public double getLongitude(){return this.lon;}
}
