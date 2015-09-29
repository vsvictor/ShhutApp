package com.shhutapp.data;

/**
 * Created by victor on 24.09.15.
 */
public class Incomming extends IntStringPair {
    private long time;

    public Incomming(int id, String numb, long t){
        super(id, numb);
        time = t;
    }
    public long getTime(){
        return time;
    }
}
