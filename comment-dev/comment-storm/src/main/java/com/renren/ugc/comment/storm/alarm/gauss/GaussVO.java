package com.renren.ugc.comment.storm.alarm.gauss;

public class GaussVO {
	private int id;
	private double dx;
	private double ex;
	private String key;
	public double getEx() {
		return ex;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setEx(double ex) {
		this.ex = ex;
	}
	public double getDx() {
		return dx;
	}
	public void setDx(double dx) {
		this.dx = dx;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	
}
