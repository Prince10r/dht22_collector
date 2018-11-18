package com.murphy.raspberry.dht.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.murphy.raspberry.dht.entity.DHTManager;
import com.murphy.raspberry.dht.entity.DHTReading;

@Path("dht")
public class DHTResource {
	
	private String dateFormat = "HH:mm dd-MM-yyyy";
	private SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
	
	DHTManager dhtManager = new DHTManager();

	@GET
	@Path("findBetweenDates")
	@Produces(MediaType.APPLICATION_JSON)
	public List<DHTReading> getImage(@QueryParam("startDate") String startDateString, @QueryParam("endDate") String endDateString) throws ParseException{
		Date startDate = formatter.parse(startDateString);
		Date endDate = formatter.parse(endDateString);
		List<DHTReading> readings = dhtManager.findDHTReadingsBetweenDates(startDate, endDate);	
		return readings;
	}
	
	
}


