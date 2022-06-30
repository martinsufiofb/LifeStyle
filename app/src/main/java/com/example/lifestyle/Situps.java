package com.example.lifestyle;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Situps")
public class Situps extends ParseObject {
    public static final String KEY_COUNT = "count";
    public static final String KEY_USER = "user";

    public String getCount(){
        return getString(KEY_COUNT);
    }
    public ParseUser getUser(){return getParseUser(KEY_USER);}

    public void  setCount(String count){
        put(KEY_COUNT, count);
    }
    public void setUser(ParseUser user){put(KEY_USER, user);}
}
