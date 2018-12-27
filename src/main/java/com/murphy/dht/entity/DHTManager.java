package com.murphy.dht.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * There is no update method for a DHTReading as the data is static.
 * 
 * @author ronan
 *
 */
public class DHTManager {

	private static final Logger LOG = LoggerFactory.getLogger(DHTManager.class);
	
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("dht.odb");
	EntityManager em = emf.createEntityManager();
	List<DHT> dhts = null;
	
	public DHTManager() {
		loadDHTs();
	}

	/**
	 * Not sure the this needs to be synchronized but better on the safe side.
	 * 
	 * @param dhtReading
	 */
	public synchronized void createDHTReading(DHTReading dhtReading) {
		em.getTransaction().begin();
		em.persist(dhtReading);
		em.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public synchronized List<DHTReading> findDHTReadingsBetweenDates(Date startDate, Date endDate) {
		List<DHTReading> result = em
				.createQuery("SELECT dht FROM DHTReading dht WHERE dht.date BETWEEN :startDate AND :endDate ")
				.setParameter("startDate", startDate).setParameter("endDate", endDate).getResultList();

		return result;
	}
	
	public void loadDHTs() {
		Properties props = new Properties();
		dhts = new ArrayList<>();
		try {
			props.load(DHTManager.class.getResourceAsStream("/dhtpins.properties"));
			for(Object id : props.keySet()) {
				Object gpio = props.get(id);
				DHT dht = new DHT(Integer.parseInt(id.toString()), Integer.parseInt(gpio.toString()));
				dhts.add(dht);
			}
		} catch (IOException e) {
			LOG.error("Failed to read in the properties file for DHT Pins");
		}			
	}
	
	public List<DHT> getDHTs(){
		return dhts;
	}

}
