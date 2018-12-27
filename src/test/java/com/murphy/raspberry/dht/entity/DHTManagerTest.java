package com.murphy.raspberry.dht.entity;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.murphy.dht.entity.DHTManager;
import com.murphy.dht.entity.DHTReading;
import com.murphy.dht.schedule.DHTCollector;

import static org.junit.Assert.*;

public class DHTManagerTest {

	@Test
	public void testCreateDHTReading() {
		String dhtString = "Temp=21.6*  Humidity=61.6%";
		DHTCollector dhtCollector = new DHTCollector();
		DHTReading dhtReading = dhtCollector.convert(dhtString, 5, new Date());
		DHTManager dhtManager = new DHTManager();
		dhtManager.createDHTReading(dhtReading);
		
		long fiveHours = 5 * 60 * 60 * 1000;
		Date startDate = new Date(System.currentTimeMillis() - fiveHours);
		Date endDate = new Date(System.currentTimeMillis() + fiveHours);
		
		List<DHTReading> readings = dhtManager.findDHTReadingsBetweenDates(startDate, endDate);
		
		assertTrue(readings.size() > 0);
	}

}
