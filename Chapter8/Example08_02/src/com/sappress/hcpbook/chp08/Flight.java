package com.sappress.hcpbook.chp08;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class Flight implements Serializable, JSONAware
{
  private static final long serialVersionUID = 1L;

  private String carrier;
  private String id;
  private String fromAirport;
  private Date departureTime;
  private String toAirport;
  private Date arrivalTime;
  private Long flightDuration = 0l;

  public Flight() {}
  
  public String getCarrier()
  {
    return carrier;
  }
  
  public void setCarrier(String carrier)
  {
    this.carrier = carrier;
  }
  
  public String getId()
  {
    return id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }
  
  public String getFromAirport()
  {
    return fromAirport;
  }
  
  public void setFromAirport(String fromAirport)
  {
    this.fromAirport = fromAirport;
  }
  
  public Date getDepartureTime()
  {
    return departureTime;
  }
  
  public void setDepartureTime(Date departureTime)
  {
    this.departureTime = departureTime;
  }
  
  public String getToAirport()
  {
    return toAirport;
  }
  
  public void setToAirport(String toAirport)
  {
    this.toAirport = toAirport;
  }
  
  public Date getArrivalTime()
  {
    return arrivalTime;
  }
  
  public void setArrivalTime(Date arrivalTime)
  {
    this.arrivalTime = arrivalTime;
  }
  
  public Long getFlightDuration()
  {
    return flightDuration;
  }
  
  public void setFlightDuration(Long flightDuration)
  {
    this.flightDuration = flightDuration;
  }
  
  @SuppressWarnings("unchecked")
  public String toJSONString()
  {
    SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d yyyy HH:mm:ss");
    JSONObject obj = new JSONObject();
    
    obj.put("carrier", carrier);
    obj.put("flightId", id);
    obj.put("fromAirport", fromAirport);
    obj.put("departureTime", formatter.format(departureTime));
    obj.put("toAirport", toAirport);
    obj.put("arrivalTime", formatter.format(arrivalTime));
    obj.put("flightDuration", departureTime.toString());    
    
    return obj.toString();
  }
}