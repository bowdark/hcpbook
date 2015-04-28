package com.sappress.hcpbook.chp08;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class MainControllerServlet extends HttpServlet 
{
  // Constant declarations:
  private static final Logger logger = LoggerFactory.getLogger(MainControllerServlet.class);

  // Instance attributes:
  private FlightModel flightModel = new FlightModel();
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	  throws ServletException, IOException 
	{
	  // Determine the action triggered on the frontend:
    Map<String, String[]> paramMap = request.getParameterMap();
    String action = "";
    
    if (! paramMap.containsKey("action"))
    {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must specify an action code.");
    }
    else
    {
      action = paramMap.get("action")[0];
    }
    
    // Process the action:
    try
    {
      if (action.equalsIgnoreCase("get-flights"))
      {
        // Process the flight lookup request:
        String from = "";
        String to = "";
        
        if (paramMap.containsKey("from"))
          from = paramMap.get("from")[0];
        
        if (paramMap.containsKey("to"))
          to = paramMap.get("to")[0];
        
        logger.debug("Received a flight lookup request from " + from + " to " + to + ".");
        
        String jsonResults = flightModel.getFlights(from, to);
        logger.debug("JSON Results: " + jsonResults);
        
        // Check the results:
        if (jsonResults == null)
        {
          response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred during the search request.");
        }
        else
        {
          response.setStatus(HttpServletResponse.SC_OK);
          response.setContentType("application/json");
          response.getWriter().println(jsonResults);
        }
      }
    }
    catch (Exception ex)
    {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      ex.printStackTrace(response.getWriter());
    }
	} // -- protected void doGet() -- //

} // -- End of class MainControllerServlet -- //