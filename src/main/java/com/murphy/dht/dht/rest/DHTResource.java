package com.murphy.dht.dht.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.dht.dht.entity.DHTManager;
import com.murphy.dht.dht.entity.DHTReading;



@Path("dht")
public class DHTResource {
	private static final Logger logger = LoggerFactory.getLogger(DHTResource.class);
	
	private String dateFormat = "HH:mm dd-MM-yyyy";
	private SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
	
	DHTManager dhtManager = new DHTManager();

	@GET
	@Path("findBetweenDates")
	@Produces(MediaType.APPLICATION_JSON)
	public List<DHTReading> getDHTReadings(@QueryParam("startDate") String startDateString, @QueryParam("endDate") String endDateString) throws ParseException{
		logger.debug("getDHTReadings called for: startdate: " + startDateString + "  endDate: " + endDateString);
		Date startDate = formatter.parse(startDateString);
		Date endDate = formatter.parse(endDateString);
		List<DHTReading> readings = dhtManager.findDHTReadingsBetweenDates(startDate, endDate);	
		return readings;
	}
	
	
}


