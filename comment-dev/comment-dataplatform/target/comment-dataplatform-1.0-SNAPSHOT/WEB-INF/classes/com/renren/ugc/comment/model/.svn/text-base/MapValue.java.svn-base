package com.renren.ugc.comment.model;

public class MapValue {
	
	private int value;
	
	private int count;
	
	public MapValue(int value){
		this.value = value;
		this.count = 1;
	}
	
	public void inc(int increment){
		this.value += increment;
		this.count++;
	}
	
	public double getAvg(){
		return count != 0 ? value /count : 0;
	}

}
