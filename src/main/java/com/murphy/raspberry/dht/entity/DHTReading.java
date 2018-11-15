package com.murphy.raspberry.dht.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class DHTReading {

	@Id @GeneratedValue
	private long id;
	
	private float humidity;
	private float temperature;
	private Date date;	
	private int dhtID;	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getDhtID() {
		return dhtID;
	}

	public void setDhtID(int dhtID) {
		this.dhtID = dhtID;
	}

	public float getHumidity() {
		return humidity;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "DHTReading [id=" + id + ", humidity=" + humidity + ", temperature=" + temperature + ", date=" + date
				+ ", dhtID=" + dhtID + "]";
	}
	
	

}
