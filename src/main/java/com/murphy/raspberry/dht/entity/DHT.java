package com.murphy.raspberry.dht.entity;

public class DHT {

	public int id;
	public int gpio;

	public DHT(int id, int gpio) {
		this.id = id;
		this.gpio = gpio;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGpio() {
		return gpio;
	}

	public void setGpio(int gpio) {
		this.gpio = gpio;
	}
}
