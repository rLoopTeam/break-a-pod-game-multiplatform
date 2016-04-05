package com.mygdx.entities;

import com.badlogic.gdx.maps.MapObject;

import java.util.Map;

public class Pair {
    private MapObject key;
    private Map<String, Object> value;

    public Pair(MapObject mO, Map<String, Object> m) {
        this.key = mO;
        this.value = m;
    }

    //accessors
    public MapObject getKey(){ return this.key; }
    public Map<String, Object> getValue(){ return this.value; }
    public void setKey(MapObject i){ this.key = i; }
    public void setValue(Map<String, Object> i){ this.value = i; }
}