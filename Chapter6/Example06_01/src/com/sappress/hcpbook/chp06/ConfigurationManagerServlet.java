package com.sappress.hcpbook.chp06;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sappress.hcpbook.chp06.connectivity.RfcProxyBean;
import com.sappress.hcpbook.chp06.persistence.ConfigurationManagerBean;
import com.sappress.hcpbook.chp06.persistence.Country;
import com.sappress.hcpbook.chp06.persistence.Region;

/**
 * Servlet implementation class ConfigurationManagerServlet
 */
public class ConfigurationManagerServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationManagerServlet.class);
	
	@EJB
	private ConfigurationManagerBean confManagerBean;
	
	@EJB
	private RfcProxyBean rfcProxyBean;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException 
	{
	  // Determine the course of action to take:
	  try
	  {
  	  String action = request.getParameter("action");
  	  if (action.equalsIgnoreCase("refreshDatabase"))
  	  {
  	    // Refresh the configuration database:
  	    logger.info("Dropping the configuration database...");
  	    confManagerBean.refreshDatabase();
  	    logger.info("Refresh is complete.");
  	    
  	    openNavigationEditor(request, response, "Configuration database has been refreshed.");
  	  }
  	  else if (action.equalsIgnoreCase("loadCountries"))
  	  {
  		  // Copy the countries into the cloud database:
  		  List<Country> countries = rfcProxyBean.getCountries();
  		  logger.info("Found " + countries.size() + " countries from the on-premise system.");
  		  for (Country c : countries)
  		  {
  		    logger.info("Inserting " + c.getCode() + " (" + c.getDescription() + ")");
  		    confManagerBean.addCountry(c);
  		    logger.info("Country " + c.getCode() + " added successfully.");
  		  }
		  
  		  openNavigationEditor(request, response, "Uploaded " + countries.size() + " countries into the database.");
  	  }
  	  else if (action.equalsIgnoreCase("loadRegions"))
  	  {
  		  // Copy region codes into the cloud database:
  	    String country = request.getParameter("country");
        List<Region> regions = rfcProxyBean.getRegionsByCountry(country);
        logger.info("Found " + regions.size() + " regions from the on-premise system.");
        for (Region r : regions)
        {
          logger.info("Inserting " + r.getCode() + " (" + r.getDescription() + ")");
          confManagerBean.addRegion(r);
          logger.info("Region " + r.getCode() + " added successfully.");
        }
        
        openNavigationEditor(request, response, "Uploaded " + regions.size() + " regions into the database.");
  		}
	  }
	  catch (Exception ex)
	  {
	    openNavigationEditor(request, response, "Exception: " + ex.getMessage());
	  }
	} // -- protected void doGet() -- //
	
	private void openNavigationEditor(HttpServletRequest request, HttpServletResponse response, String message)
	  throws ServletException, IOException
	{
	  // Route the user to the incident editor form:
	  request.setAttribute("message",  message);
	  
    RequestDispatcher rd = request.getRequestDispatcher("ConfigurationManager.jsp");
    rd.forward(request, response);
	}

} // -- public class ConfigurationManagerServlet -- //