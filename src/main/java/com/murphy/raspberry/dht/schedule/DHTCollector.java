package com.murphy.raspberry.dht.schedule;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.raspberry.dht.command.ProcessManager;
import com.murphy.raspberry.dht.entity.DHT;
import com.murphy.raspberry.dht.entity.DHTManager;
import com.murphy.raspberry.dht.entity.DHTReading;

public class DHTCollector {

	private static final Logger LOG = LoggerFactory.getLogger(DHTCollector.class);
	
	// Query every 1 mins
	private int SCHEDULE = 1 * 60 * 1000;

	// Allow the system 40 seconds to start up.
	private int SCHEDULE_DELAY = 3 * 1000;

	DHTManager dhtManager = new DHTManager();
	ProcessManager processManager = new ProcessManager();

	public void start() {
		TimerTask task = new TimerTask() {
			public void run() {
				queryDHTs();
			}
		};
		Timer timer = new Timer("Timer");

		timer.scheduleAtFixedRate(task, SCHEDULE_DELAY, SCHEDULE);
	}

	private void queryDHTs() {
		List<DHT> dhts = dhtManager.getDHTs();
		if (SystemUtils.IS_OS_LINUX) {
			for (final DHT dht : dhts) {
				processManager.queryDHT((success, message) -> {
					if (success) {
						DHTReading dhtReading = convert(message, dht.getId());
						if (dhtReading != null) {
							dhtManager.createDHTReading(dhtReading);
						}
					}
				}, dht.getGpio());
			}
		}
	}

	public DHTReading convert(String dhtString, int dhtId) {
		try {
			String[] parts = dhtString.split(" ");
			String tempStr = parts[0].split("=")[1].replaceAll("[*]", "");
			String humPart = parts[2].split("=")[1].replaceAll("[%]", "");
			DHTReading dhtReading = new DHTReading();
			dhtReading.setDhtID(dhtId);
			dhtReading.setDate(new Date());
			dhtReading.setTemperature(Float.parseFloat(tempStr));
			dhtReading.setHumidity(Float.parseFloat(humPart));
			return dhtReading;
		} catch (Exception e) {
			LOG.error("Failed to convert DHT Reading: " + dhtString);
		}
		return null;
	}

}
