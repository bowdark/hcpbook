package com.sappress.hcpbook.chp04;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
@WebServlet("/EnvReadServlet")
public class EnvReadServlet extends HttpServlet 
{
	// Create a logger instance:
  private static Logger logger =
    LoggerFactory.getLogger(EnvReadServlet.class);
       
  @Override
  public void init() throws ServletException
  {
    logger.debug("Servlet EnvReadServlet has been initialized.");
  }
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	  throws ServletException, IOException 
	{
	  // Output the server environment variables:
	  response.setContentType("text/plain");
	  
	  response.getWriter().println(
	    "Server Environment Variables:\n\n");
	  
	  Map<String, String> envVarMap = System.getenv();
	  for (String key : envVarMap.keySet())
	  {
	    String msg = "Variable [" + key + "]: " + envVarMap.get(key); 
	    response.getWriter().println(msg);
	    logger.info(msg);
	  }
	} // -- protected void doGet() -- //
	
	public void destroy()
	{
	  logger.debug("Servlet EnvReadServlet is being destroyed.");
	}
} // -- End of class EnvReadServlet -- //