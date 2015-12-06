package com.example.evindj.popularmovies.movie;

/**
 * Created by evindj on 12/3/15.
 */
public enum SortOrder {
    MOSTPOPULAR(0),
    HIGHESTRATED(1);
    private int value;
    private SortOrder(int value){
        this.value= value;
    }
    public  int getValue(){
        return value;
    }
    public void setValue(int value){
        this.value = value;
    }
}
