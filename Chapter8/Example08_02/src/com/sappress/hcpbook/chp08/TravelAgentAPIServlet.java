package com.sappress.hcpbook.chp08;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("serial")
public class TravelAgentAPIServlet extends HttpServlet 
{
  // Constant Declarations:
  private static final int RECORD_COUNT = 50;
  private static final String FROM_AIRPORT_PARAM = "from";
  private static final String TO_AIRPORT_PARAM = "to";
  
  private static final String[] carriers =
    {"AA", "AB", "AC", "B6", "BY", "CA", "DL", "LH", "UA"};
  
  private static final String[] airports = 
    {"ATL", "PEK", "LHR", "HND", "ORD", "LAX", "CDG", "DFW", "CGK", "DXB",
     "FRA", "HKG", "DEN", "BKK", "SIN", "AMS", "JFK", "CAN", "MAD", "IST",
     "PVG", "SFO", "CLT", "LAS", "PHX", "IAH", "KUL", "MIA", "ICN", "MUC"};
  
  private static final Logger logger = LoggerFactory.getLogger(TravelAgentAPIServlet.class);
  
  // Instance Attributes:
  private List<Flight> flights = new ArrayList<Flight>();
  
  /**
   * Initialize the API with test data.
   */
  public void init(ServletConfig config)
  {
    // Generate some random (garbage) data for selection:
    Random carrierGen = new Random();
    Random airportGen = new Random();
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.add(GregorianCalendar.DATE, -30);
    
    for (int i = 0; i < RECORD_COUNT; i++)
    {
      Flight flight = new Flight();
      
      flight.setCarrier(carriers[carrierGen.nextInt(carriers.length)]);
      flight.setId(String.valueOf(i));
      flight.setFromAirport(airports[airportGen.nextInt(airports.length)]);
      flight.setDepartureTime(calendar.getTime());
      calendar.add(GregorianCalendar.HOUR, 3);
      flight.setArrivalTime(calendar.getTime());
      flight.setToAirport(airports[airportGen.nextInt(airports.length)]);
      flight.setFlightDuration(3l);
      
      flights.add(flight);
    }
  } // -- public void init() -- //
  
  @SuppressWarnings("unchecked")
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException 
  {
    // Retrieve API parameters:
    String fromAirport = request.getParameter(FROM_AIRPORT_PARAM);
    String toAirport = request.getParameter(TO_AIRPORT_PARAM);
    
    logger.debug("Received a query for flights from " + fromAirport + " to " + toAirport + ".");

    // Try to build a list of flights that match the selection criteria:
    JSONArray flightResults = new JSONArray();
    
    for (Flight f : flights)
    {
      if ((fromAirport != null) && (! fromAirport.equals("")))
      {
        if (f.getFromAirport().equals(fromAirport) == false)
          continue;
      }
      
      if ((toAirport != null) && (! toAirport.equals("")))
      {
        if (f.getToAirport().equals(toAirport) == false)
          continue;
      }
      
      flightResults.add(f);
    }
    
    logger.info("Found " + flightResults.size() + " flight records in the database.");
    logger.info(flightResults.toJSONString());
    
    // Output the results using JSON:
    response.setContentType("application/json");
    response.getWriter().println(flightResults.toJSONString());
  } // -- protected void doGet() -- //
  
} // -- public class TravelAgentAPIServlet -- //