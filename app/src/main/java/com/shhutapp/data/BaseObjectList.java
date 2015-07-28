package com.shhutapp.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BaseObjectList  extends ArrayList<BaseObject>{
	public BaseObject find(int id){
		BaseObject result = null;
		Integer key  = new Integer(id);
		for(BaseObject b : this){
			if(b.getID() == key) return b;
		}
		return result;
	}
	public BaseObject find(String name){
		BaseObject result = null;
		for(BaseObject b : this){
			if(b.getName().equals(name)) return b;
		}
		return result;
	}
	public boolean isPresent(String name){
		BaseObject b = find(name);
		if(b!=null) return true;
		else return false;
	}
	public void sortByName(){
		//Collections.sort(this);
		Collections.sort(this, new Comparator<BaseObject>() {
			@Override
			public int compare(BaseObject lhs, BaseObject rhs) {
				String s1 = (String) lhs.getName();
				String s2 = (String) rhs.getName();
				return s1.compareToIgnoreCase(s2);
			}
		});

	}
	public int newID(){
		int b = 0;
        if(this==null) return 0;
		for(BaseObject o: this){
            if(((Integer)o.getID()>b)) b=(Integer)o.getID();
        }
        return b++;
	}
/*	public static BaseObjectList concat(BaseObjectList first, BaseObjectList second){
		BaseObjectList res = new BaseObjectList();
		for(BaseObject b:first) res.add(b);
		for(BaseObject b:second) res.add(b);
		return res;
	}*/
}
