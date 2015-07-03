package com.shhutapp.data;

import java.util.Comparator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class StringStringPair implements BaseObject{
	private String id;
	private String name;
	
	public StringStringPair(String id, String name) {
		this.id = id;
		this.name = name;
	}
	@Override
	public void setID(Object key) {
		this.id = (String) key;
	}
	@Override
	public String getID() {
		return id;
	}
	@Override
	public void setName(Object name) {
		this.name = (String) name;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public int compareTo(Object another) {
		return this.getName().compareToIgnoreCase((String) ((BaseObject)another).getName());
	}
}
